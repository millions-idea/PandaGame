/***
 * @pName management
 * @name GameRoomMapper
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.GameRoom;
import com.panda.game.management.entity.dbExt.GameRoomDetailInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GameRoomMapper extends MyMapper<GameRoom> {

    @Select("SELECT t1.*" +
            ",(SELECT COUNT(*) FROM tb_game_member_group WHERE room_code = t1.room_code) AS personCount" +
            ",(SELECT is_owner FROM tb_game_member_group WHERE room_code = t1.room_code) AS isOwner " +
            " FROM tb_game_room t1 WHERE owner_id=#{userId}  AND `status` !=  6 ORDER BY t1.add_time DESC")
    /**
     * 根据userId查询房间列表 韦德 2018年8月19日15:54:57
     * @param userId
     * @return
     */
    List<GameRoomDetailInfo> selectByUid(@Param("userId") Integer userId);
}
