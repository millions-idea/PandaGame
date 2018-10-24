/***
 * @pName management
 * @name RoomCardMapper
 * @user HongWei
 * @date 2018/8/20
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.RoomCard;
import com.panda.game.management.entity.dbExt.RoomCardDetailInfo;
import com.panda.game.management.entity.dbExt.WithdrawDetailInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoomCardMapper extends MyMapper<RoomCard> {
    @Select("SELECT * FROM tb_room_card WHERE user_id=#{userId} AND panda_id = #{pandaId} ORDER BY add_time DESC  LIMIT 1")
    /**
     * 查询最后一次领取记录 韦德 2018年8月20日23:38:58
     * @param userId
     * @param pandaId
     * @return
     */
    RoomCard selectLast(@Param("userId") Integer userId, @Param("pandaId") String pandaId);


    @Select("SELECT t1.*, t2.phone FROM tb_room_card t1 " +
            "LEFT JOIN tb_users t2 ON t1.user_id = t2.user_id " +
            "WHERE ${condition} ORDER BY t1.add_time DESC LIMIT #{page},${limit}")
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
    List<RoomCardDetailInfo> selectLimit(@Param("page") Integer page, @Param("limit") String limit
            , @Param("state") Integer state
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition")  String condition);

    @Select("SELECT COUNT(t1.card_id) FROM tb_room_card t1\n" +
            "LEFT JOIN tb_users t2 ON t1.user_id = t2.user_id \n" +
            "WHERE ${condition}")
    /**
     * 分页查询记录数 韦德 2018年8月30日11:33:30
     * @param state
     * @param beginTime
     * @param endTime
     * @param where
     * @return
     */
    Integer selectLimitCount(@Param("state") Integer state
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition")  String condition);


    @Select("SELECT * FROM tb_room_card WHERE state = 0 LIMIT 1;")
    /**
     * 获取最新未充值的房卡记录
     * @return
     */
    RoomCard selectNewRoomCard();
}
