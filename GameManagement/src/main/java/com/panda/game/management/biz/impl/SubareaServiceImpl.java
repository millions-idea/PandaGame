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
import com.panda.game.management.repository.SubareaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return subareaMapper.selectRelations(subareaId);
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

}
