/***
 * @pName management
 * @name PermissionServiceImpl
 * @user HongWei
 * @date 2018/9/1
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.IPermissionRelationService;
import com.panda.game.management.entity.db.Permission;
import com.panda.game.management.entity.db.PermissionRelation;
import com.panda.game.management.exception.MsgException;
import com.panda.game.management.repository.PermissionMapper;
import com.panda.game.management.repository.PermissionRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class PermissionRelationServiceImpl extends BaseServiceImpl<PermissionRelation> implements IPermissionRelationService {
    private final PermissionRelationMapper permissionRelationMapper;
    private final PermissionMapper permissionMapper;

    @Autowired
    public PermissionRelationServiceImpl(PermissionRelationMapper permissionRelationMapper, PermissionMapper permissionMapper) {
        this.permissionRelationMapper = permissionRelationMapper;
        this.permissionMapper = permissionMapper;
    }

    /**
     * 通过用户id查询权限关系列表 韦德 2018年9月1日02:12:32
     *
     * @param userId
     * @return
     */
    @Override
    public List<PermissionRelation> getListByUid(Integer userId) {
        Example example = new Example(PermissionRelation.class);
        Example.Criteria criteria = example.createCriteria();
        PermissionRelation permissionRelation = new PermissionRelation();
        permissionRelation.setUserId(userId);
        criteria.andEqualTo("userId", permissionRelation.getUserId());
        return  permissionRelationMapper.selectByExample(example);
    }

    /**
     * 新增或替换 韦德 2018年9月1日02:50:13
     *
     * @param userId
     * @param roleName
     */
    @Override
    @Transactional
    public int replace(Integer userId, String roleName) {

        Example example = new Example(PermissionRelation.class);
        Example.Criteria criteria = example.createCriteria();
        PermissionRelation permissionRelation = new PermissionRelation();
        permissionRelation.setUserId(userId);
        criteria.andEqualTo("userId", permissionRelation.getUserId());
        List<PermissionRelation>  relationList = permissionRelationMapper.selectByExample(example);
        if(relationList != null){
            permissionRelationMapper.deleteIn(userId);
        }

        List<Permission> permissions = permissionMapper.selectByName(roleName);
        permissions.forEach(permission -> {
            int count =  permissionRelationMapper.replace(userId, permission.getPermissionId(), "ROLE_" + roleName);
            if(count == 0) throw new MsgException("新增或替换失败");
        });
        return 1;
    }

}
