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
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GameRoomMapper extends MyMapper<GameRoom> {

    @Select("SELECT t1.* " +
            ",(SELECT COUNT(*) FROM tb_game_member_group WHERE room_code = t1.room_code AND is_confirm = 0) AS personCount " +
            ",(SELECT is_owner FROM tb_game_member_group WHERE user_id = #{userId} AND room_code = t1.room_code) AS isOwner " +
            "FROM tb_game_room t1 LEFT JOIN tb_game_member_group t2 ON t1.room_code = t2.room_code WHERE t2.user_id=#{userId} AND `status` != 6 ORDER BY t1.add_time DESC")
    /**
     * 根据userId查询房间列表 韦德 2018年8月19日15:54:57
     * @param userId
     * @return
     */
    List<GameRoomDetailInfo> selectByUid(@Param("userId") Integer userId);

    @Select("SELECT * FROM tb_game_room WHERE room_code = #{roomCode} AND `status` !=  6")
    /**
     * 查询 韦德 2018年8月20日10:42:28
     * @param roomCode
     * @return
     */
    GameRoom selectByRoomCode(@Param("roomCode") Long roomCode);


    @Update("UPDATE tb_game_room SET `status`=#{status} WHERE room_code=#{roomCode}")
    /**
     * 根据roomCode更新状态值 韦德 2018年8月20日10:57:19
     * @param roomCode
     * @param status
     */
    int updateStatusByRoomCode(@Param("roomCode") Long roomCode,@Param("status") int status);

    @Select("SELECT t1.* " +
            ",(SELECT COUNT(*) FROM tb_game_member_group WHERE room_code = t1.room_code AND is_confirm = 0) AS personCount " +
            ",(SELECT is_owner FROM tb_game_member_group WHERE user_id = t1.owner_id AND room_code = t1.room_code) AS isOwner  " +
            ",(SELECT `name` FROM tb_subareas WHERE subarea_id = t1.parent_area_id) AS parentName " +
            ",(SELECT `price` FROM tb_subareas WHERE subarea_id = t1.parent_area_id) AS parentPrice " +
            "FROM tb_game_room t1 WHERE `status` = 0 ORDER BY t1.add_time DESC")
    /**
     * 查询游戏大厅内显示的房间列表 韦德 2018年8月20日21:21:04
     * @return
     */
    List<GameRoomDetailInfo> selectSalaRoomList();


    @Select("SELECT COUNT(*) FROM tb_game_member_group WHERE room_code = #{roomCode}")
    /**
     * 查询房间在线人数 韦德 2018年8月20日22:18:54
     * @param roomCode
     * @return
     */
    int selectRoomOnLineCount(@Param("roomCode") Long roomCode);

    @Select("SELECT * FROM tb_game_room WHERE `status` = 0 AND is_enable = 1 AND parent_area_id IN(${subareasId}) LIMIT 1;")
    /**
     * 根据分区匹配合适的房间 韦德 2018年8月21日17:11:31
     * @param subareasId
     * @return
     */
    GameRoomDetailInfo getLimitRoom(@Param("subareasId") String subareasId);
}
