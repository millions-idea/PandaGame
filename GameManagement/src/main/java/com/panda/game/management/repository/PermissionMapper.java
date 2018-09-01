/***
 * @pName management
 * @name PermissionMapper
 * @user HongWei
 * @date 2018/9/1
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper extends MyMapper<Permission> {
    @Select("SELECT * FROM tb_permission WHERE permission_name=#{roleName}")
    List<Permission> selectByName(@Param("roleName") String roleName);
}
