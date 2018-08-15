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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getUserInfo")
    @JsonView(UserResp.FinanceView.class)
    public JsonResult<UserResp> getUserInfo(String token){
        UserResp userResp = userService.getUserDetailByToken(token);
        return new JsonResult<>().successful(userResp);
    }
}
