/***
 * @pName management
 * @name UserServiceImpl
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.google.common.collect.ImmutableMap;
import com.panda.game.management.biz.UserService;
import com.panda.game.management.entity.db.Users;
import com.panda.game.management.entity.resp.UserResp;
import com.panda.game.management.exception.InfoException;
import com.panda.game.management.repository.UserMapper;
import com.panda.game.management.utils.MD5Util;
import com.panda.game.management.utils.PropertyUtil;
import com.panda.game.management.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends BaseServiceImpl<Users> implements UserService  {
    private final UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

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
        if(userInfo.getIsEnable() == 0) throw new InfoException("您的账户已被管理员锁定");
        if(userInfo.getIsDelete() == 1) throw new InfoException("您的账户已被管理员回收");

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
}
