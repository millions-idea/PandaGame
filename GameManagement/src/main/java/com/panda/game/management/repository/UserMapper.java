/***
 * @pName management
 * @name UserMapper
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.Users;
import com.panda.game.management.entity.dbExt.UserDetailInfo;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper extends MyMapper<Users> {

    @Update("UPDATE tb_users SET update_date=NOW(), ip=#{ip} WHERE user_id = #{userId} AND is_delete = 0 AND is_enable = 1")
    /**
     * 更新用户登录信息 韦德 2018年8月14日10:55:59
     * @param condition
     * @return
     */
    int updateOne(Users condition);


    @Select("SELECT t1.*,t2.* FROM tb_users t1 LEFT JOIN tb_wallets t2 ON t2.user_id = t1.user_id WHERE t1.user_id IN(${userId})")
    /**
     * 查询交易主体信息 韦德 2018年8月16日13:33:09
     * @param userId
     * @return
     */
    List<UserDetailInfo> selectInUid(@Param("userId") String userId);

    @Select("SELECT t1.*,t2.* FROM tb_users t1 LEFT JOIN tb_wallets t2 ON t2.user_id = t1.user_id WHERE t1.user_id =#{userId}")
    /**
     * 查询交易主体信息 韦德 2018年8月16日13:33:09
     * @param userId
     * @return
     */
    UserDetailInfo selectUserDetail(@Param("userId") String userId);
}
