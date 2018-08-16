/***
 * @pName management
 * @name AccountMapper
 * @user HongWei
 * @date 2018/8/16
 * @desc 财务会计账目表
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.Accounts;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AccountMapper extends MyMapper<Accounts> {
    @Insert("<script>" +
            "INSERT INTO `tb_accounts` (`accounts_id`,`pay_id`, `trade_account_id`, `trade_account_name`, `accounts_type`, `amount`, `before_balance`, `after_balance`, `add_time`, `remark`) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.accountsId},#{item.payId}, #{item.tradeAccountId}, #{item.tradeAccountName}, #{item.accountsType}, #{item.amount}, (SELECT balance FROM tb_wallets WHERE user_id = #{item.tradeAccountId}),(SELECT CASE WHEN #{item.accountsType} = 1 THEN  balance + #{item.amount}  ELSE balance - #{item.amount}  END AS balance FROM tb_wallets WHERE user_id = #{item.tradeAccountId}), NOW(), #{item.remark})" +
            "</foreach>" +
            "</script>")
    /**
     * 批量插入 韦德 2018年8月16日15:42:01
     * @param list
     * @return
     */
    int insertList(@Param("list") List<Accounts> list);
}
