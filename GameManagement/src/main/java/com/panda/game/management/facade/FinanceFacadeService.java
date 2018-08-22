/***
 * @pName management
 * @name FinanceFacadeService
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.panda.game.management.facade;

import com.panda.game.management.biz.PayService;
import com.panda.game.management.entity.db.Withdraw;

public interface FinanceFacadeService {

    /**
     * 申请提现 韦德 2018年8月21日10:57:26
     *
     * @param token
     * @param amount
     */
    void addWithdraw(String token, Double amount);


    /**
     * 申请提现 韦德 2018年8月21日10:42:23
     * @return
     */
    boolean confirmWithdraw(Withdraw withdraw);
}
