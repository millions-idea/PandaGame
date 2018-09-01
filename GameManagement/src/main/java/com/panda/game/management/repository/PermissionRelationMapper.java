/***
 * @pName management
 * @name PermissionMapper
 * @user HongWei
 * @date 2018/9/1
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.Permission;
import com.panda.game.management.entity.db.PermissionRelation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PermissionRelationMapper extends MyMapper<PermissionRelation> {

    @Insert("REPLACE INTO tb_permission_relation(permission_id, user_id, permission_role) VALUES(" +
                "#{permissionId}, #{userId}, #{role})")
    /**
     * 新增或替换 韦德 2018年9月1日02:51:33
     * @param userId
     * @param permissionId
     * @param role
     * @return
     */
    int replace(@Param("userId") Integer userId, @Param("permissionId") Integer permissionId, @Param("role") String role);

    @Delete("DELETE FROM tb_permission_relation WHERE user_id = #{userId}")
    int deleteIn(@Param("userId") Integer userId);
}
