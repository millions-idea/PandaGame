/***
 * @pName management
 * @name GameMemberGroupService
 * @user HongWei
 * @date 2018/8/19
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.entity.db.GameMemberGroup;

import java.util.List;

public interface IGameMemberGroupService extends IBaseService<GameMemberGroup> {
    /**
     * 查询指定房间内的成员列表 韦德 2018年8月21日01:31:06
     * @param roomCode
     * @return
     */
    List<GameMemberGroup> getListByRoom(Long roomCode);
}
