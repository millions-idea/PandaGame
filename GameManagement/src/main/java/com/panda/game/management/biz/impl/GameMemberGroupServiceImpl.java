/***
 * @pName management
 * @name GameMemberGroupServiceImpl
 * @user HongWei
 * @date 2018/8/19
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.GameMemberGroupService;
import com.panda.game.management.entity.db.GameMemberGroup;
import com.panda.game.management.repository.GameMemberGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameMemberGroupServiceImpl extends BaseServiceImpl<GameMemberGroup> implements GameMemberGroupService {
    private final GameMemberGroupMapper gameMemberGroupMapper;

    @Autowired
    public GameMemberGroupServiceImpl(GameMemberGroupMapper gameMemberGroupMapper) {
        this.gameMemberGroupMapper = gameMemberGroupMapper;
    }

    @Override
    public List<GameMemberGroup> getListByRoom(Long roomCode) {
        return gameMemberGroupMapper.getListByRoom(roomCode);
    }
}
