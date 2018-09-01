/***
 * @pName management
 * @name BaseServiceImpl
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.IBaseService;

import java.util.List;

public abstract class BaseServiceImpl<T> implements IBaseService<T> {
    /**
     * 获取数据 韦德 2018年8月13日13:26:57
     *
     * @param param
     * @return
     */
    @Override
    public T get(T param) {
        return null;
    }

    /**
     * 获取全部数据 韦德 2018年8月13日13:26:57
     *
     * @return
     */
    @Override
    public List<T> getList() {
        return null;
    }

    /**
     * 分页获取全部数据 韦德 2018年8月13日13:27:17
     *
     * @param page
     * @param limit
     * @param condition
     * @return
     */
    @Override
    public List<T> getLimit(Integer page, Integer limit, String condition) {
        return null;
    }

    /**
     * 插入数据 韦德 2018年8月13日13:27:48
     *
     * @param param
     * @return
     */
    @Override
    public int insert(T param) {
        return 0;
    }

    /**
     * 更新数据 韦德 2018年8月13日13:28:01
     *
     * @param param
     * @return
     */
    @Override
    public int update(T param) {
        return 0;
    }

    /**
     * 删除数据 韦德 2018年8月13日13:28:16
     *
     * @param param
     * @return
     */
    @Override
    public int delete(T param) {
        return 0;
    }
}
