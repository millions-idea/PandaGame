/***
 * @pName management
 * @name MemberController
 * @user HongWei
 * @date 2018/8/31
 * @desc
 */
package com.panda.game.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/management/member")
public class MemberController {
    @GetMapping("/index")
    public String  index(){
        return "member/index";
    }

}
