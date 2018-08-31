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
import com.panda.game.management.entity.db.Recharge;
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
    private com.panda.game.management.biz.IWithdrawService withdrawService;
    @Autowired
    private com.panda.game.management.biz.IPayService payService;
    @Autowired
    private com.panda.game.management.biz.IRechargeService rechargeService;

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

        // 转账
        PayParam payParam = new PayParam();
        payParam.setFromUid(Integer.valueOf(userId));
        payParam.setAmount(amount);
        payParam.setToUid(Constant.SYSTEM_ACCOUNTS_ID);
        long  systemRecordId = payService.withdraw(payParam);

        withdrawService.addWithdraw(token, amount, systemRecordId);
    }

    /**
     * 提现审批 韦德 2018年8月21日10:42:23
     *
     * @param withdraw
     * @return
     */
    @Override
    @Transactional
    public boolean confirmWithdraw(Withdraw withdraw) {
        // 1、更新提现审批表
        Withdraw model = withdrawService.getWithdrawById(withdraw);
        if(model == null) throw new MsgException("审批请求不存在");

        PayParam payParam = new PayParam();
        payParam.setFromUid(model.getUserId());
        payParam.setAmount(withdraw.getAmount());
        payParam.setToUid(Constant.SYSTEM_ACCOUNTS_ID);
        long  systemRecordId = payService.withdraw(payParam);

        model.setUpdateTime(new Date());
        model.setState(1);
        model.setRemark(withdraw.getRemark());
        model.setSystemRecordId(systemRecordId);
        model.setChannelRecordId(withdraw.getChannelRecordId());
        int count = withdrawService.update(model);
        if(count == 0) throw new MsgException("编辑提现状态失败");

        // 2、更新财务日志
        count = 0;
        count = payService.updateChannelRecordId(systemRecordId,  withdraw.getChannelRecordId());
        if(count == 0) throw new MsgException("更新交易回执失败");

        withdraw.setUserId(model.getUserId());
        return true;
    }

    /**
     * 充值审批 韦德 2018年8月21日10:42:23
     *
     * @param recharge
     * @return
     */
    @Override
    @Transactional
    public boolean confirmRecharge(Recharge recharge) {
        Recharge model = rechargeService.getById(recharge);
        if(model == null) throw new MsgException("审批请求不存在");

        // 1、插入交易流水
        PayParam payParam = new PayParam();
        payParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
        payParam.setAmount(recharge.getAmount());
        payParam.setToUid(model.getUserId());
        long  systemRecordId = payService.recharge(payParam);

        // 2、更新提现审批表
        model.setUpdateTime(new Date());
        model.setState(1);
        model.setRemark(recharge.getRemark());
        model.setSystemRecordId(systemRecordId);
        model.setChannelRecordId(recharge.getChannelRecordId());
        int count = rechargeService.update(model);
        if(count == 0) throw new MsgException("编辑审批状态失败");


        // 3、更新财务日志
        count = 0;
        count = payService.updateChannelRecordId(systemRecordId,  model.getChannelRecordId());
        if(count == 0) throw new MsgException("更新交易回执失败");

        recharge.setUserId(model.getUserId());
        return true;
    }

    /**
     * 充值 韦德 2018年8月31日18:14:50
     *
     * @param token
     * @param amount
     */
    @Override
    @Transactional
    public void addRecharge(String token, Double amount) {
        // 加载用户信息
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) JsonResult.failing();
        String userId = map.get("userId");
        if(userId == null || userId.isEmpty()) throw new MsgException("身份校验失败");

        // 转账

        // 2、插入交易流水
        PayParam payParam = new PayParam();
        payParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
        payParam.setAmount(amount);
        payParam.setToUid(Integer.valueOf(userId));
        long  systemRecordId = payService.recharge(payParam);

    }

}
