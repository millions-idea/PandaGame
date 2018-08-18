/***
 * @pName management
 * @name SubareaMapper
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.Subareas;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubareaMapper extends MyMapper<Subareas> {
}
