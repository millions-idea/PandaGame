/***
 * @pName management
 * @name SmsService
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.biz;

public interface SmsService {
    /**
     * 给指定手机号发送短信
     * @param phone
     * @param body
     */
    void sendMessage(String phone, String body);

    /**
     * 获取最新的手机短信
     * @param phone
     * @return
     */
    String getCurrentMessage(String phone);

    /**
     * 加入回收队列
     * @param phone
     */
    void setRecycle(String phone);

    /**
     * 是否超过日限
     * @param phone
     * @return
     */
    Boolean isOutMaxRange(String phone);
}
