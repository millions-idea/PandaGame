/***
 * @pName management
 * @name WithdrawController
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.panda.game.management.controller;

import com.panda.game.management.biz.IMessageService;
import com.panda.game.management.biz.IRechargeService;
import com.panda.game.management.entity.JsonArrayResult;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.db.Messages;
import com.panda.game.management.entity.db.Recharge;
import com.panda.game.management.entity.dbExt.RechargeDetailInfo;
import com.panda.game.management.entity.resp.RechargeResp;
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
@RequestMapping("/management/recharge")
public class RechargeController extends BaseController{
    @Autowired
    private FinanceFacadeService financeFacadeService;
    @Autowired
    private IRechargeService rechargeService;
    @Autowired
    private IMessageService messageService;

    @PostMapping("/confirm")
    @ResponseBody
    public JsonResult confirmRecharge(Recharge recharge){
        financeFacadeService.confirmRecharge(recharge);
        messageService.pushMessage(new Messages(null, recharge.getUserId(), "恭喜您充值的" + recharge.getAmount() +"元已到账，请注意查收！", 0, new Date()));
        return JsonResult.successful();
    }


    @PostMapping("/pass")
    @ResponseBody
    public JsonResult pass(Recharge recharge){
        rechargeService.pass(recharge);
        String message = "您充值的" + recharge.getAmount() +"元未成功";
        if(recharge.getRemark() != null) message = "您充值的" + recharge.getAmount() +"元未成功，原因：" + recharge.getRemark();
        messageService.pushMessage(new Messages(null, recharge.getUserId(), message, 0, new Date()));
        return JsonResult.successful();
    }

    @GetMapping("/index")
    public String  index(){
        return "recharge/index";
    }

    /**
     * 提现审批 韦德 2018年8月29日11:42:31
     * @return
     */
    @GetMapping("/getRechargeLimit")
    @ResponseBody
    public JsonArrayResult<RechargeResp> getRechargeLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime){
        Integer count = 0;
        List<RechargeDetailInfo> list = rechargeService.getLimit(page, limit, condition, state, beginTime, endTime);
        List<RechargeResp> wrapList = new ArrayList<>();
        list.forEach(item -> {
            RechargeResp rechargeResp = new RechargeResp();
            PropertyUtil.clone(item, rechargeResp);
            rechargeResp.setSystemRecordId(item.getSystemRecordId().toString());
            wrapList.add(rechargeResp);
        });
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, wrapList);
        if (StringUtil.isBlank(condition)
                && StringUtil.isBlank(beginTime)
                && StringUtil.isBlank(endTime)
                && (state == null || state == 0)){
            count = rechargeService.getCount();
        }else{
            count = rechargeService.getLimitCount(condition, state, beginTime, endTime);
        }
        jsonArrayResult.setCount(count);
        return jsonArrayResult;
    }
}
