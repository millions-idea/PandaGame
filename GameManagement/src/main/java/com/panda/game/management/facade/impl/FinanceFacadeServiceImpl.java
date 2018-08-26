/***
 * @pName management
 * @name FinanceFacadeServiceImpl
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.panda.game.management.facade.impl;

import com.panda.game.management.entity.Constant;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.db.Withdraw;
import com.panda.game.management.entity.param.PayParam;
import com.panda.game.management.exception.MsgException;
import com.panda.game.management.facade.FinanceFacadeService;
import com.panda.game.management.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
public class FinanceFacadeServiceImpl implements FinanceFacadeService {
    @Autowired
    private com.panda.game.management.biz.IWithdrawService IWithdrawService;
    @Autowired
    private com.panda.game.management.biz.IPayService IPayService;

    /**
     * 申请提现 韦德 2018年8月21日10:57:26
     *
     * @param token
     * @param amount
     */
    @Override
    @Transactional
    public void addWithdraw(String token, Double amount) {
        // 加载用户信息
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) JsonResult.failing();
        String userId = map.get("userId");
        if(userId == null || userId.isEmpty()) throw new MsgException("身份校验失败");
        IWithdrawService.addWithdraw(token, amount);
        // 转账
        PayParam payParam = new PayParam();
        payParam.setFromUid(Integer.valueOf(userId));
        payParam.setAmount(amount);
        payParam.setToUid(Constant.SYSTEM_ACCOUNTS_ID);
        IPayService.withdraw(payParam);
    }

    /**
     * 申请提现 韦德 2018年8月21日10:42:23
     *
     * @param withdraw
     * @return
     */
    @Override
    @Transactional
    public boolean confirmWithdraw(Withdraw withdraw) {
        // 查询提现信息
        Withdraw model = IWithdrawService.getWithdrawById(withdraw);
        if(model == null) throw new MsgException("加载提现信息失败");

        // 更改状态
        model.setUpdateTime(new Date());
        model.setState(1);
        int count = IWithdrawService.update(model);
        if(count == 0) throw new MsgException("更改状态失败");

        // TODO 更新支付宝交易号
        return true;
    }
}
