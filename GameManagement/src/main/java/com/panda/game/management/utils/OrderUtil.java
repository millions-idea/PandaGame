/***
 * @pName management
 * @name OrderUtil
 * @user HongWei
 * @date 2018/9/5
 * @desc
 */
package com.panda.game.management.utils;

import java.util.Calendar;
import java.util.Date;

public class OrderUtil{
    public static String get(){
        Calendar c = Calendar.getInstance();
        Date vNow = new Date();
        String sNow = "";
        sNow += c.get(Calendar.YEAR);
        sNow += c.get(Calendar.MONTH) + 1;
        sNow += c.get(Calendar.DATE);
        sNow += c.get(Calendar.HOUR);
        sNow += c.get(Calendar.MINUTE);
        sNow += c.get(Calendar.SECOND);
        sNow += c.get(Calendar.MILLISECOND);
        return  sNow;
    }
}