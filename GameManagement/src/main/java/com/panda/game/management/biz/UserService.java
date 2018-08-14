/***
 * @pName management
 * @name UserService
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.entity.db.Users;
import com.panda.game.management.entity.resp.UserResp;

/**
 * 用户逻辑接口 韦德 2018年8月13日13:58:10
 */
public interface UserService extends BaseService<Users> {

    /**
     * 登录验证并查询返回用户信息 韦德 2018年8月14日10:25:49
     * @param user
     * @param smsCode
     * @return
     */
    UserResp loginAndQuery(Users user, String smsCode);

    /**
     * 根据令牌获取用户信息 韦德 2018年8月14日12:49:00
     * @param token
     * @return
     */
    UserResp getUserByToken(String token);

    /**
     * 登出操作 韦德 2018年8月14日13:36:20
     * @param token
     */
    void logout(String token);
}
