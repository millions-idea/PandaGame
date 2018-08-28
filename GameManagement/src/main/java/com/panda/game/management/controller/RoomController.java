/***
 * @pName management
 * @name RoomController
 * @user HongWei
 * @date 2018/8/28
 * @desc
 */
package com.panda.game.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/management/room")
public class RoomController {
    @GetMapping("/close-accounts")
    public String  closeAccounts(){
        return "room/close-accounts";
    }
}
