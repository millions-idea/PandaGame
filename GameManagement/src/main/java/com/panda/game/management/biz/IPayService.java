/***
 * @pName management
 * @name PayService
 * @user HongWei
 * @date 2018/8/16
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.entity.db.Accounts;
import com.panda.game.management.entity.db.Pays;
import com.panda.game.management.entity.param.PayParam;
import com.panda.game.management.entity.resp.AccountsResp;

import java.util.List;

public interface IPayService extends IBaseService<Pays> {
    /**
     * 转账 韦德 2018年8月16日13:17:17
     * @param payParam
     */
    void transfer(PayParam payParam);

    /**
     * 批量转账 韦德 2018年8月21日01:53:25
     * @param payParams
     */
    void batchTransfer(List<PayParam> payParams);

    /**
     * 提现 韦德 2018年8月21日13:42:56
     * @param payParam
     */
    void withdraw(PayParam payParam);

    /**
     * 消费 韦德 2018年8月16日13:17:17
     *
     * @param payParam
     */
    void consume(PayParam payParam);

    /**
     * 批量消费 韦德 2018年8月21日01:53:25
     * @param payParams
     */
    void batchConsume(List<PayParam> payParams);

    /**
     * 充值 韦德 2018年8月16日13:17:17
     *
     * @param payParam
     */
    void recharge(PayParam payParam);

    /**
     * 获取不可用余额 韦德 2018年8月21日15:50:04
     * @param userId
     * @return
     */
    Double getNotWithdrawAmount(Integer userId);

    /**
     * 获取可用余额 韦德 2018年8月21日15:50:04
     * @param userId
     * @return
     */
    Double getWithdrawAmount(Integer userId);

    /**
     * 分页加载财务会计账目数据列表 韦德 2018年8月27日00:20:54
     * @param page
     * @param limit
     * @param condition
     * @param trade_type
     * @param trade_date_begin
     * @param trade_date_end
     * @return
     */
    List<Accounts> getAccountsLimit(Integer page, String limit, String condition, Integer trade_type, String trade_date_begin, String trade_date_end);

    /**
     * 统计分页加载财务会计账目数据列表总条数 韦德 2018年8月27日09:58:27
     * @param condition
     * @param trade_type
     * @param trade_date_begin
     * @param trade_date_end
     * @return
     */
    int getAccountsLimitCount(String condition, Integer trade_type, String trade_date_begin, String trade_date_end);

    /**
     * 记载财务会计账目数据总条数 韦德 2018年8月27日00:21:16
     * @return
     */
    int getAccountsCount();
}
