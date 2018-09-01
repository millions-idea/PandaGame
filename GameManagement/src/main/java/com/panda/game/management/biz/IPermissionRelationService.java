/***
 * @pName management
 * @name IPermissionService
 * @user HongWei
 * @date 2018/9/1
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.entity.db.Permission;
import com.panda.game.management.entity.db.PermissionRelation;

import java.util.List;

public interface IPermissionRelationService extends IBaseService<PermissionRelation> {
    /**
     * 通过用户id查询权限关系列表 韦德 2018年9月1日02:12:32
     * @param userId
     * @return
     */
    List<PermissionRelation> getListByUid(Integer userId);

    /**
     * 新增或替换 韦德 2018年9月1日02:50:13
     * @param userId
     * @param roleName
     */
    int replace(Integer userId, String roleName);
}
