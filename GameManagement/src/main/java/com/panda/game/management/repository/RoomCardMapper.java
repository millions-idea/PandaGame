/***
 * @pName management
 * @name RoomCardMapper
 * @user HongWei
 * @date 2018/8/20
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.RoomCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoomCardMapper extends MyMapper<RoomCard> {
    @Select("SELECT * FROM tb_room_card WHERE user_id=#{userId} AND pandaId = #{pandaId} LIMIT 1 ORDER BY add_time DESC")
    /**
     * 查询最后一次领取记录 韦德 2018年8月20日23:38:58
     * @param userId
     * @param pandaId
     * @return
     */
    RoomCard selectLast(@Param("userId") Integer userId, @Param("pandaId") String pandaId);
}
