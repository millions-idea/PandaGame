/***
 * @pName management
 * @name ISettlementService
 * @user HongWei
 * @date 2018/8/28
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.entity.db.Settlement;

import java.util.List;

public interface ISettlementService extends IBaseService<Settlement> {
    /**
     * 加载房间成员列表 韦德 2018年8月28日22:10:25
     * @param roomCode
     * @return
     */
    List<Settlement> getMemberList(Long roomCode);
}
