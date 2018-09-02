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
import com.panda.game.management.entity.dbExt.SettlementDetailInfo;
import com.panda.game.management.exception.InfoException;
import com.panda.game.management.repository.SettlementMapper;
import com.panda.game.management.repository.utils.ConditionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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
    @Transactional
    public List<SettlementDetailInfo> getMemberList(Long roomCode) {
        return settlementMapper.selectDetailInfo(roomCode);
    }

    /**
     * 分页加载结算申请数据列表 韦德 2018年8月27日00:20:54
     * @return
     */
    @Override
    public List<SettlementDetailInfo> getSettlementLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime) {
        // 计算分页位置
        page = ConditionUtil.extractPageIndex(page, limit);
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        List<SettlementDetailInfo> list = settlementMapper.selectLimitGroup(page, limit, state, beginTime, endTime, where);
        return list;
    }

    /**
     * 统计分页加载结算申请数据列表
     *
     * @return
     */
    @Override
    public int getSettlementLimitCount(String condition, Integer state, String beginTime, String endTime) {
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        return settlementMapper.selectLimitCountGroup(state, beginTime, endTime, where);
    }

    /**
     * 加载结算申请数据总条数 韦德 2018年8月27日00:21:16
     *
     * @return
     */
    @Override
    public int getSettlementCount() {
        return settlementMapper.selectCountDistinct();
    }

    /**
     * 更新时间 韦德 2018年8月29日20:31:57
     *
     * @param userId
     * @param grade
     * @param roomCode
     */
    @Override
    @Transactional
    public void editGrade(Integer userId, Double grade, Long roomCode) {
        settlementMapper.updateGrade(userId, grade, roomCode);
    }

    /**
     * 更新房间内成员状态 韦德 2018年8月29日22:11:17
     *
     * @param roomCode
     * @param status
     * @return
     */
    @Override
    @Transactional
    public int updateStatusByRoomCode(Long roomCode, int status) {
        return settlementMapper.updateStatusByRoomCode(roomCode, status);
    }

    /**
     * 根据房间id和用户id查询成绩 韦德 2018年9月2日13:26:24
     *
     * @param userId
     * @param roomCode
     */
    @Override
    public Settlement getMemberGrade(Integer userId, Long roomCode) {
        Example example = new Example(Settlement.class);
        Example.Criteria criteria = example.createCriteria();
        Settlement settlement = new  Settlement();
        settlement.setUserId(userId);
        settlement.setRoomCode(roomCode);
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("roomCode", roomCode);
        Settlement result = settlementMapper.selectOneByExample(example);
        if(result == null) throw new InfoException("查询成绩失败");
        return result;
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
            where += " AND (" + ConditionUtil.like("settlement_id", condition, true, "t1");
            if (condition.split("-").length == 2){
                where += " OR " + ConditionUtil.like("add_time", condition, true, "t1");
                where += " OR " + ConditionUtil.like("update_time", condition, true, "t1");
            }
            where += " OR " + ConditionUtil.like("room_code", condition, true, "t1");
            where += " OR " + ConditionUtil.like("grade", condition, true, "t1");
            where += " OR " + ConditionUtil.like("remark", condition, true, "t1") + ")";
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
