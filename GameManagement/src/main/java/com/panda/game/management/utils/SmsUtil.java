/***
 * @pName management
 * @name SmsUtil
 * @user HongWei
 * @date 2018/8/14
 * @desc
 */
package com.panda.game.management.utils;

public class SmsUtil {
    interface Config {
        // 短信应用SDK AppID
        int appid = 1400009099; // 1400开头

        // 短信应用SDK AppKey
        String appkey = "9ff91d87c2cd7cd0ea762f141975d1df37481d48700d70ac37470aefc60f9bad";

        // 需要发送短信的手机号码
        String[] phoneNumbers = {"21212313123", "12345678902", "12345678903"};

        // 短信模板ID，需要在短信应用中申请
        int templateId = 7839; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请

        // 签名
        String smsSign = "腾讯云"; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`
    }

}
