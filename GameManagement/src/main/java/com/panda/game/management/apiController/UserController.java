/***
 * @pName management
 * @name UserController
 * @user HongWei
 * @date 2018/8/16
 * @desc
 */
package com.panda.game.management.apiController;

import com.fasterxml.jackson.annotation.JsonView;
import com.panda.game.management.biz.UserService;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.resp.UserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getUserInfo")
    @JsonView(UserResp.FinanceView.class)
    /**
     * 查询用户详情  韦德 2018年8月17日00:34:37
     * @param token
     * @return
     */
    public JsonResult<UserResp> getUserInfo(String token){
        UserResp userResp = userService.getUserDetailByToken(token);
        return new JsonResult<>().successful(userResp);
    }


    @PostMapping("/editPassword")
    /**
     * 修改密码 韦德 2018年8月17日00:34:25
     * @param token
     * @param password
     * @param newPassword
     * @return
     */
    public JsonResult editPassword(String token, String password, String newPassword){
        userService.editPassword(token, password, newPassword);
        return new JsonResult<>().successful();
    }
}
