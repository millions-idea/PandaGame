/***
 * @pName management
 * @name UserService
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.entity.db.Users;
import com.panda.game.management.entity.dbExt.RechargeDetailInfo;
import com.panda.game.management.entity.dbExt.UserDetailInfo;
import com.panda.game.management.entity.resp.UserResp;

import java.util.List;

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

    /**
     * 根据主键id查询用户信息 韦德 2018年8月27日11:17:42
     * @param systemAccountsId
     * @return
     */
    UserResp getUserById(Integer systemAccountsId);

    /**
     * 根据用户名查询用户信息 韦德 2018年8月27日22:51:49
     * @param username
     * @return
     */
    Users getUserByUserName(String username);


    /**
     * 分页加载 韦德 2018年8月30日11:29:00
     * @param page
     * @param limit
     * @param condition
     * @param state
     * @param beginTime
     * @param endTime
     * @return
     */
    List<UserDetailInfo> getLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime);

    /**
     * 加载总记录数 韦德 2018年8月30日11:29:11
     * @return
     */
    Integer getCount();

    /**
     * 加载分页记录数 韦德 2018年8月30日11:29:22
     * @param condition
     * @param state
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer getLimitCount(String condition, Integer state, String beginTime, String endTime);


    /**
     * 更新密码 韦德 2018年9月1日00:23:54
     * @param users
     */
    int updatePassword(Users users);

    /**
     * 冻结用户 韦德 2018年9月1日00:28:07
     * @param users
     */
    int updateEnable(Users users);


    int updateAvailability(Users users);

    int changeBalance(String username, Double amount);

    /**
     * 绑定财务账户 韦德 2018年9月2日00:57:46
     * @param token
     * @param account
     * @param accountName
     */
    void bindFinanceAccount(String token, String account,String accountName);
}
