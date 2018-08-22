/***
 * @pName management
 * @name WithdrawController
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.panda.game.management.controller;

import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.db.Withdraw;
import com.panda.game.management.facade.FinanceFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/withdraw")
public class WithdrawController {
    @Autowired
    private FinanceFacadeService financeFacadeService;

    @PostMapping("/confirm")
    @ResponseBody
    public JsonResult confirmWithdraw(Withdraw withdraw){
        financeFacadeService.confirmWithdraw(withdraw);
        return JsonResult.successful();
    }
}
