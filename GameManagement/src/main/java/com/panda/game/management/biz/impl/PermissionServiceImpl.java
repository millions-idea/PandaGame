/***
 * @pName management
 * @name PermissionServiceImpl
 * @user HongWei
 * @date 2018/9/1
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.IPermissionService;
import com.panda.game.management.entity.db.Permission;
import com.panda.game.management.repository.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements IPermissionService {
    private final PermissionMapper permissionMapper;

    @Autowired
    public PermissionServiceImpl(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    /**
     * 获取全部数据 韦德 2018年8月13日13:26:57
     *
     * @return
     */
    @Override
    public List<Permission> getList() {
        return permissionMapper.selectAll();
    }
}
