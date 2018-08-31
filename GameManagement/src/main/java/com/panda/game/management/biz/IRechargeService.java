/***
 * @pName management
 * @name WithdrawService
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.entity.db.Recharge;
import com.panda.game.management.entity.db.Withdraw;
import com.panda.game.management.entity.dbExt.RechargeDetailInfo;
import com.panda.game.management.entity.dbExt.WithdrawDetailInfo;

import java.util.List;

public interface IRechargeService extends IBaseService<Recharge> {

    /**
     * 添加数据 韦德 2018年8月21日10:57:26
     * @param token
     * @param amount
     * @param systemRecordId
     */
    void add(String token, Double amount, Long systemRecordId);

    /**
     * 根据id查询信息 韦德 2018年8月21日13:39:01
     * @param recharge
     * @return
     */
    Recharge getById(Recharge recharge);

    /**
     * 分页加载 韦德 2018年8月30日11:29:00
     * @param page
     * @param limit
     * @param condition
     * @param state
     * @param beginTime
     * @param endTime
     * @return
     */
    List<RechargeDetailInfo> getLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime);

    /**
     * 加载总记录数 韦德 2018年8月30日11:29:11
     * @return
     */
    Integer getCount();

    /**
     * 加载分页记录数 韦德 2018年8月30日11:29:22
     * @param condition
     * @param state
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer getLimitCount(String condition, Integer state, String beginTime, String endTime);


    /**
     * 拒绝审批 韦德 2018年8月30日15:18:45
     * @param recharge
     */
    boolean pass(Recharge recharge);
}
