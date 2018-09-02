/***
 * @pName management
 * @name UserServiceImpl
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.google.common.collect.ImmutableMap;
import com.panda.game.management.biz.IUserService;
import com.panda.game.management.entity.db.Recharge;
import com.panda.game.management.entity.db.Users;
import com.panda.game.management.entity.db.Wallets;
import com.panda.game.management.entity.dbExt.RechargeDetailInfo;
import com.panda.game.management.entity.dbExt.UserDetailInfo;
import com.panda.game.management.entity.resp.UserResp;
import com.panda.game.management.exception.FinanceException;
import com.panda.game.management.exception.InfoException;
import com.panda.game.management.exception.MsgException;
import com.panda.game.management.repository.PayMapper;
import com.panda.game.management.repository.UserMapper;
import com.panda.game.management.repository.WalletMapper;
import com.panda.game.management.repository.utils.ConditionUtil;
import com.panda.game.management.utils.MD5Util;
import com.panda.game.management.utils.PropertyUtil;
import com.panda.game.management.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends BaseServiceImpl<Users> implements IUserService {
    private final UserMapper userMapper;
    private final WalletMapper walletMapper;
    private final PayMapper payMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, WalletMapper walletMapper, PayMapper payMapper) {
        this.userMapper = userMapper;
        this.walletMapper = walletMapper;
        this.payMapper = payMapper;
    }

    /**
     * 插入数据 韦德 2018年8月13日13:27:48
     *
     * @param param
     * @return
     */
    @Override
    @Transactional
    public void register(Users param) {
        // 增加用户数据
        int count = 0;
        try{
            count = userMapper.insert(param);
        }catch (Exception e){
            if(e != null  && e.getCause() != null  && e.getMessage() != null  && e.getCause().getMessage().contains("Duplicate entry")){
                String msg = "对不起，您注册的账户已存在！";
                if(e.getCause().getMessage().contains("uq_panda")){
                    msg = "对不起，您输入的熊猫id已存在！";
                }
                throw new MsgException(msg);
            }
        }
        if(count == 0) throw new MsgException("注册用户失败");

        // 开通钱包
        Wallets wallets = new Wallets();
        wallets.setUserId(param.getUserId());
        wallets.setBalance(0D);
        wallets.setUpdateTime(new Date());
        wallets.setVersion(0);
        count = 0;
        count = walletMapper.insert(wallets);
        if(count == 0) throw new MsgException("开通钱包失败");
    }

    /**
     * 根据主键id查询用户信息 韦德 2018年8月27日11:17:42
     *
     * @param systemAccountsId
     * @return
     */
    @Override
    public UserResp getUserById(Integer systemAccountsId) {
        UserDetailInfo userInfo = userMapper.selectUserDetail(systemAccountsId.toString());
        UserResp userResp = new UserResp();
        PropertyUtil.clone(userInfo, userResp);
        return userResp;
    }

    /**
     * 根据用户名查询用户信息 韦德 2018年8月27日22:51:49
     *
     * @param username
     * @return
     */
    @Override
    public Users getUserByUserName(String username) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        Users users = new Users();
        users.setPhone(username);
        criteria.andEqualTo("phone", users.getPhone());
        List<Users> usersList = userMapper.selectByExample(example);
        if (users != null && usersList.size() > 0) {
            return usersList.get(0);
        }
        return null;
    }

    /**
     * 分页加载 韦德 2018年8月30日11:29:00
     *
     * @param page
     * @param limit
     * @param condition
     * @param state
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public List<UserDetailInfo> getLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime) {
        // 计算分页位置
        page = ConditionUtil.extractPageIndex(page, limit);
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        List<UserDetailInfo> list = userMapper.selectLimit(page, limit, state, beginTime, endTime, where);
        return list;
    }

    /**
     * 加载总记录数 韦德 2018年8月30日11:29:11
     *
     * @return
     */
    @Override
    public Integer getCount() {
        return userMapper.selectCount(new Users());
    }

    /**
     * 加载分页记录数 韦德 2018年8月30日11:29:22
     *
     * @param condition
     * @param state
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public Integer getLimitCount(String condition, Integer state, String beginTime, String endTime) {
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        return userMapper.selectLimitCount(state, beginTime, endTime, where);
    }

    /**
     * 更新密码 韦德 2018年9月1日00:23:54
     *
     * @param users
     */
    @Override
    public int updatePassword(Users users) {
        Users model = userMapper.selectByPrimaryKey(users);
        if(model == null) throw new MsgException("用户不存在");
        model.setPassword(MD5Util.md5(model.getPhone() + users.getPassword().trim()));
        model.setUpdateDate(new Date());
        int count = userMapper.updateByPrimaryKey(model);
        if(count == 0) throw new MsgException("更新失败");
        return count;
    }

    /**
     * 冻结用户 韦德 2018年9月1日00:28:07
     *
     * @param users
     */
    @Override
    public int updateEnable(Users users) {
        Users model = userMapper.selectByPrimaryKey(users);
        if(model == null) throw new MsgException("用户不存在");
        model.setUpdateDate(new Date());
        model.setIsEnable(users.getIsEnable());
        model.setIsDelete(users.getIsDelete());
        int count = userMapper.updateByPrimaryKey(model);
        if(count == 0) throw new MsgException("更新失败");
        return count;
    }

    /**
     * 登录验证并查询返回用户信息 韦德 2018年8月14日10:25:49
     *
     * @param user
     * @param smsCode
     * @return
     */
    @Override
    public UserResp loginAndQuery(Users user, String smsCode) {
        Users condition = new Users();
        condition.setPhone(user.getPhone());
        condition.setPassword(MD5Util.md5(user.getPhone() + user.getPassword()));
        condition.setIsDelete(0);
        condition.setIsEnable(1);
        Users userInfo = userMapper.selectOne(condition);

        if(userInfo == null) return null;
        if(userInfo.getIsEnable() == 0) throw new MsgException("您的账户已被管理员锁定");
        if(userInfo.getIsDelete() == 1) throw new MsgException("您的账户已被管理员回收");

        condition = new Users();
        condition.setUserId(userInfo.getUserId());
        condition.setIp(user.getIp());
        userMapper.updateOne(condition);

        Map<String, String> fields = ImmutableMap.of("phone", userInfo.getPhone(), "userId", userInfo.getUserId() + "", "timestamp", Instant.now().getEpochSecond() + "");
        String token = TokenUtil.create(fields);
        UserResp userResp = new UserResp();
        PropertyUtil.clone(userInfo, userResp);
        userResp.setToken(token);
        renewalToken(token , userInfo.getPhone(), userInfo.getUserId());
        return userResp;
    }

    /**
     * 登录验证 韦德 2018年8月26日17:18:58
     *
     * @param user
     * @return
     */
    @Override
    public Users login(Users user) {
        Users condition = new Users();
        condition.setPhone(user.getPhone());
        condition.setIsDelete(0);
        condition.setIsEnable(1);
        Users userInfo = userMapper.selectOne(condition);

        if(userInfo == null) return null;
        if(userInfo.getIsEnable() == 0) throw new MsgException("您的账户已被管理员锁定");
        if(userInfo.getIsDelete() == 1) throw new MsgException("您的账户已被管理员回收");

        condition = new Users();
        condition.setUserId(userInfo.getUserId());
        condition.setIp(user.getIp());
        userMapper.updateOne(condition);

        return userInfo;
    }

    /**
     * 令牌续期 韦德 2018年8月14日13:41:38
     * @param token
     * @param phone
     * @param userId
     */
    private void renewalToken(String token, String phone, Integer userId){
        String key  = "token:" + MD5Util.encrypt16(phone + userId);
        redisTemplate.opsForValue().set(key, token, 30, TimeUnit.DAYS);
    }

    /**
     * 根据令牌获取用户信息 韦德 2018年8月14日12:49:00
     *
     * @param token
     * @return
     */
    @Override
    public UserResp getUserByToken(String token) {
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) return null;

        String phone = map.get("phone");
        String userId = map.get("userId");

        Users condition = new Users();
        condition.setPhone(phone);
        condition.setUserId(Integer.parseInt(userId));
        Users userInfo = userMapper.selectOne(condition);

        UserResp userResp = new UserResp();
        PropertyUtil.clone(userInfo, userResp);
        userResp.setToken(token);

        String key  = "token:" + MD5Util.encrypt16(phone + userId);
        Long expire = redisTemplate.getExpire(key);
        if(expire <= 0) throw new InfoException("登录令牌失效");
        renewalToken(token, userInfo.getPhone(), userInfo.getUserId());
        return userResp;
    }

    /**
     * 登出操作 韦德 2018年8月14日13:36:20
     *
     * @param token
     */
    @Override
    public void logout(String token) {
        Map<String, String> map = TokenUtil.validate(token);
        if(!map.isEmpty()){
            String key  = "token:" + MD5Util.encrypt16(map.get("phone") + map.get("userId"));
            redisTemplate.delete(key);
        }
    }

    /**
     * 根据令牌获取用户详细信息 韦德 2018年8月16日00:08:26
     *
     * @param token
     * @return
     */
    @Override
    public UserResp getUserDetailByToken(String token) {
        // 根据token查询用户权限字段
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) return null;
        String phone = map.get("phone");
        String userId = map.get("userId");

        String key  = "token:" + MD5Util.encrypt16(phone + userId);
        Long expire = redisTemplate.getExpire(key);
        if(expire <= 0) throw new MsgException("登录令牌失效");

        // 查询基础信息
        UserDetailInfo userInfo = userMapper.selectUserDetail(userId);
        UserResp userResp = new UserResp();
        PropertyUtil.clone(userInfo, userResp);
        userResp.setToken(token);

        // 计算不可提现金额、可提现金额
        Double notWithdrawAmount = payMapper.selectNotWithdrawAmount(Integer.valueOf(userId),  "新用户注册奖励");
        Double withdrawAmount = payMapper.selectWithdrawAmount(Integer.valueOf(userId));
        if(withdrawAmount == null) withdrawAmount = 0D;
        userResp.setCanWithdrawAmount(withdrawAmount);
        userResp.setCanNotWithdrawAmount(notWithdrawAmount);
        return userResp;
    }

    /**
     * 修改密码 韦德 2018年8月17日00:35:30
     *
     * @param token
     * @param password
     * @param newPassword
     */
    @Override
    public boolean editPassword(String token, String password, String newPassword) {
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) return false;
        String phone = map.get("phone");
        Integer userId = Integer.valueOf(map.get("userId"));

        Users users = userMapper.selectByPrimaryKey(userId);
        if(users == null) throw new MsgException("用户不存在");

        if(!users.getPassword().equalsIgnoreCase(MD5Util.md5(phone + password))) throw new MsgException("密码不正确");

        users.setPassword(MD5Util.md5(phone + newPassword));
        int count = userMapper.updateByPrimaryKey(users);
        if(count == 0) throw new MsgException("修改失败");

        String key  = "token:" + MD5Util.encrypt16(phone + userId);
        Long expire = redisTemplate.getExpire(key);
        if(expire <= 0) throw new MsgException("登录令牌失效");
        return true;
    }

    /**
     * 删除数据 韦德 2018年8月13日13:28:16
     *
     * @param param
     * @return
     */
    @Override
    public int updateAvailability(Users param) {
        Users model = userMapper.selectByPrimaryKey(param);
        if(model == null) throw new MsgException("用户不存在");
        /*model.setIsDelete(1);
        model.setIsEnable(0);*/
        model.setUpdateDate(new Date());
        model.setIsEnable(param.getIsEnable());
        model.setIsDelete(param.getIsDelete());
        int count = userMapper.updateByPrimaryKey(model);
        if(count == 0) throw new MsgException("更新失败");
        return count;
    }

    @Override
    @Transactional
    public int changeBalance(String username, Double amount) {
        Users user = this.getUserByUserName(username);
        if(user == null) throw new MsgException("用户不存在");
        user.setUpdateDate(new Date());
        userMapper.updateByPrimaryKey(user);
        Wallets wallets = walletMapper.selectByUid(user.getUserId());
        if(wallets == null) throw new MsgException("查询钱包失败");
        wallets.setBalance(wallets.getBalance() + amount);
        wallets.setUpdateTime(new Date());
        int count = walletMapper.updateByPrimaryKey(wallets);
        if(count == 0) throw new MsgException("更新失败");
        return count;
    }

    /**
     * 绑定财务账户 韦德 2018年9月2日00:57:46
     *
     * @param token
     * @param account
     */
    @Override
    public void bindFinanceAccount(String token, String account) {
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) throw new MsgException("请重新登录");
        Integer userId = Integer.valueOf(map.get("userId"));

        Users users = userMapper.selectByPrimaryKey(userId);
        if(users == null) throw new MsgException("用户不存在");

        users.setFinanceId(account);
        users.setUpdateDate(new Date());

        int count = userMapper.updateByPrimaryKey(users);
        if(count == 0) throw  new MsgException("绑定失败");
    }


    /**
     * 提取分页条件
     * @return
     */
    private String extractLimitWhere(String condition, Integer isEnable,  String beginTime, String endTime) {
        // 查询模糊条件
        String where = " 1=1";
        if(condition != null) {
            condition = condition.trim();
            where += " AND (" + ConditionUtil.like("user_id", condition, true, "t1");
            if (condition.split("-").length == 2){
                where += " OR " + ConditionUtil.like("add_time", condition, true, "t1");
                where += " OR " + ConditionUtil.like("update_time", condition, true, "t1");
            }
            where += " OR " + ConditionUtil.like("phone", condition, true, "t1");
            where += " OR " + ConditionUtil.like("panda_id", condition, true, "t1") + ")";
        }

        // 查询全部数据或者只有一类数据
        // where = extractQueryAllOrOne(isEnable, where);

        // 取两个日期之间或查询指定日期
        where = extractBetweenTime(beginTime, endTime, where);
        return where.trim();
    }


    /**
     * 提取两个日期之间的条件
     * @return
     */
    private String extractBetweenTime(String beginTime, String endTime, String where) {
        if ((beginTime != null && beginTime.contains("-")) &&
                endTime != null && endTime.contains("-")){
            where += " AND t1.add_date BETWEEN #{beginTime} AND #{endTime}";
        }else if (beginTime != null && beginTime.contains("-")){
            where += " AND t1.add_date BETWEEN #{beginTime} AND #{endTime}";
        }else if (endTime != null && endTime.contains("-")){
            where += " AND t1.add_date BETWEEN #{beginTime} AND #{endTime}";
        }
        return where;
    }


    /**
     * 查询全部数据或者只有一类数据
     * @return
     */
    private String extractQueryAllOrOne(Integer isEnable, String where) {
        if (isEnable != null && isEnable != 0){
            where += " AND t1.is_enable = #{isEnable}";
        }
        return where;
    }
}
