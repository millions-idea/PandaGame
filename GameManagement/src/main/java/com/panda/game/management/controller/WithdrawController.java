/***
 * @pName management
 * @name WithdrawController
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.panda.game.management.controller;

import com.panda.game.management.biz.IMessageService;
import com.panda.game.management.biz.IWithdrawService;
import com.panda.game.management.entity.JsonArrayResult;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.db.Messages;
import com.panda.game.management.entity.db.Withdraw;
import com.panda.game.management.entity.dbExt.WithdrawDetailInfo;
import com.panda.game.management.entity.resp.WithdrawResp;
import com.panda.game.management.facade.FinanceFacadeService;
import com.panda.game.management.utils.PropertyUtil;
import com.panda.game.management.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/management/withdraw")
public class WithdrawController extends BaseController{
    @Autowired
    private FinanceFacadeService financeFacadeService;
    @Autowired
    private IWithdrawService withdrawService;
    @Autowired
    private IMessageService messageService;

    @PostMapping("/confirm")
    @ResponseBody
    public JsonResult confirmWithdraw(Withdraw withdraw){
        financeFacadeService.confirmWithdraw(withdraw);
        messageService.pushMessage(new Messages(null, withdraw.getUserId(), "恭喜您的提现已到账，请登录支付账户查收！", 0, new Date()));
        return JsonResult.successful();
    }


    @PostMapping("/pass")
    @ResponseBody
    public JsonResult pass(Withdraw withdraw){
        withdrawService.pass(withdraw);
        String message = "您的提现未成功";
        if(withdraw.getRemark() != null) message = "您的提现未成功，原因：" + withdraw.getRemark();
        messageService.pushMessage(new Messages(null, withdraw.getUserId(), message, 0, new Date()));
        return JsonResult.successful();
    }

    @GetMapping("/index")
    public String  index(){
        return "withdraw/index";
    }

    /**
     * 提现审批 韦德 2018年8月29日11:42:31
     * @return
     */
    @GetMapping("/getWithdrawLimit")
    @ResponseBody
    public JsonArrayResult<WithdrawResp> getWithdrawLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime){
        Integer count = 0;
        List<WithdrawDetailInfo> list = withdrawService.getWithdrawLimit(page, limit, condition, state, beginTime, endTime);
        List<WithdrawResp> wrapList = new ArrayList<>();
        list.forEach(item -> {
            WithdrawResp withdrawResp = new WithdrawResp();
            PropertyUtil.clone(item, withdrawResp);
            withdrawResp.setSystemRecordId(item.getSystemRecordId().toString());
            wrapList.add(withdrawResp);
        });
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, wrapList);
        if (StringUtil.isBlank(condition)
                && StringUtil.isBlank(beginTime)
                && StringUtil.isBlank(endTime)
                && (state == null || state == 0)){
            count = withdrawService.getWithdrawCount();
        }else{
            count = withdrawService.getWithdrawLimitCount(condition, state, beginTime, endTime);
        }
        jsonArrayResult.setCount(count);
        return jsonArrayResult;
    }
}
