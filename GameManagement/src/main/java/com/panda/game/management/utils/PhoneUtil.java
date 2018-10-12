/***
 * @pName GameManagement
 * @name PhoneUtil
 * @user HongWei
 * @date 2018/10/12
 * @desc
 */
package com.panda.game.management.utils;

public class PhoneUtil {

    /**
     * 获取加密处理后的手机号
     * @param phone
     * @return
     */
    public static String getEncrypt(String phone){
        String encryptPhoneBefore = phone.substring(0,3);
        String encryptPhoneAfter = phone.substring(9,11);
        String encryptPhone = StringUtil.padLeft(encryptPhoneBefore, 9, '*').concat(encryptPhoneAfter);
        return encryptPhone;
    }
}
