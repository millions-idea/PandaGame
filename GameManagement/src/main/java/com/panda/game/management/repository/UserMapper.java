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
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends MyMapper<Users> {

    @Update("UPDATE tb_users SET update_date=NOW(), ip=#{ip} WHERE user_id = #{userId} AND is_delete = 0 AND is_enable = 1")
    /**
     * 更新用户登录信息 韦德 2018年8月14日10:55:59
     * @param condition
     * @return
     */
    int updateOne(Users condition);





}
