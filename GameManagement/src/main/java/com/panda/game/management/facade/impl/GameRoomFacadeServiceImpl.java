/***
 * @pName management
 * @name GameRoomFacadeServiceImpl
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.panda.game.management.facade.impl;

import com.panda.game.management.entity.Constant;
import com.panda.game.management.entity.db.GameMemberGroup;
import com.panda.game.management.entity.param.PayParam;
import com.panda.game.management.exception.InfoException;
import com.panda.game.management.facade.GameRoomFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameRoomFacadeServiceImpl implements GameRoomFacadeService {
    @Autowired
    private com.panda.game.management.biz.IGameRoomService IGameRoomService;
    @Autowired
    private com.panda.game.management.biz.IGameMemberGroupService IGameMemberGroupService;
    @Autowired
    private com.panda.game.management.biz.IPayService IPayService;

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
        IGameRoomService.closeAccounts(token, roomCode, grade, (gameRoomCallbackResp) -> {
            if(gameRoomCallbackResp == null || gameRoomCallbackResp.getGameRoom() == null || gameRoomCallbackResp.getSubareas() == null)
                throw new InfoException("回调参数有误");

            // 扣除此房间内所有人的金币
            List<GameMemberGroup> memberList = IGameMemberGroupService.getListByRoom(gameRoomCallbackResp.getGameRoom().getRoomCode());
            if(memberList == null || memberList.isEmpty()) throw new InfoException("加载房间成员列表失败");

            List<PayParam> payParams = new ArrayList<>();
            memberList.forEach(member -> {
                Double price = gameRoomCallbackResp.getSubareas().getReducePrice();
                PayParam payParam = new PayParam();
                // 优先扣减不可用余额
                Double notWithdrawAmount = IPayService.getNotWithdrawAmount(member.getUserId());
                if(notWithdrawAmount > price) payParam.setCurrency(1);
                payParam.setFromUid(member.getUserId());
                payParam.setAmount(price);
                payParam.setToUid(Constant.SYSTEM_ACCOUNTS_ID);
                payParams.add(payParam);
            });
            IPayService.batchConsume(payParams);
        });
    }
}
