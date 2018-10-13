/***
 * @pName management
 * @name SubaeraService
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.entity.db.Subareas;
import com.panda.game.management.entity.dbExt.UserDetailInfo;

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


    /**
     * 分页加载 韦德 2018年8月30日11:29:00
     * @param page
     * @param limit
     * @param condition
     * @param state
     * @param beginTime
     * @param endTime
     * @return
     */
    List<Subareas> getLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime);

    /**
     * 加载总记录数 韦德 2018年8月30日11:29:11
     * @return
     */
    Integer getCount();

    /**
     * 加载分页记录数 韦德 2018年8月30日11:29:22
     * @param condition
     * @param state
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer getLimitCount(String condition, Integer state, String beginTime, String endTime);

    /**
     * 修改 韦德 2018年9月4日09:12:34
     * @param subareas
     */
    int update(Subareas subareas);

    /**
     * 通过房间号查询分区信息 韦德 2018年10月13日14:57:18
     * @param roomCode
     * @return
     */
    Subareas getSubareaByRoomId(Long roomCode);
}
