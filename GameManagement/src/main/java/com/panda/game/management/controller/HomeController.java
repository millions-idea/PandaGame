/***
 * @pName management
 * @name HomeController
 * @user HongWei
 * @date 2018/8/27
 * @desc
 */
package com.panda.game.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/management")
public class HomeController {

    @GetMapping("/index")
    public String index(){
        return "home/index";
    }
}
