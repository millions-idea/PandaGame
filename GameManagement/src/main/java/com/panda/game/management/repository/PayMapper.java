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
    @Select("SELECT sum(amount) FROM tb_pays WHERE to_uid = #{userId} AND trade_type = 6 AND remark = #{remark}")
    /**
     * 统计不可提现金额 韦德 2018年8月20日18:43:47
     * @param userId
     * @param remark
     * @return
     */
    Double selectNotWithdrawAmount(@Param("userId") Integer userId, @Param("remark") String remark);
}
