/***
 * @pName management
 * @name BootstrapController
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.apiController;

import com.fasterxml.jackson.annotation.JsonView;
import com.panda.game.management.biz.IBadBoyService;
import com.panda.game.management.biz.IMessageService;
import com.panda.game.management.biz.ISmsService;
import com.panda.game.management.biz.IUserService;
import com.panda.game.management.entity.DataDictionary;
import com.panda.game.management.entity.JsonArrayResult;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.db.Dictionary;
import com.panda.game.management.entity.db.Messages;
import com.panda.game.management.entity.db.Users;
import com.panda.game.management.entity.resp.UserResp;
import com.panda.game.management.entity.resp.VersionResp;
import com.panda.game.management.facade.UserFacadeService;
import com.panda.game.management.utils.MD5Util;
import com.panda.game.management.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/bootstrap")
public class BootstrapApiController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IBadBoyService IBadBoyService;
    @Autowired
    private ISmsService ISmsService;
    @Autowired
    private UserFacadeService userFacadeService;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping("/register")
    public JsonResult register(HttpServletRequest request, Users user, String smsCode){
        //if(user.getPandaId().isEmpty()) return new JsonResult().normalExceptionAsString("熊猫麻将ID是必填项");

        // 1、检查是否在黑名单中
        String userIp = RequestUtil.getIp(request);
        //IBadBoyService.isRegisterBlackList(userIp);

        // 2、校验手机短信
        /*String currentMessage = smsService.getCurrentMessage(user.getPhone());
        if(currentMessage == null || currentMessage == Constant.RECYCLE) return new JsonResult().normalExceptionAsString("验证码无效");
        if(!currentMessage.equalsIgnoreCase(smsCode)) return new JsonResult().normalExceptionAsString("验证码错误");
        smsService.setRecycle(user.getPhone());*/

        // 3、添加用户数据
        user.setIp(userIp);
        user.setPassword(MD5Util.md5(user.getPhone() + user.getPassword()));
        user.setIsEnable(1);
        user.setIsDelete(0);
        user.setAddDate(new Date());
        user.setUpdateDate(new Date());
        userFacadeService.register(user);
        //IBadBoyService.addToRegisterBlackList(userIp);

        return JsonResult.successful();
    }

    @GetMapping("/getSmsCode")
    public JsonResult getSmsCode(HttpServletRequest request, String phone){
        if(phone.length() < 11) return new JsonResult().normalExceptionAsString("手机号最短需要11位数字");

        // 1、检查是否在黑名单中
        String userIp = RequestUtil.getIp(request);
        IBadBoyService.isRegisterBlackList(userIp);

        // 2、是否超过日限
        if( ISmsService.isOutMaxRange(phone)) return new JsonResult().normalExceptionAsString("操作频繁");

        // 3、发送手机短信验证码
        ISmsService.sendMessage(phone, "1234");
        return new JsonResult(200, "1234");
    }

    @PostMapping("/login")
    @JsonView(UserResp.SecurityView.class)
    public JsonResult<UserResp> login(HttpServletRequest request,Users user, String smsCode){
        String userIp = RequestUtil.getIp(request);
        user.setIp(userIp);
        UserResp userResp = userService.loginAndQuery(user, smsCode);
        if(userResp == null) return new JsonResult().normalExceptionAsString("登录失败");
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

    /**
     * 查询短消息列表 韦德 2018年8月29日23:56:01
     * @param token
     * @return
     */
    @GetMapping("/getMessageList")
    @Transactional
    public JsonArrayResult<Messages> getMessageList(String token){
        List<Messages> list = messageService.getMessageList(token);
        messageService.batchMarkRead(list);
        return new JsonArrayResult<>(list);
    }

    /**
     * 查询短消息列表 韦德 2018年8月29日23:56:01
     * @param token
     * @return
     */
    @GetMapping("/getMessageNotification")
    public JsonArrayResult<Messages> getMessageNotification(String token){
        List<Messages> list = messageService.getMessageList(token);
        return new JsonArrayResult<>(list);
    }



    @GetMapping("/version")
    public VersionResp version(){
        Dictionary version = DataDictionary.DATA_DICTIONARY.get("version");
        String value = version.getValue();
        return new VersionResp(value.contains("U") ? 1 : 0, value.replace("U","").trim());
    }

    @GetMapping("/cache")
    public Object cache(){
        redisTemplate.opsForValue().set("init", "hello redis");
        return redisTemplate.opsForValue().get("init");
    }
}
