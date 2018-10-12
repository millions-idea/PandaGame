/***
 * @pName GameManagement
 * @name InviteManageController
 * @user HongWei
 * @date 2018/10/12
 * @desc
 */
package com.panda.game.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/management/invite")
public class InviteManageController {

    @GetMapping("/index")
    public String index(){
        return "";
    }
}
