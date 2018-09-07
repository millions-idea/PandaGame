/***
 * @pName management
 * @name SubareaServiceImpl
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.ISubareaService;
import com.panda.game.management.entity.db.Subareas;
import com.panda.game.management.exception.InfoException;
import com.panda.game.management.repository.SubareaMapper;
import com.panda.game.management.repository.utils.ConditionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubareaServiceImpl extends BaseServiceImpl<Subareas> implements ISubareaService {
    private final SubareaMapper subareaMapper;

    @Autowired
    public SubareaServiceImpl(SubareaMapper subareaMapper) {
        this.subareaMapper = subareaMapper;
    }


    /**
     * 获取等级分区列表 韦德 2018年8月18日12:24:50
     *
     * @return
     */
    @Override
    public List<Subareas> getLevelSubareas() {
        Subareas subareas = new Subareas();
        subareas.setIsRelation(1);
        return subareaMapper.select(subareas);
    }

    /**
     * 获取指定id关联的游戏分区 韦德 2018年8月18日17:12:17
     *
     * @param subareaId
     * @return
     */
    @Override
    public List<Subareas> getSubareas(Integer subareaId) {
        List<Subareas> subareas = subareaMapper.selectRelations(subareaId);
        return subareas.stream().filter(item -> item.getIsEnable() == 1).collect(Collectors.toList());
    }

    /**
     * 查询分区 韦德 2018年9月2日14:14:52
     *
     * @param areaId
     * @return
     */
    @Override
    public Subareas getSubarea(Integer areaId) {
        return subareaMapper.selectByPrimaryKey(areaId);
    }

    /**
     * 分页加载 韦德 2018年8月30日11:29:00
     *
     * @param page
     * @param limit
     * @param condition
     * @param state
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public List<Subareas> getLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime) {
        page = ConditionUtil.extractPageIndex(page, limit);
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        List<Subareas> list = subareaMapper.selectLimit(page, limit, state, beginTime, endTime, where);
        return list;
    }

    /**
     * 加载总记录数 韦德 2018年8月30日11:29:11
     *
     * @return
     */
    @Override
    public Integer getCount() {
        return subareaMapper.selectCount(new Subareas());
    }

    /**
     * 加载分页记录数 韦德 2018年8月30日11:29:22
     *
     * @param condition
     * @param state
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public Integer getLimitCount(String condition, Integer state, String beginTime, String endTime) {
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        return subareaMapper.selectLimitCount(state, beginTime, endTime, where);
    }

    /**
     * 修改状态 韦德 2018年9月4日09:12:34
     *
     * @param subareas
     */
    @Override
    public int update(Subareas subareas) {
        int count = subareaMapper.updateByPrimaryKey(subareas);
        if(count == 0) throw new InfoException("修改失败");
        return count;
    }


    /**
     * 提取分页条件
     * @return
     */
    private String extractLimitWhere(String condition, Integer isEnable,  String beginTime, String endTime) {
        // 查询模糊条件
        String where = " 1=1";
        if(condition != null) {
            condition = condition.trim();
            where += " AND (" + ConditionUtil.like("subarea_id", condition, true, "t1");
            if (condition.split("-").length == 2){
                where += " OR " + ConditionUtil.like("add_time", condition, true, "t1");
            }
            where += " OR " + ConditionUtil.like("name", condition, true, "t1") + ")";
        }

        // 查询全部数据或者只有一类数据
        // where = extractQueryAllOrOne(isEnable, where);

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


}
