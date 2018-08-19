/***
 * @pName management
 * @name GameRoomServiceImpl
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.GameRoomService;
import com.panda.game.management.entity.db.GameMemberGroup;
import com.panda.game.management.entity.db.GameRoom;
import com.panda.game.management.entity.db.Subareas;
import com.panda.game.management.entity.dbExt.GameRoomDetailInfo;
import com.panda.game.management.exception.MsgException;
import com.panda.game.management.repository.GameMemberGroupMapper;
import com.panda.game.management.repository.GameRoomMapper;
import com.panda.game.management.repository.SubareaMapper;
import com.panda.game.management.utils.IdWorker;
import com.panda.game.management.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GameRoomServiceImpl extends BaseServiceImpl<GameRoom> implements GameRoomService {
    private final GameRoomMapper gameRoomMapper;
    private final SubareaMapper subareaMapper;
    private final GameMemberGroupMapper gameMemberGroupMapper;

    @Autowired
    public GameRoomServiceImpl(GameRoomMapper gameRoomMapper, SubareaMapper subareaMapper, GameMemberGroupMapper gameMemberGroupMapper) {
        this.gameRoomMapper = gameRoomMapper;
        this.subareaMapper = subareaMapper;
        this.gameMemberGroupMapper = gameMemberGroupMapper;
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

        // 加载游戏大区
        Subareas subareas = subareaMapper.selectByPrimaryKey(param.getSubareaId());
        if(subareas == null || subareas.getIsRelation() == 1) throw new MsgException("游戏模式不存在");

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
     * @param gameRoom
     */
    @Override
    public void disband(String token, GameRoom gameRoom) {

    }

    /**
     * 申请结算 韦德 2018年8月20日01:07:26
     *
     * @param token
     * @param standings
     * @param beRouted
     */
    @Override
    public void closeAccounts(String token, Double standings, Double beRouted) {

    }
}
