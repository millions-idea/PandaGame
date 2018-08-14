/***
 * @pName management
 * @name BootstrapController
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.apiController;

import com.fasterxml.jackson.annotation.JsonView;
import com.panda.game.management.biz.BadBoyService;
import com.panda.game.management.biz.SmsService;
import com.panda.game.management.biz.UserService;
import com.panda.game.management.entity.Constant;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.db.Users;
import com.panda.game.management.entity.resp.UserResp;
import com.panda.game.management.utils.MD5Util;
import com.panda.game.management.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/api/bootstrap")
public class BootstrapController {
    @Autowired
    private UserService userService;
    @Autowired
    private BadBoyService badBoyService;
    @Autowired
    private SmsService smsService;

    @PostMapping("/register")
    public JsonResult register(HttpServletRequest request, Users user, String smsCode){
        // 1、检查是否在黑名单中
        String userIp = RequestUtil.getIp(request);
        badBoyService.isRegisterBlackList(userIp);

        // 2、校验手机短信
        String currentMessage = smsService.getCurrentMessage(user.getPhone());
        if(currentMessage == null || currentMessage == Constant.RECYCLE) return new JsonResult().exceptionAsString("验证码无效");
        if(!currentMessage.equalsIgnoreCase(smsCode)) return new JsonResult().exceptionAsString("验证码错误");
        smsService.setRecycle(user.getPhone());

        // 3、添加用户数据
        user.setIp(userIp);
        user.setPassword(MD5Util.md5(user.getPhone() + user.getPassword()));
        user.setIsEnable(1);
        user.setIsDelete(0);
        user.setUpdateDate(new Date());
        userService.insert(user);
        badBoyService.addToRegisterBlackList(userIp);

        return JsonResult.successful();
    }

    @GetMapping("/getSmsCode")
    public JsonResult getSmsCode(HttpServletRequest request, String phone){
        if(phone.length() < 11) return new JsonResult().exceptionAsString("手机号最短需要11位数字");

        // 1、检查是否在黑名单中
        String userIp = RequestUtil.getIp(request);
        badBoyService.isRegisterBlackList(userIp);

        // 2、是否超过日限
        if( smsService.isOutMaxRange(phone)) return new JsonResult().exceptionAsString("操作频繁");

        // 3、发送手机短信验证码
        smsService.sendMessage(phone, "1234");
        return new JsonResult(200, "1234");
    }

    @PostMapping("/login")
    @JsonView(UserResp.SecurityView.class)
    public JsonResult<UserResp> login(HttpServletRequest request,Users user, String smsCode){
        String userIp = RequestUtil.getIp(request);
        user.setIp(userIp);
        UserResp userResp = userService.loginAndQuery(user, smsCode);
        if(userResp == null) return new JsonResult().exceptionAsString("登录失败");
        return new JsonResult<UserResp>().successful(userResp);
    }

    @GetMapping("/getUserInfo")
    @JsonView(UserResp.SecurityView.class)
    public JsonResult<UserResp> getUserInfo(String token){
        UserResp userResp = userService.getUserByToken(token);
        return new JsonResult<>().successful(userResp);
    }

    @GetMapping("/logout")
    @JsonView(UserResp.SecurityView.class)
    public JsonResult logout(String token){
        userService.logout(token);
        return JsonResult.successful();
    }
}
