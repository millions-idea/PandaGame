/***
 * @pName management
 * @name UserMapper
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.Users;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends MyMapper<Users> {
}
