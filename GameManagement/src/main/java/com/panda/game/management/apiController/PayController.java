/***
 * @pName management
 * @name PayController
 * @user HongWei
 * @date 2018/8/16
 * @desc
 */
package com.panda.game.management.apiController;

import com.panda.game.management.biz.PayService;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.param.PayParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pay")
public class PayController {
    @Autowired
    private PayService payService;

    @PostMapping("/transfer")
    public JsonResult transfer(PayParam payParam){
        payService.transfer(payParam);
        return JsonResult.successful();
    }
}
