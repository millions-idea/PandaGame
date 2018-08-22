/***
 * @pName management
 * @name PayMapper
 * @user HongWei
 * @date 2018/8/16
 * @desc 财务交易记录表
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.Pays;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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


    @Select("SELECT \n" +
            "after_balance \n" +
            "- \n" +
            "(\n" +
            "\tSELECT \n" +
            "\tCOALESCE(SUM(amount),0) \n" +
            "\t- \n" +
            "\t(SELECT COALESCE(SUM(amount),0) FROM `tb_accounts` WHERE trade_account_id = #{userId} AND currency = 1 AND accounts_type = 2)\n" +
            "\tFROM `tb_accounts` WHERE trade_account_id = #{userId} AND accounts_type = 1 AND currency = 1 \n" +
            ")\n" +
            "FROM `tb_accounts` WHERE trade_account_id = #{userId} ORDER BY add_time DESC LIMIT 1;")
    /*@Select("SELECT " +
            "COALESCE(SUM(amount),0) " +
            "- " +
            "COALESCE(SUM((SELECT amount FROM `tb_accounts` WHERE trade_account_id = #{userId} AND accounts_type = 2 AND currency = 0  AND (remark IS NULL OR remark != #{remark} ))),0) " +
            "FROM `tb_accounts` WHERE trade_account_id = #{userId} AND accounts_type = 1 AND currency = 0 AND (remark IS NULL OR remark !=  #{remark}  )")*/
    /**
     * 统计可提现金额 韦德 2018年8月21日11:17:17
     * @param userId
     * @return
     */
    Double selectWithdrawAmount(@Param("userId") Integer userId, @Param("remark") String remark);
}
