/***
 * @pName management
 * @name SubareaServiceImpl
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.SubareaService;
import com.panda.game.management.entity.db.Subareas;
import com.panda.game.management.entity.resp.GroupInformation;
import com.panda.game.management.repository.SubareaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubareaServiceImpl extends BaseServiceImpl<Subareas> implements SubareaService {
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

}
