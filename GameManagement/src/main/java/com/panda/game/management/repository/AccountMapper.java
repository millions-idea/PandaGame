/***
 * @pName management
 * @name AccountMapper
 * @user HongWei
 * @date 2018/8/16
 * @desc 财务会计账目表
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.Accounts;
import com.panda.game.management.entity.resp.AccountsResp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AccountMapper extends MyMapper<Accounts> {
    @Insert("<script>" +
            "INSERT INTO `tb_accounts` (`accounts_id`,`pay_id`, `trade_account_id`, `trade_account_name`, `accounts_type`, `amount`, `before_balance`, `after_balance`, `add_time`, `remark`, `currency`) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.accountsId},#{item.payId}, #{item.tradeAccountId}, #{item.tradeAccountName}, #{item.accountsType}, #{item.amount}" +
            ",(SELECT CASE WHEN #{item.accountsType} = 1 THEN  balance - #{item.amount}  ELSE balance + #{item.amount}  END AS balance FROM tb_wallets WHERE user_id = #{item.tradeAccountId})" +
            ",(SELECT balance FROM tb_wallets WHERE user_id = #{item.tradeAccountId})" +
            ", NOW(), #{item.remark}, #{item.currency})" +
            "</foreach>" +
            "</script>")
    /**
     * 批量插入 韦德 2018年8月16日15:42:01
     * @param list
     * @return
     */
    int insertList(@Param("list") List<Accounts> list);

    @Select("SELECT t1.*,t2.phone FROM tb_accounts t1 LEFT JOIN tb_users t2 ON t1.trade_account_id = t2.user_id " +
            "WHERE ${condition} ORDER BY t1.add_time DESC LIMIT #{page},${limit}")
    /**
     * 查询分页 韦德 2018年8月27日00:39:38
     * @param page
     * @param limit
     * @param trade_type
     * @param trade_date_begin
     * @param trade_date_end
     * @param where
     * @return
     */
    List<Accounts> selectLimit(@Param("page") Integer page, @Param("limit") String limit
            , @Param("accounts_type") Integer accountsType
            , @Param("beginTime") String trade_date_begin
            , @Param("endTime") String trade_date_end
            , @Param("condition")  String condition);


    @Select("SELECT COUNT(t1.accounts_id) FROM tb_accounts t1 LEFT JOIN tb_users t2 ON t1.trade_account_id = t2.user_id " +
            "WHERE ${condition}")
    /**
     * 查询分页记录总数 韦德 2018年8月27日09:52:40
     * @param trade_type
     * @param trade_date_begin
     * @param trade_date_end
     * @param where
     * @return
     */
    int selectLimitCount( @Param("accounts_type") Integer accountsType
            , @Param("beginTime") String trade_date_begin
            , @Param("endTime") String trade_date_end
            , @Param("condition")  String condition);
}
