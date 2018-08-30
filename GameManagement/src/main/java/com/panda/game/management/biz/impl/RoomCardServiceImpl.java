/***
 * @pName management
 * @name RoomCardServiceImpl
 * @user HongWei
 * @date 2018/8/30
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.IRoomCardService;
import com.panda.game.management.entity.db.RoomCard;
import com.panda.game.management.entity.db.Withdraw;
import com.panda.game.management.entity.dbExt.RoomCardDetailInfo;
import com.panda.game.management.entity.dbExt.WithdrawDetailInfo;
import com.panda.game.management.exception.MsgException;
import com.panda.game.management.repository.RoomCardMapper;
import com.panda.game.management.repository.utils.ConditionUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RoomCardServiceImpl extends BaseServiceImpl<RoomCard> implements IRoomCardService {
    private final RoomCardMapper roomCardMapper;

    public RoomCardServiceImpl(RoomCardMapper roomCardMapper) {
        this.roomCardMapper = roomCardMapper;
    }


    /**
     * 分页加载数据列表 韦德 2018年8月27日00:20:54
     *
     * @param page
     * @param limit
     * @param condition
     * @param state
     * @param beginTime
     * @param endTime
     */
    @Override
    public List<RoomCardDetailInfo> getRoomCardLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime) {
        // 计算分页位置
        page = ConditionUtil.extractPageIndex(page, limit);
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        List<RoomCardDetailInfo> list = roomCardMapper.selectLimit(page, limit, state, beginTime, endTime, where);
        return list;
    }

    /**
     * 统计分页加载数据列表
     *
     * @param condition
     * @param state
     * @param beginTime
     * @param endTime
     */
    @Override
    public int getRoomCardLimitCount(String condition, Integer state, String beginTime, String endTime) {
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        return roomCardMapper.selectLimitCount(state, beginTime, endTime, where);
    }

    /**
     * 加载数据总条数 韦德 2018年8月27日00:21:16
     *
     * @return
     */
    @Override
    public int getRoomCardCount() {
        return roomCardMapper.selectCount(new RoomCard());
    }

    /**
     * 充值
     *
     * @param roomCard
     */
    @Override
    public void recharge(RoomCard roomCard) {
        RoomCard model = roomCardMapper.selectByPrimaryKey(roomCard.getCardId());
        if(model == null) throw new MsgException("记录不存在");
        model.setState(1);
        model.setUpdateTime(new Date());
        int count = roomCardMapper.updateByPrimaryKey(model);
        if(count == 0) throw new MsgException("操作失败");
    }

    /**
     * 拒绝
     *
     * @param roomCard
     */
    @Override
    public void pass(RoomCard roomCard) {
        RoomCard model = roomCardMapper.selectByPrimaryKey(roomCard.getCardId());
        if(model == null) throw new MsgException("记录不存在");
        model.setState(2);
        model.setUpdateTime(new Date());
        int count = roomCardMapper.updateByPrimaryKey(model);
        if(count == 0) throw new MsgException("操作失败");
    }

    /**
     * 提取分页条件
     * @return
     */
    private String extractLimitWhere(String condition, Integer state,  String beginTime, String endTime) {
        // 查询模糊条件
        String where = " 1=1";
        if(condition != null) {
            condition = condition.trim();
            where += " AND (" + ConditionUtil.like("card_id", condition, true, "t1");
            if (condition.split("-").length == 2){
                where += " OR " + ConditionUtil.like("add_time", condition, true, "t1");
                where += " OR " + ConditionUtil.like("update_time", condition, true, "t1");
            }
            where += " OR " + ConditionUtil.like("phone", condition, true, "t2");
            where += " OR " + ConditionUtil.like("panda_id", condition, true, "t1") + ")";
        }

        // 查询全部数据或者只有一类数据
        where = extractQueryAllOrOne(state, where);

        // 取两个日期之间或查询指定日期
        where = extractBetweenTime(beginTime, endTime, where);
        return where.trim();
    }


    /**
     * 提取两个日期之间的条件
     * @return
     */
    private String extractBetweenTime(String beginTime, String endTime, String where) {
        if ((beginTime != null && beginTime.contains("-")) &&
                endTime != null && endTime.contains("-")){
            where += " AND t1.add_time BETWEEN #{beginTime} AND #{endTime}";
        }else if (beginTime != null && beginTime.contains("-")){
            where += " AND t1.add_time BETWEEN #{beginTime} AND #{endTime}";
        }else if (endTime != null && endTime.contains("-")){
            where += " AND t1.add_time BETWEEN #{beginTime} AND #{endTime}";
        }
        return where;
    }


    /**
     * 查询全部数据或者只有一类数据
     * @return
     */
    private String extractQueryAllOrOne(Integer state, String where) {
        if (state != null && state != 0){
            where += " AND t1.state = #{state}";
        }
        return where;
    }
}
