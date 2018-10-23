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


    @Select("SELECT t1.*, t2.balance,t3.permission_role FROM tb_users t1 " +
            "LEFT JOIN tb_wallets t2 ON t1.user_id = t2.user_id " +
            "LEFT JOIN tb_permission_relation t3 ON t1.user_id = t3.user_id " +
            "WHERE ${condition} GROUP BY t1.user_id ORDER BY t1.add_date DESC LIMIT #{page},${limit}")
    /**
     * 分页查询 韦德 2018年8月30日11:33:22
     * @param page
     * @param limit
     * @param state
     * @param beginTime
     * @param endTime
     * @param where
     * @return
     */
    List<UserDetailInfo> selectLimit(@Param("page") Integer page, @Param("limit") String limit
            , @Param("isEnable") Integer isEnable
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition") String condition);

    @Select("SELECT COUNT(t1.user_id) FROM tb_users t1\n" +
            "LEFT JOIN tb_wallets t2 ON t1.user_id = t2.user_id \n" +
            "WHERE ${condition}")
    /**
     * 分页查询记录数 韦德 2018年8月30日11:33:30
     * @param state
     * @param beginTime
     * @param endTime
     * @param where
     * @return
     */
    Integer selectLimitCount(@Param("isEnable") Integer isEnable
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition") String condition);


    @Select("SELECT * FROM tb_users WHERE parent_id = #{userId}")
    /**
     * 查询子用户列表 韦德 2018年10月23日17:41:15
     * @param userId
     * @return
     */
    List<Users> selectChildren(@Param("userId") Integer userId);
}
