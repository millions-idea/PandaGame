/***
 * @pName management
 * @name UserServiceImpl
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.BaseService;
import com.panda.game.management.biz.UserService;
import com.panda.game.management.entity.db.Users;
import com.panda.game.management.exception.InfoException;
import com.panda.game.management.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<Users> implements UserService  {
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 插入数据 韦德 2018年8月13日13:27:48
     *
     * @param param
     * @return
     */
    @Override
    public int insert(Users param) {
        int result = userMapper.insert(param);
        if(result == 0) throw new InfoException("插入用户数据失败");
        return result;
    }
}
