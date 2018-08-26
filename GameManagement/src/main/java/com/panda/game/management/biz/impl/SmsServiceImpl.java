/***
 * @pName management
 * @name SmServiceImpl
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.ISmsService;
import com.panda.game.management.entity.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements ISmsService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 给指定手机号发送短信
     *
     * @param phone
     * @param body
     */
    @Override
    public void sendMessage(String phone, String body) {
        redisTemplate.opsForValue().set("short:phone:sms", body, 5, TimeUnit.MINUTES);
    }

    /**
     * 获取最新的手机短信
     *
     * @param phone
     * @return
     */
    @Override
    public String getCurrentMessage(String phone) {
        Object sms = redisTemplate.opsForValue().get("short:phone:sms");
        if(sms == null) return null;
        return String.valueOf(sms);
    }

    /**
     * 加入回收队列
     *
     * @param phone
     */
    @Override
    public void setRecycle(String phone) {
        redisTemplate.opsForValue().set("short:phone:sms", Constant.RECYCLE, 5, TimeUnit.MINUTES);
    }

    /**
     * 是否超过日限
     *
     * @param phone
     * @return
     */
    @Override
    public Boolean isOutMaxRange(String phone) {
        Object obj = redisTemplate.opsForValue().get("short:phone:smsPool:" + phone);
        if(obj == null) return false;
        Integer count = Integer.valueOf(String.valueOf(obj));
        if(count >= 5) return true;
        count += 1;
        redisTemplate.opsForValue().set("short:phone:smsPool:" + phone, count, 1, TimeUnit.DAYS);
    return false;
    }
}
