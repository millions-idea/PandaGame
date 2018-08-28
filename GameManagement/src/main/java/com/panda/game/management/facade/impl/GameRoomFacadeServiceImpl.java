/***
 * @pName management
 * @name GameRoomFacadeServiceImpl
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.panda.game.management.facade.impl;

import com.panda.game.management.biz.IGameMemberGroupService;
import com.panda.game.management.biz.IGameRoomService;
import com.panda.game.management.biz.IPayService;
import com.panda.game.management.biz.ISettlementService;
import com.panda.game.management.entity.Constant;
import com.panda.game.management.entity.db.GameMemberGroup;
import com.panda.game.management.entity.db.Settlement;
import com.panda.game.management.entity.param.PayParam;
import com.panda.game.management.entity.resp.GameRoomCallbackResp;
import com.panda.game.management.exception.InfoException;
import com.panda.game.management.facade.GameRoomFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameRoomFacadeServiceImpl implements GameRoomFacadeService {
    @Autowired
    private IGameRoomService gameRoomService;
    @Autowired
    private IGameMemberGroupService gameMemberGroupService;
    @Autowired
    private IPayService payService;
    @Autowired
    private ISettlementService settlementService;

    /**
     * 结算 韦德 2018年8月21日01:02:27
     *
     * @param token
     * @param roomCode
     * @param grade
     */
    @Override
    @Transactional
    public void closeAccounts(String token, Long roomCode, Double grade) {
        // 结算
        gameRoomService.closeAccounts(token, roomCode, grade, (gameRoomCallbackResp) -> {
            if(gameRoomCallbackResp == null || gameRoomCallbackResp.getGameRoom() == null || gameRoomCallbackResp.getSubareas() == null)
                throw new InfoException("回调参数有误");

            // 扣除此房间内所有人的金币
            List<GameMemberGroup> memberList = gameMemberGroupService.getListByRoom(gameRoomCallbackResp.getGameRoom().getRoomCode());
            if(memberList == null || memberList.isEmpty()) throw new InfoException("加载房间成员列表失败");

            List<PayParam> payParams = new ArrayList<>();
            memberList.forEach(member -> {
                Double price = gameRoomCallbackResp.getSubareas().getReducePrice();
                PayParam payParam = new PayParam();
                // 优先扣减不可用余额
                Double notWithdrawAmount = payService.getNotWithdrawAmount(member.getUserId());
                if (notWithdrawAmount > price) payParam.setCurrency(1);
                payParam.setFromUid(member.getUserId());
                payParam.setAmount(price);
                payParam.setToUid(Constant.SYSTEM_ACCOUNTS_ID);
                payParams.add(payParam);
            });
            payService.batchConsume(payParams);

            // 计算房间内所有人的成绩
            // 如果所有人的成绩相加运算后得到结果是0，系统自动进行结算

            // 1、计算房间总成绩
            List<Settlement> settlementList = settlementService.getMemberList(roomCode);
            Double countGrade = settlementList.stream().map(s -> s.getGrade()).reduce(0D, (acc, element) -> acc + element);
            if(settlementList == null || settlementList.isEmpty() || countGrade != 0) return;// 说明有人误报成绩，交给后台人工复审成绩

            // 2、给此房间内的所有人进行结算
            List<Settlement> goodGradeMemberList = settlementList.stream().filter(s -> s.getGrade() > 0).collect(Collectors.toList());
            List<Settlement> badGradeMemberList = settlementList.stream().filter(s -> s.getGrade() <= 0).collect(Collectors.toList());

            List<PayParam> goodSettlementPayParams = new ArrayList<>();
            badGradeMemberList.forEach(member -> {
                PayParam payParam = loopReduceBalance(gameRoomCallbackResp, member.getUserId());
                goodSettlementPayParams.add(payParam);
            });
            payService.batchTransfer(goodSettlementPayParams);


            List<PayParam> badSettlementPayParams = new ArrayList<>();
            goodGradeMemberList.forEach(member -> {
                PayParam payParam = loopAddBalance(gameRoomCallbackResp, member.getUserId());
                badSettlementPayParams.add(payParam);
            });
            payService.batchTransfer(badSettlementPayParams);
        });
    }

    private PayParam loopReduceBalance(GameRoomCallbackResp gameRoomCallbackResp, Integer userId) {
        Double price = gameRoomCallbackResp.getSubareas().getReducePrice();
        PayParam payParam = new PayParam();
        // 优先扣减不可用余额
        Double notWithdrawAmount = payService.getNotWithdrawAmount(userId);
        if (notWithdrawAmount > price) payParam.setCurrency(1);
        payParam.setFromUid(userId);
        payParam.setAmount(price);
        payParam.setToUid(Constant.SYSTEM_ACCOUNTS_ID);
        return payParam;
    }


    private PayParam loopAddBalance(GameRoomCallbackResp gameRoomCallbackResp, Integer userId) {
        Double price = gameRoomCallbackResp.getSubareas().getReducePrice();
        PayParam payParam = new PayParam();
        payParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
        payParam.setAmount(price);
        payParam.setToUid(userId);
        return payParam;
    }
}
