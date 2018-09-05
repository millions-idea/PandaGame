/***
 * @pName management
 * @name PayController
 * @user HongWei
 * @date 2018/8/16
 * @desc
 */
package com.panda.game.management.apiController;

import com.panda.game.management.biz.IPayService;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.param.PayParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pay")
public class PayApiController {
    @Autowired
    private IPayService payService;

    @PostMapping("/transfer")
    public JsonResult transfer(PayParam payParam){
        payService.transfer(payParam);
        return JsonResult.successful();
    }

    /**
     * 前台统一支付接口 韦德 2018年9月5日18:12:21
     * @param payId 支付id：alipay、wxpay
     * @param total 支付总额
     * @return
     */
    @PostMapping("/payment")
    public JsonResult payment(String payId, Double total){
        return JsonResult.successful();
    }
}
