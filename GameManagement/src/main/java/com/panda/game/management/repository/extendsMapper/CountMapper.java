/***
 * @pName management
 * @name CountMapper
 * @user HongWei
 * @date 2018/8/27
 * @desc
 */
package com.panda.game.management.repository.extendsMapper;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.provider.base.BaseSelectProvider;

@RegisterMapper
public interface CountMapper<T> {
    /**
     * 根据实体中的属性查询总数，无查询条件
     * @param record
     * @return
     */
    @SelectProvider(type = ExtendsSelectProvider.class, method = "dynamicSQL")
    int selectCount(T record);
}
