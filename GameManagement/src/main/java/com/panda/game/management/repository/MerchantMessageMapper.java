/***
 * @pName GameManagement
 * @name BusinessMessageMapper
 * @user HongWei
 * @date 2018/10/23
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.MerchantMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MerchantMessageMapper extends MyMapper<MerchantMessage> {
    @Select("SELECT * FROM tb_merchant_messages WHERE parent_user_id = #{parentUserId} ORDER BY add_time DESC")
    /**
     * 查询子用户信息 韦德 2018年10月23日15:17:12
     * @param userId
     * @return
     */
    List<MerchantMessage> selectChildren(@Param("parentUserId") Integer userId);
}
