/***
 * @pName management
 * @name FinanceController
 * @user HongWei
 * @date 2018/8/27
 * @desc
 */
package com.panda.game.management.controller;

import com.panda.game.management.biz.IPayService;
import com.panda.game.management.entity.JsonArrayResult;
import com.panda.game.management.entity.db.Accounts;
import com.panda.game.management.entity.resp.AccountsResp;
import com.panda.game.management.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/management/finance")
public class FinanceController {
    @Autowired
    private IPayService payService;

    /**
     * 财务会计账目 韦德 2018年8月27日00:18:43
     * @return
     */
    @GetMapping("/accounts")
    public String  accounts(String accountsId){
        return "finance/accounts/index";
    }

    /**
     * 查询凭证-分页 韦德 2018年8月3日11:48:50
     * @param condition
     * @return
     */
    @RequestMapping("/accounts/getLimit")
    @ResponseBody
    public JsonArrayResult<AccountsResp> getVouchers(Integer page, String limit, String condition, Integer trade_type, String trade_date_begin, String trade_date_end){
        Integer count = 0;
        List<Accounts> list = payService.getAccountsLimit(page, limit, condition, trade_type, trade_date_begin, trade_date_end);
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0,list);
        if (StringUtil.isBlank(condition)
                && StringUtil.isBlank(trade_date_begin)
                && StringUtil.isBlank(trade_date_end)
                && (trade_type == null || trade_type == 0)){
            count = payService.getAccountsCount();
        }else{
            count = payService.getAccountsLimitCount(condition, trade_type, trade_date_begin, trade_date_end);
        }
        jsonArrayResult.setCount(count);
        return jsonArrayResult;
    }
}
