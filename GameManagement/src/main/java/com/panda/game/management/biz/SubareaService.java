/***
 * @pName management
 * @name SubaeraService
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.entity.db.Subareas;
import com.panda.game.management.entity.resp.GroupInformation;

import java.util.List;

public interface SubareaService extends BaseService<Subareas> {
    /**
     * 获取等级分区列表 韦德 2018年8月18日12:24:50
     * @return
     */
    List<Subareas> getLevelSubareas();

}
