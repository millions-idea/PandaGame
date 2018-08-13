/***
 * @pName management
 * @name BaseService
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.biz;

import java.util.List;

public interface BaseService<T> {
    /**
     * 获取全部数据 韦德 2018年8月13日13:26:57
     * @return
     */
    List<T> getList();

    /**
     * 分页获取全部数据 韦德 2018年8月13日13:27:17
     * @param page
     * @param limit
     * @param condition
     * @return
     */
    List<T> getLimit(Integer page, Integer limit, String condition);


    /**
     * 插入数据 韦德 2018年8月13日13:27:48
     * @param param
     * @return
     */
    int insert(T param);

    /**
     * 更新数据 韦德 2018年8月13日13:28:01
     * @param param
     * @return
     */
    int update(T param);

    /**
     * 删除数据 韦德 2018年8月13日13:28:16
     * @param param
     * @return
     */
    int delete(T param);
}
