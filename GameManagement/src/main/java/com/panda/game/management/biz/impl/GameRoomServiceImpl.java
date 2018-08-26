/***
 * @pName management
 * @name GameRoomServiceImpl
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.IGameRoomService;
import com.panda.game.management.entity.db.*;
import com.panda.game.management.entity.dbExt.GameRoomDetailInfo;
import com.panda.game.management.entity.resp.GameRoomCallbackResp;
import com.panda.game.management.exception.MsgException;
import com.panda.game.management.repository.*;
import com.panda.game.management.utils.IdWorker;
import com.panda.game.management.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class GameRoomServiceImpl extends BaseServiceImpl<GameRoom> implements IGameRoomService {
    private final GameRoomMapper gameRoomMapper;
    private final SubareaMapper subareaMapper;
    private final GameMemberGroupMapper gameMemberGroupMapper;
    private final SettlementMapper settlementMapper;
    private final RoomCardMapper roomCardMapper;
    private final WalletMapper walletMapper;


    @Autowired
    public GameRoomServiceImpl(GameRoomMapper gameRoomMapper, SubareaMapper subareaMapper, GameMemberGroupMapper gameMemberGroupMapper, SettlementMapper settlementMapper, RoomCardMapper roomCardMapper, WalletMapper walletMapper) {
        this.gameRoomMapper = gameRoomMapper;
        this.subareaMapper = subareaMapper;
        this.gameMemberGroupMapper = gameMemberGroupMapper;
        this.settlementMapper = settlementMapper;
        this.roomCardMapper = roomCardMapper;
        this.walletMapper = walletMapper;
    }

    /**
     * 插入数据 韦德 2018年8月18日23:01:25
     *
     * @param param
     * @return
     */
    @Override
    @Transactional
    public int insert(String token, GameRoom param){
        // 加载用户信息
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) return 0;

        String userId = map.get("userId");
        if(userId == null || userId.isEmpty()) throw new MsgException("身份校验失败");

        // 每位用户只能同时创建1个房间
        int playingCount = gameMemberGroupMapper.selectPlayingCount(Integer.valueOf(userId));
        if(playingCount >= 1) throw new MsgException("存在未结束的游戏，创建新房间失败！");

        // 加载游戏分区
        Subareas subareas = subareaMapper.selectByPrimaryKey(param.getSubareaId());
        if(subareas == null || subareas.getIsRelation() == 1) throw new MsgException("游戏分区不存在");

        // 加载游戏大区
        Subareas area = subareaMapper.selectByPrimaryKey(param.getParentAreaId());
        if(area == null) throw new MsgException("游戏大区不存在");

        Wallets wallets = walletMapper.selectByUid(Integer.valueOf(userId));
        if(wallets == null) throw new MsgException("查询钱包数据异常");
        Double balance = wallets.getBalance();
        if(balance < area.getLimitPrice()) throw new MsgException("金币" + area.getLimitPrice().intValue() + "枚以上才可以创建哟~");

        // 创建房间
        param.setOwnerId(Integer.valueOf(userId));
        param.setStatus(0);
        param.setAddTime(new Date());
        param.setUpdateTime(new Date());
        param.setIsEnable(1);

        long roomCode = 0;
        try {
            roomCode = IdWorker.getFlowIdWorkerInstance().nextId(8);
        } catch (Exception e) {
            throw new MsgException("生成房间ID失败");
        }
        param.setRoomCode(roomCode);
        param.setName(subareas.getName());

        int count = gameRoomMapper.insert(param);
        if(count == 0) throw new MsgException("创建房间失败");


        // 加入成员
        GameMemberGroup gameMemberGroup = new GameMemberGroup();
        gameMemberGroup.setRoomCode(roomCode);
        gameMemberGroup.setUserId(Integer.valueOf(userId));
        gameMemberGroup.setIsOwner(1);
        gameMemberGroup.setIsConfirm(0);
        gameMemberGroup.setAddTime(new Date());
        count = 0;
        count = gameMemberGroupMapper.insert(gameMemberGroup);
        if(count == 0) throw new MsgException("加入房间失败");
        return count;
    }

    /**
     * 查询数据 韦德 2018年8月19日15:46:11
     *
     * @param token
     * @return
     */
    @Override
    public List<GameRoomDetailInfo> getRoomList(String token) {
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) return null;

        String userId = map.get("userId");
        if(userId == null || userId.isEmpty()) throw new MsgException("身份校验失败");

        return gameRoomMapper.selectByUid(Integer.valueOf(userId));
    }

    /**
     * 解散房间 韦德 2018年8月20日01:05:48
     *
     * @param token
     * @param param
     */
    @Override
    @Transactional
    public void disband(String token, GameRoom param) {
        /*
            1、判断房间内是否有别人加入
            2、将房间status改为解散，前台不再显示
            3、更新member的退出时间
         */
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) return;

        String userId = map.get("userId");
        if(userId == null || userId.isEmpty()) throw new MsgException("身份校验失败");

        GameRoom gameRoom = gameRoomMapper.selectByRoomCode(param.getRoomCode());
        if(gameRoom == null) throw new MsgException("房间不存在");

        // 普通成员退出房间
        if(!gameRoom.getOwnerId().equals(Integer.valueOf(userId))) {
            // 如果游戏房间正处于游戏中，此时用户想要退出房间，必须要结算后才能退出去
            if(gameRoom.getStatus() == 2){
                int isConfirm = gameMemberGroupMapper.selectConfirm(Integer.valueOf(userId), param.getRoomCode());
                if(isConfirm <= 0) throw new MsgException("正在游戏中，请结算后再退出房间！");
            }

            int count = gameMemberGroupMapper.deleteMember(Integer.valueOf(userId),param.getRoomCode());
            if(count == 0) throw new MsgException("退出房间失败");

            // 加载游戏分区
            Subareas subareas = subareaMapper.selectByPrimaryKey(gameRoom.getSubareaId());
            if(subareas == null) throw new MsgException("游戏大区不存在");
            int onLineCount = gameRoomMapper.selectRoomOnLineCount(gameRoom.getRoomCode());
            onLineCount -= 1;
            if(onLineCount < subareas.getMaxPersonCount()) {
                count = gameRoomMapper.updateStatusByRoomCode(gameRoom.getRoomCode(), 0);
                if(count == 0) throw new MsgException("更改显示状态失败");
            }
            return;
        }

        List<GameMemberGroup> memberGroupList = gameMemberGroupMapper.selectByRoomCode(gameRoom.getRoomCode());
        if(!(memberGroupList == null || memberGroupList.isEmpty() )) throw new MsgException("请等房间内的成员全部退出后再解散");

        int count = gameMemberGroupMapper.updateMemberExit(gameRoom.getRoomCode());
        if(count == 0) throw new MsgException("清空成员失败");

        count = 0;
        count = gameRoomMapper.updateStatusByRoomCode(gameRoom.getRoomCode(), 6);
        if(count == 0) throw new MsgException("解散房间失败");

    }


    /**
     * 申请结算 韦德 2018年8月20日01:07:26
     *
     * @param token
     * @param roomCode
     * @param grade
     */
    @Override
    @Transactional
    public void closeAccounts(String token, Long roomCode, Double grade, Consumer<GameRoomCallbackResp> lastPersonCallback) {
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) return;

        String userId = map.get("userId");
        if(userId == null || userId.isEmpty()) throw new MsgException("身份校验失败");

        GameRoom room = gameRoomMapper.selectByRoomCode(roomCode);
        if(room == null) throw new MsgException("房间不存在");
        if(room.getStatus() == 0) throw new MsgException("游戏未开始");

        Subareas subareas = subareaMapper.selectByPrimaryKey(room.getParentAreaId());
        if(subareas == null) throw new MsgException("游戏分区不存在");

        int count = gameMemberGroupMapper.updateConfirm(Integer.valueOf(userId), roomCode);
        if(count == 0) throw new MsgException("更新结算状态失败");

        Settlement settlement = new Settlement();
        settlement.setUserId(Integer.valueOf(userId));
        settlement.setGrade(grade);
        settlement.setRoomCode(roomCode);
        settlement.setState(0);
        settlement.setAddTime(new Date());
        settlement.setUpdateTime(new Date());
        count = settlementMapper.insert(settlement);
        if(count == 0) throw new MsgException("申请结算失败");

        List<GameMemberGroup> memberGroupList = gameMemberGroupMapper.selectNotSettlementByRoomCode(roomCode);
        if(memberGroupList == null || memberGroupList.isEmpty()){
            count = gameRoomMapper.updateStatusByRoomCode(roomCode,6);
            if(count == 0) throw new MsgException("解散房间失败");
            GameRoomCallbackResp gameRoomCallbackResp = new GameRoomCallbackResp(room, subareas);
            lastPersonCallback.accept(gameRoomCallbackResp);
        }
    }


    /**
     * 获取所有游戏房间 韦德 2018年8月20日21:20:09
     *
     * @return
     */
    @Override
    public List<GameRoomDetailInfo> getAllRoomList() {
        return gameRoomMapper.selectSalaRoomList();
    }

    /**
     * 加入房间 韦德 2018年8月20日21:40:51
     *
     * @param token
     * @param gameRoom
     */
    @Override
    public boolean join(String token, GameRoom gameRoom) {
        // 加载用户信息
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) return false;

        String userId = map.get("userId");
        if(userId == null || userId.isEmpty()) throw new MsgException("身份校验失败");

        GameRoom room = gameRoomMapper.selectByRoomCode(gameRoom.getRoomCode());
        if(room == null) throw new MsgException("房间不存在");

        if(room.getOwnerId().equals(Integer.valueOf(userId))) throw new MsgException("您是此房间的房主！");


        // 加载游戏大区
        Subareas area = subareaMapper.selectByPrimaryKey(room.getParentAreaId());
        if(area == null) throw new MsgException("游戏大区不存在");

        // 加载钱包信息
        Wallets wallets = walletMapper.selectByUid(Integer.valueOf(userId));
        if(wallets == null) throw new MsgException("查询钱包数据异常");
        Double balance = wallets.getBalance();
        if(balance < area.getLimitPrice()) throw new MsgException("金币" + area.getLimitPrice().intValue() + "枚以上才可以加入哟~");

        // 加载游戏分区
        Subareas subareas = subareaMapper.selectByPrimaryKey(room.getSubareaId());
        if(subareas == null) throw new MsgException("游戏大区不存在");
        int onLineCount = gameRoomMapper.selectRoomOnLineCount(gameRoom.getRoomCode());
        onLineCount += 1;
        if(onLineCount > subareas.getMaxPersonCount()) throw new MsgException("该房间人数已满！");
        // 更改状态为已开始
        if(onLineCount == subareas.getMaxPersonCount()){
            int count = gameRoomMapper.updateStatusByRoomCode(gameRoom.getRoomCode(), 2);
            if(count == 0) throw new MsgException("更改显示状态失败");
        }

        // 加入成员
        GameMemberGroup gameMemberGroup = new GameMemberGroup();
        gameMemberGroup.setRoomCode(gameRoom.getRoomCode());
        gameMemberGroup.setUserId(Integer.valueOf(userId));
        gameMemberGroup.setIsOwner(0);
        gameMemberGroup.setIsConfirm(0);
        gameMemberGroup.setAddTime(new Date());
        int count = gameMemberGroupMapper.insert(gameMemberGroup);
        if(count == 0) throw new MsgException("加入房间失败");
        return true;
    }

    /**
     * 领取房卡 韦德 2018年8月20日23:28:15
     *
     * @param token
     * @param users
     */
    @Override
    public void getRoomCard(String token, Users users) {
        // 加载用户信息
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) return;

        String userId = map.get("userId");
        if(userId == null || userId.isEmpty()) throw new MsgException("身份校验失败");

        RoomCard roomCard = roomCardMapper.selectLast(Integer.valueOf(userId), users.getPandaId());
        if(roomCard != null){
            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;

            Long currentTime = new Date().getTime();
            Long addTime = roomCard.getAddTime().getTime();
            // 获得两个时间的毫秒时间差异
            Long diff = currentTime - addTime;
            // 计算差多少天
            long day = diff / nd;
            // 计算差多少小时
            long hour = diff % nd / nh;
            // 计算差多少分钟
            long min = diff % nd % nh / nm;

            if(min < 180) throw new MsgException("限隔180分钟领取一次~");
        }


        Wallets wallets = walletMapper.selectByUid(Integer.valueOf(userId));
        if(wallets == null) throw new MsgException("查询钱包数据异常");
        Double balance = wallets.getBalance();
        if(balance < 100) throw new MsgException("金币100枚以上才可以领取哟~");

        // TODO 这里要推送熊猫服务器才能决定此次是否成功
        roomCard = new RoomCard();
        roomCard.setUserId(Integer.valueOf(userId));
        roomCard.setPandaId(users.getPandaId());
        roomCard.setState(0);
        roomCard.setAddTime(new Date());
        int count = roomCardMapper.insert(roomCard);
        if(count == 0) throw new MsgException("领取失败");
    }

    /**
     * 根据分区匹配房间 韦德 2018年8月21日17:11:02
     *
     * @param subareasId
     * @return
     */
    @Override
    public GameRoomDetailInfo getLimitRoom(String subareasId) {
        return gameRoomMapper.getLimitRoom(subareasId);
    }

    /**
     * 查询房间人数 韦德 2018年8月24日15:48:45
     *
     * @param roomCode
     */
    @Override
    public Integer getPersonCount(String roomCode) {
        return gameMemberGroupMapper.selectPersonCount(roomCode);
    }
}
