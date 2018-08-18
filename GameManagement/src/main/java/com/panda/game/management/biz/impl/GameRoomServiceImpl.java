/***
 * @pName management
 * @name GameRoomServiceImpl
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.GameRoomService;
import com.panda.game.management.entity.db.GameRoom;
import com.panda.game.management.entity.db.Subareas;
import com.panda.game.management.exception.MsgException;
import com.panda.game.management.repository.GameRoomMapper;
import com.panda.game.management.repository.SubareaMapper;
import com.panda.game.management.utils.IdWorker;
import com.panda.game.management.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class GameRoomServiceImpl extends BaseServiceImpl<GameRoom> implements GameRoomService {
    private final GameRoomMapper gameRoomMapper;
    private final SubareaMapper subareaMapper;

    @Autowired
    public GameRoomServiceImpl(GameRoomMapper gameRoomMapper, SubareaMapper subareaMapper) {
        this.gameRoomMapper = gameRoomMapper;
        this.subareaMapper = subareaMapper;
    }

    /**
     * 插入数据 韦德 2018年8月18日23:01:25
     *
     * @param param
     * @return
     */
    @Override
    public int insert(String token, GameRoom param){
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) return 0;

        String userId = map.get("userId");
        if(userId == null || userId.isEmpty()) throw new MsgException("身份校验失败");

        Subareas subareas = subareaMapper.selectByPrimaryKey(param.getSubareaId());
        if(subareas == null || subareas.getIsRelation() == 1) throw new MsgException("游戏模式不存在");

        param.setOwnerId(Integer.valueOf(userId));
        param.setStatus(0);
        param.setAddTime(new Date());
        param.setUpdateTime(new Date());
        param.setIsEnable(1);

        long roomCode = 0;
        try {
            roomCode = IdWorker.getFlowIdWorkerInstance().nextId(8);
        } catch (Exception e) {
            throw new MsgException("生成房间号失败");
        }
        param.setRoomCode(roomCode);
        param.setName(subareas.getName());

        int count = gameRoomMapper.insert(param);
        if(count == 0) throw new MsgException("创建房间失败");
        return count;
    }
}
