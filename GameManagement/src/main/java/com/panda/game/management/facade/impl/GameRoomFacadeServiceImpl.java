/***
 * @pName management
 * @name GameRoomFacadeServiceImpl
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.panda.game.management.facade.impl;

import com.panda.game.management.biz.*;
import com.panda.game.management.entity.Constant;
import com.panda.game.management.entity.DataDictionary;
import com.panda.game.management.entity.db.*;
import com.panda.game.management.entity.dbExt.GameMemberGroupDetailInfo;
import com.panda.game.management.entity.dbExt.SettlementDetailInfo;
import com.panda.game.management.entity.param.PayParam;
import com.panda.game.management.entity.resp.GameRoomCallbackResp;
import com.panda.game.management.entity.resp.GroupInformation;
import com.panda.game.management.exception.InfoException;
import com.panda.game.management.exception.MsgException;
import com.panda.game.management.facade.GameRoomFacadeService;
import com.panda.game.management.utils.PhoneUtil;
import com.panda.game.management.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private IMessageService messageService;
    @Autowired
    private ISubareaService subareaService;

    private Integer lock =111697367;

    /**
     * 结算 韦德 2018年8月21日01:02:27
     *
     * @param token
     * @param roomCode
     * @param grade
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void closeAccounts(String token, Long roomCode, Double grade) {
        // 结算
        GameRoomCallbackResp callback = gameRoomService.closeAccounts(token, roomCode, grade);
        if(callback.getUserId() ==  -1) return;

        // 计算房间内所有人的成绩
        // 如果所有人的成绩相加运算后得到结果是0，系统自动进行结算

        // 扣除此房间内所有人的金币
        List<GameMemberGroupDetailInfo> memberList = gameMemberGroupService.getDetailInfoListByRoom(callback.getGameRoom().getRoomCode());
        if(memberList == null || memberList.isEmpty()) throw new InfoException("加载房间成员列表失败");
        // 查询所有人的成绩信息
        List<SettlementDetailInfo> settlementList = settlementService.getMemberList(roomCode);
        if(settlementList == null || settlementList.isEmpty()) throw new InfoException("加载房间成员成绩失败");

        List<PayParam> reduceRoomCostPayParams = new ArrayList<>();
        memberList.forEach(member -> {
            PayParam payParam = new PayParam();
            // 扣除房费
            Double price = callback.getSubareas().getReducePrice();
            // 优先扣减不可用余额
            Double notWithdrawAmount = payService.getNotWithdrawAmount(member.getUserId());
            if (notWithdrawAmount > price) payParam.setCurrency(1);
            payParam.setFromUid(member.getUserId());
            payParam.setAmount(price);
            payParam.setToUid(Constant.SYSTEM_ACCOUNTS_ID);
            reduceRoomCostPayParams.add(payParam);


            // 代理佣金计算(判断该用户是否是第一次游戏，如果是，向他的上级用户发放邀请红包，如果不是第一次，向他的上级发放抽成收益)
            if(member.getParentUserId() != null && member.getParentUserId() > 0){
                // 得到用户游戏总局数
                Integer joinCount = member.getJoinCount();
                // 得到系统指定的阈值条件
                String osJoinCount = DataDictionary.DATA_DICTIONARY.values().stream()
                        .filter(d -> d.getKey().contains("invite.joinCount")).findFirst().get().getValue();
                String regPackagePrice = DataDictionary.DATA_DICTIONARY.values().stream()
                        .filter(d -> d.getKey().contains("invite.regPackagePrice")).findFirst().get().getValue();

                // 用户满足预设条件
                Integer parentId = member.getParentUserId();
                if(joinCount >= Integer.valueOf(osJoinCount)){
                    // 用户满足首次注册条件
                    if(joinCount == 1){
                        PayParam regPackagePayParam = new PayParam();
                        regPackagePayParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
                        regPackagePayParam.setToUid(parentId);
                        regPackagePayParam.setAmount(Double.valueOf(regPackagePrice));
                        regPackagePayParam.setRemark("新用户邀请注册红包");
                        reduceRoomCostPayParams.add(regPackagePayParam);
                        messageService.pushMessage(new Messages(null, parentId
                                , "【邀请红包】" + PhoneUtil.getEncrypt(member.getPhone()) +"贡献的" + regPackagePrice + "元已到账!", 0, new Date()));
                    }else{
                        // 向邀请方赠送游戏收益
                        String playAwardPrice = DataDictionary.DATA_DICTIONARY.values().stream()
                                .filter(d -> d.getKey().contains("invite.playAwardPrice")).findFirst().get().getValue();
                        PayParam regPackagePayParam = new PayParam();
                        regPackagePayParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
                        regPackagePayParam.setToUid(parentId);
                        regPackagePayParam.setAmount(Double.valueOf(playAwardPrice));
                        regPackagePayParam.setRemark("游戏返利奖励");
                        reduceRoomCostPayParams.add(regPackagePayParam);
                        messageService.pushMessage(new Messages(null, parentId
                                , "【佣金返利】" + PhoneUtil.getEncrypt(member.getPhone()) +"贡献的" + playAwardPrice + "元已到账!", 0, new Date()));
                    }
                }
            }

        });
        payService.batchConsume(reduceRoomCostPayParams);

        // 1、计算房间总成绩
        Double countGrade = settlementList.stream().map(s -> s.getGrade()).reduce(0D, (acc, element) -> acc + element);
        if(settlementList == null || settlementList.isEmpty() || countGrade != 0) return;// 说明有人误报成绩，交给后台人工复审成绩

        int count = gameRoomService.updateStatusCodeVersion(roomCode, 5);
        if(count == 0) throw new MsgException("更新结算状态失败[A00]");

        count = 0;
        count = settlementService.updateStatusByRoomCode(roomCode, 1);
        if(count == 0) throw new MsgException("更新结算状态失败[A02]");


        // 扣游戏金币
        List<PayParam> consumePayParams = new ArrayList<>();
        memberList.forEach(member -> {
            Double price = callback.getSubareas().getReducePrice();
            GameRoom gameRoom = callback.getGameRoom();
            Subareas subarea = callback.getSubareas();
            Integer parentAreaId = gameRoom.getParentAreaId();
            SettlementDetailInfo memberSettlement = settlementList.stream()
                    .filter(settlementDetailInfo -> settlementDetailInfo.getUserId() == member.getUserId())
                    .findFirst().get();
            Double memberGrade = memberSettlement.getGrade();

            // 判断是否为负数，如果是负数，要先转为正数，方便计算

            Double finalGrade = memberGrade;
            if(memberGrade < 0) finalGrade = memberGrade - memberGrade * 2;


            // 体验区计算公式：80*0.1-0.1=+7.9, 80*0.1+0.1=-8.1
            // 1元区计算公式：80-1=+79, 80+1=-81
            // 2元区计算公式：80*2-1=+79, 80*5+1=-81
            // 5元区计算公式：80*5-2=+158, 80*5+2=-162


            // 2元区和5元区的计算公式与其他分区的不同
            if(parentAreaId == 3 || parentAreaId == 4){
                price = finalGrade * subarea.getPrice();
            }else {
                price = finalGrade * subarea.getReducePrice();
            }


            // 优先扣减不可用余额
            PayParam payParam = new PayParam();
            Double notWithdrawAmount = payService.getNotWithdrawAmount(member.getUserId());
            if (notWithdrawAmount > price) payParam.setCurrency(1);

            if(memberGrade < 0){
                payParam.setFromUid(member.getUserId());
                payParam.setToUid(Constant.SYSTEM_ACCOUNTS_ID);
            } else {
                payParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
                payParam.setToUid(member.getUserId());
            }
            payParam.setAmount(price);

            consumePayParams.add(payParam);

        });
        payService.batchConsume(consumePayParams);


        List<Messages> messagesList = new ArrayList<>();
        memberList.forEach(member -> {
            messagesList.add(new Messages(null, member.getUserId()
                    ,  callback.getGameRoom().getExternalRoomId() + "房间结算审核通过通知", 0, new Date()));
        });
        messageService.pushMessage(messagesList);
    }

    /**
     * 结算 韦德 2018年8月29日20:53:36
     *
     * @param roomCode
     */
    @Override
    @Transactional
    @Deprecated
    public void closeAccounts(Long roomCode) {
        // 结算
        GameRoomCallbackResp callback = gameRoomService.closeAccounts(roomCode);
        if(callback.getUserId() ==  -1) return;

        // 计算房间内所有人的成绩
        // 如果所有人的成绩相加运算后得到结果是0，系统自动进行结算

        // 1、计算房间总成绩

        synchronized (lock) {
            List<SettlementDetailInfo> settlementList = settlementService.getMemberList(roomCode);
            Double countGrade = settlementList.stream().map(s -> s.getGrade()).reduce(0D, (acc, element) -> acc + element);
            if(settlementList == null || settlementList.isEmpty() || countGrade != 0) throw new InfoException("房间总成绩有误，请重新计算！");// 说明有人误报成绩，交给后台人工复审成绩

            GameRoom room = gameRoomService.getRoom(roomCode);
            int count = gameRoomService.updateStatusCodeVersion(roomCode, 5);
            if(count == 0) throw new MsgException("更新结算状态失败[A00]");

            count = 0;
            count = settlementService.updateStatusByRoomCode(roomCode, 1);
            if(count == 0) throw new MsgException("更新结算状态失败[A02]");
        }

        List<GameMemberGroup> memberList = gameMemberGroupService.getListByRoom(callback.getGameRoom().getRoomCode());
        if(memberList == null || memberList.isEmpty()) throw new InfoException("加载房间成员列表失败");

        List<Messages> messagesList = new ArrayList<>();
        memberList.forEach(member -> {
            messagesList.add(new Messages(null, member.getUserId()
                    ,  callback.getGameRoom().getExternalRoomId() + "房间结算审核通过通知", 0, new Date()));
        });
        messageService.pushMessage(messagesList);
    }

    /**
     * 强制结算 韦德 2018年9月2日12:56:44
     *
     * @param roomCode
     */
    @Override
    public void executeCloseAccounts(Long roomCode) {
        // 强制修改状态、解散房间
        gameRoomService.executeCloseAccounts(roomCode, (callback) -> {
            List<GameMemberGroup> memberList = gameMemberGroupService.getListByRoom(callback.getGameRoom().getRoomCode());
            if(memberList == null || memberList.isEmpty()) throw new InfoException("加载房间成员列表失败");

            // 计算房间总成绩
            List<SettlementDetailInfo> settlementList = settlementService.getMemberList(roomCode);
            Double countGrade = settlementList.stream().map(s -> s.getGrade()).reduce(0D, (acc, element) -> acc + element);
            if(settlementList == null || settlementList.isEmpty() || countGrade != 0) throw new InfoException("房间总成绩有误，请重新计算！");// 说明有人误报成绩，交给后台人工复审成绩

            // 扣游戏金币
            List<PayParam> consumePayParams = new ArrayList<>();
            memberList.forEach(member -> {
                Double price = callback.getSubareas().getReducePrice();
                GameRoom gameRoom = callback.getGameRoom();
                Subareas subarea = callback.getSubareas();
                Integer parentAreaId = gameRoom.getParentAreaId();
                SettlementDetailInfo memberSettlement = settlementList.stream()
                        .filter(settlementDetailInfo -> settlementDetailInfo.getUserId() == member.getUserId())
                        .findFirst().get();
                Double memberGrade = memberSettlement.getGrade();

                // 判断是否为负数，如果是负数，要先转为正数，方便计算

                Double finalGrade = memberGrade;
                if(memberGrade < 0) finalGrade = memberGrade - memberGrade * 2;

                // 体验区计算公式：80*0.1-0.1=+7.9, 80*0.1+0.1=-8.1
                // 1元区计算公式：80-1=+79, 80+1=-81
                // 2元区计算公式：80*2-1=+79, 80*5+1=-81
                // 5元区计算公式：80*5-2=+158, 80*5+2=-162


                // 2元区和5元区的计算公式与其他分区的不同
                if(parentAreaId == 3 || parentAreaId == 4){
                    price = finalGrade * subarea.getPrice();
                }else {
                    price = finalGrade * subarea.getReducePrice();
                }


                // 优先扣减不可用余额
                PayParam payParam = new PayParam();
                Double notWithdrawAmount = payService.getNotWithdrawAmount(member.getUserId());
                if (notWithdrawAmount > price) payParam.setCurrency(1);

                if(memberGrade < 0){
                    payParam.setFromUid(member.getUserId());
                    payParam.setToUid(Constant.SYSTEM_ACCOUNTS_ID);
                } else {
                    payParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
                    payParam.setToUid(member.getUserId());
                }
                payParam.setAmount(price);

                consumePayParams.add(payParam);

            });
            payService.batchConsume(consumePayParams);

            // 推送解散通知
            List<Messages> messagesList = new ArrayList<>();
            memberList.forEach(member -> {
                messagesList.add(new Messages(null, member.getUserId()
                        ,  callback.getGameRoom().getExternalRoomId() + "房间结算审核通过通知", 0, new Date()));
            });
            messageService.pushMessage(messagesList);
        });

    }

    /**
     * 修改成绩 韦德 2018年8月29日20:31:57
     *
     * @param userId
     * @param grade
     * @param roomCode
     */
    @Override
    @Transactional
    public void editGrade(Integer userId, Double grade, Long roomCode) {
        // 查询房间分区信息
        GameRoom room = gameRoomService.getRoom(roomCode);
        if(room == null) throw new InfoException("查询房间失败");
        Integer parentAreaId = room.getParentAreaId();

        // 查询分区信息
        Subareas subarea = subareaService.getSubarea(parentAreaId);
        if(subarea == null) throw new InfoException("查询游戏分区失败");

        // 查询原来的成绩
        Settlement member = settlementService.getMemberGrade(userId, roomCode);
        Double oldGrade = member.getGrade();

        Double price = 0D;

        // 判断是否为负数，如果是负数，要先转为正数，方便计算
        Double finalGrade = grade;
        if(grade < 0) finalGrade = grade - grade * 2;

        // 体验区计算公式：80*0.1-0.1=+7.9, 80*0.1+0.1=-8.1
        // 1元区计算公式：80-1=+79, 80+1=-81
        // 2元区计算公式：80*1-1=+79, 80*1+1=-81
        // 5元区计算公式：80*2-2=+158, 80*2+2=-162
        price = finalGrade * subarea.getReducePrice();

        // 优先扣减不可用余额
        PayParam payParam = new PayParam();
        Double notWithdrawAmount = payService.getNotWithdrawAmount(userId);
        if (notWithdrawAmount > price) payParam.setCurrency(1);

        if(grade < 0){
            payParam.setFromUid(userId);
            payParam.setToUid(Constant.SYSTEM_ACCOUNTS_ID);
        } else {
            payParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
            payParam.setToUid(userId);
        }
        payParam.setAmount(price);
        payService.consume(payParam);

        // 修改成绩
        settlementService.editGrade(userId, grade, roomCode);
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
