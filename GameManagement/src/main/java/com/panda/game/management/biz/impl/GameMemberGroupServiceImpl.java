/***
 * @pName management
 * @name GameMemberGroupServiceImpl
 * @user HongWei
 * @date 2018/8/19
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.IGameMemberGroupService;
import com.panda.game.management.entity.db.GameMemberGroup;
import com.panda.game.management.entity.dbExt.GameMemberGroupDetailInfo;
import com.panda.game.management.repository.GameMemberGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameMemberGroupServiceImpl extends BaseServiceImpl<GameMemberGroup> implements IGameMemberGroupService {
    private final GameMemberGroupMapper gameMemberGroupMapper;

    @Autowired
    public GameMemberGroupServiceImpl(GameMemberGroupMapper gameMemberGroupMapper) {
        this.gameMemberGroupMapper = gameMemberGroupMapper;
    }

    @Override
    @Transactional
    public List<GameMemberGroup> getListByRoom(Long roomCode) {
        return gameMemberGroupMapper.getListByRoom(roomCode);
    }

    /**
     * 查询指定房间内的成员列表 韦德 2018年8月21日01:31:06
     *
     * @param roomCode
     * @return
     */
    @Override
    public List<GameMemberGroupDetailInfo> getDetailInfoListByRoom(Long roomCode) {
        return gameMemberGroupMapper.getDetailInfoListByRoom(roomCode);
    }
}
