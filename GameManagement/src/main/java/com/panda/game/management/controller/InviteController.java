/***
 * @pName GameManagement
 * @name InviteController
 * @user HongWei
 * @date 2018/10/12
 * @desc
 */
package com.panda.game.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/invite")
public class InviteController {
    @GetMapping(value = {"/index", ""})
    public String index(Integer code){
        return "invite/index";
    }
}
