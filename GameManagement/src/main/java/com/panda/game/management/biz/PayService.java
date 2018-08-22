/***
 * @pName management
 * @name PayService
 * @user HongWei
 * @date 2018/8/16
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.annotaion.AspectLog;
import com.panda.game.management.entity.db.Pays;
import com.panda.game.management.entity.param.PayParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PayService extends BaseService<Pays> {
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
}
