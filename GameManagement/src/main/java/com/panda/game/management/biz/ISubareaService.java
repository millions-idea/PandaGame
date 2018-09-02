/***
 * @pName management
 * @name SubaeraService
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.entity.db.Subareas;

import java.util.List;

public interface ISubareaService extends IBaseService<Subareas> {
    /**
     * 获取等级分区列表 韦德 2018年8月18日12:24:50
     * @return
     */
    List<Subareas> getLevelSubareas();

    /**
     * 获取指定id关联的游戏分区 韦德 2018年8月18日17:12:17
     * @param subareaId
     * @return
     */
    List<Subareas> getSubareas(Integer subareaId);

    /**
     * 查询分区 韦德 2018年9月2日14:14:52
     * @param areaId
     * @return
     */
    Subareas getSubarea(Integer areaId);
}
