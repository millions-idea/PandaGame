/***
 * @pName management
 * @name BadBoyService
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.biz;

public interface IBadBoyService {

    /**
     * 检查IP地址是否在注册黑名单中
     * @param ip
     * @return
     */
    void isRegisterBlackList(String ip);

    /**
     * 添加到注册黑名单中
     * @param ip
     */
    void addToRegisterBlackList(String ip);
}
