/***
 * @pName management
 * @name PayService
 * @user HongWei
 * @date 2018/8/16
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.entity.db.Pays;
import com.panda.game.management.entity.param.PayParam;

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
}
