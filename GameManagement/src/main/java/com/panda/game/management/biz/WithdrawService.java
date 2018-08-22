/***
 * @pName management
 * @name WithdrawService
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.entity.db.Withdraw;

public interface WithdrawService extends BaseService<Withdraw> {

    /**
     * 申请提现 韦德 2018年8月21日10:57:26
     * @param token
     * @param amount
     */
    void addWithdraw(String token, Double amount);

    /**
     * 根据id查询提现信息 韦德 2018年8月21日13:39:01
     * @param withdraw
     * @return
     */
    Withdraw getWithdrawById(Withdraw withdraw);
}
