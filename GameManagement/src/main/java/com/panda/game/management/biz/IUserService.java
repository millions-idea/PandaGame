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
public interface IUserService extends IBaseService<Users> {

    /**
     * 登录验证并查询返回用户信息 韦德 2018年8月14日10:25:49
     * @param user
     * @param smsCode
     * @return
     */
    UserResp loginAndQuery(Users user, String smsCode);


    /**
     * 登录验证 韦德 2018年8月26日17:18:58
     * @param user
     * @return
     */
    Users login(Users user);

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

    /**
     * 根据令牌获取用户详细信息 韦德 2018年8月16日00:08:26
     * @param token
     * @return
     */
    UserResp getUserDetailByToken(String token);

    /**
     * 修改密码 韦德 2018年8月17日00:35:30
     * @param token
     * @param password
     * @param newPassword
     */
    boolean editPassword(String token, String password, String newPassword);


    /**
     * 插入数据 韦德 2018年8月20日15:40:56
     * @param param
     */
    void register(Users param);
}
