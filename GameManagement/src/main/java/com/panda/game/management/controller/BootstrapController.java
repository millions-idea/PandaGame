/***
 * @pName management
 * @name BootstrapController
 * @user HongWei
 * @date 2018/8/26
 * @desc
 */
package com.panda.game.management.controller;

import com.panda.game.management.biz.IUserService;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.db.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/management/bootstrap")
public class BootstrapController {

    @GetMapping("/login")
    public String login(){
        return "bootstrap/index";
    }
}
