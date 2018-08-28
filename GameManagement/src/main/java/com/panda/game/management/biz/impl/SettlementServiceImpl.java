/***
 * @pName management
 * @name SettlementServiceImpl
 * @user HongWei
 * @date 2018/8/28
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.ISettlementService;
import com.panda.game.management.entity.db.Settlement;
import com.panda.game.management.repository.SettlementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettlementServiceImpl extends BaseServiceImpl<Settlement> implements ISettlementService {
    private final SettlementMapper settlementMapper;

    @Autowired
    public SettlementServiceImpl(SettlementMapper settlementMapper) {
        this.settlementMapper = settlementMapper;
    }


    /**
     * 加载房间成员列表 韦德 2018年8月28日22:10:25
     *
     * @param roomCode
     * @return
     */
    @Override
    public List<Settlement> getMemberList(Long roomCode) {
        Settlement settlement = new Settlement();
        settlement.setRoomCode(roomCode);
        return settlementMapper.select(settlement);
    }
}
