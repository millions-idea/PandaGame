/***
 * @pName management
 * @name ExceptionMapper
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.Exceptions;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExceptionMapper extends MyMapper<Exceptions> {

}
