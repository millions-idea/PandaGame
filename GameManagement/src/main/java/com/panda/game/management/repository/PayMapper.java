/***
 * @pName management
 * @name PayMapper
 * @user HongWei
 * @date 2018/8/16
 * @desc 财务交易记录表
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.Accounts;
import com.panda.game.management.entity.db.Pays;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PayMapper extends  MyMapper<Pays>{
    @Select("SELECT \n" +
            "COALESCE(SUM(amount),0) \n" +
            "- \n" +
            "(SELECT COALESCE(SUM(amount),0) FROM `tb_accounts` WHERE trade_account_id = #{userId} AND currency = 1 AND accounts_type = 2)\n" +
            "FROM `tb_accounts` WHERE trade_account_id = #{userId} AND accounts_type = 1 AND currency = 1 \n")
    //@Select("SELECT sum(amount) FROM tb_pays WHERE to_uid = #{userId} AND currency = 1 AND (remark IS NULL OR remark != #{remark})")
    /**
     * 统计不可提现金额 韦德 2018年8月20日18:43:47
     * @param userId
     * @param remark
     * @return
     */
    Double selectNotWithdrawAmount(@Param("userId") Integer userId, @Param("remark") String remark);


    @Select("SELECT  after_balance - (" +
            "SELECT  COALESCE(SUM(amount),0)  - (" +
            " SELECT COALESCE(SUM(amount),0) FROM `tb_accounts` WHERE trade_account_id = #{userId} AND currency = 1 AND accounts_type = 2) " +
            "   FROM `tb_accounts` WHERE trade_account_id = #{userId} AND accounts_type = 1 AND currency = 1 " +
            ") FROM `tb_accounts` WHERE trade_account_id = #{userId} ORDER BY accounts_id DESC LIMIT 1;")
    /**
     * 统计可提现金额 韦德 2018年8月21日11:17:17
     * @param userId
     * @return
     */
    Double selectWithdrawAmount(@Param("userId") Integer userId);

    @Select("SELECT t1.*,t2.phone,t3.* FROM tb_pays t1 LEFT JOIN tb_users t2 ON t1.from_uid = t2.user_id " +
            " LEFT JOIN tb_pays t3 ON t1.to_uid = t2.user_id " +
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
    List<Pays> selectLimit(@Param("page") Integer page, @Param("limit") String limit
            , @Param("trade_type") Integer trade_type
            , @Param("beginTime") String trade_date_begin
            , @Param("endTime") String trade_date_end
            , @Param("condition")  String condition);

    @Select("SELECT COUNT(t1.pay_id) FROM tb_pays t1 LEFT JOIN tb_users t2 ON t1.from_uid = t2.user_id " +
            "LEFT JOIN tb_pays t3 ON t1.to_uid = t2.user_id " +
            "WHERE ${condition}")
    /**
     * 查询分页记录总数 韦德 2018年8月27日09:52:40
     * @param trade_type
     * @param trade_date_begin
     * @param trade_date_end
     * @param where
     * @return
     */
    int getPaysLimitCount( @Param("trade_type") Integer tradeType
            , @Param("beginTime") String trade_date_begin
            , @Param("endTime") String trade_date_end
            , @Param("condition")  String condition);

    @Update("UPDATE tb_pays SET channel_record_id=#{channelRecordId},to_account_time=NOW() WHERE system_record_id=#{systemRecordId}")
    /**
     * 更新交易回执单号 韦德  2018年8月30日14:55:50
     * @param systemRecordId
     * @param channelRecordId
     * @return
     */
    int updateChannelRecordId(@Param("systemRecordId") Long systemRecordId, @Param("channelRecordId") String channelRecordId);
}
