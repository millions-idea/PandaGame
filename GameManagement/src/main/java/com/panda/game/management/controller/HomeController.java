/***
 * @pName management
 * @name HomeController
 * @user HongWei
 * @date 2018/8/27
 * @desc
 */
package com.panda.game.management.controller;

import com.panda.game.management.entity.DynamicConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/management")
public class HomeController {
    @Autowired
    private DynamicConfiguration dynamicConfiguration;

    @GetMapping("/index")
    public String index(){
        System.out.println("Domain url: " + dynamicConfiguration.getDomain());
        return "home/index";
    }
}
