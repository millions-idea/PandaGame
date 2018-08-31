/***
 * @pName management
 * @name GameMemberGroupMapper
 * @user HongWei
 * @date 2018/8/19
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.GameMemberGroup;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface GameMemberGroupMapper extends MyMapper<GameMemberGroup> {
    @Select("SELECT * FROM tb_game_member_group WHERE room_code = #{roomCode} AND is_owner != 1")
    /**
     * 根据房间号查询成员列表 韦德 2018年8月20日10:36:25
     * @param roomCode
     * @return
     */
    List<GameMemberGroup> selectByRoomCode(@Param("roomCode") Long roomCode);

    @Select("SELECT * FROM tb_game_member_group WHERE room_code = #{roomCode} AND is_confirm = 0")
    /**
     * 根据房间号查询未结算成员列表 韦德 2018年8月20日10:36:25
     * @param roomCode
     * @return
     */
    List<GameMemberGroup> selectNotSettlementByRoomCode(@Param("roomCode") Long roomCode);

    @Update("UPDATE tb_game_member_group SET exit_time=NOW() WHERE room_code=#{roomCode}")
    /**
     * 退出房间 韦德 2018年8月20日10:53:27
     * @param thatMember
     */
    int updateMemberExit(@Param("roomCode") Long roomCode);

    @Update("UPDATE tb_game_member_group SET exit_time=NOW(), is_confirm=1 WHERE user_id=#{userId} AND room_code=#{roomCode}")
    /**
     * 结算 韦德 2018年8月20日10:53:27
     * @param thatMember
     */
    int updateConfirm(@Param("userId")  Integer userId, @Param("roomCode")  Long roomCode);

    @Delete("DELETE FROM tb_game_member_group WHERE user_id=#{userId} AND room_code=#{roomCode}")
    /**
     * 删除成员 韦德 2018年8月20日23:18:42
     * @param userId
     * @param roomCode
     * @return
     */
    int deleteMember(@Param("userId")  Integer userId, @Param("roomCode")  Long roomCode);

    @Select("SELECT * FROM tb_game_member_group WHERE room_code = #{roomCode}")
    /**
     * 查询指定房间内的成员列表 韦德 2018年8月21日01:40:42
     * @param roomCode
     * @return
     */
    List<GameMemberGroup> getListByRoom(@Param("roomCode") Long roomCode);

    @Select("SELECT t1.*,t2.* FROM tb_game_member_group t1\n" +
            "LEFT JOIN tb_game_room t2 ON t1.room_code = t2.room_code WHERE t1.user_id = #{userId} AND (t2.status != 5 AND t1.is_confirm != 0)")
    /**
     * 查询未结束的游戏房间 韦德 2018年8月21日17:34:15
     * @param userId
     * @return
     */
    List<GameMemberGroup> selectGoodRoomList(@Param("userId") Integer userId);

    @Select("SELECT * FROM tb_game_member_group WHERE user_id = #{userId} AND is_confirm = 0 AND exit_time IS NULL")
    /**
     * 查询未结束的游戏房间 韦德 2018年8月21日17:34:15
     * @param userId
     * @return
     */
    List<GameMemberGroup> selectByUid(@Param("userId") Integer userId);

    @Select("SELECT COUNT(*) FROM tb_game_member_group WHERE room_code = #{roomCode} AND is_confirm = 0")
    /**
     * 查询房间人数
     * @param roomCode
     * @return
     */
    int selectPersonCount(@Param("roomCode") String roomCode);

    @Select("SELECT COUNT(*) FROM tb_game_room WHERE owner_id = #{userId} AND (`status` != 6 AND `status` != 5) AND is_enable = 1")
    /**
     * 查询正在游戏中的房间数量 韦德 2018年8月24日22:45:38
     * @param userId
     * @return
     */
    int selectPlayingCount(@Param("userId") Integer userId);

    @Select("SELECT COUNT(*) FROM tb_game_member_group WHERE user_id = #{userId} AND room_code = #{roomCode} AND is_confirm = 1")
    /**
     * 是否结算 韦德 2018年8月25日01:28:55
     * @param userId
     * @param roomCode
     * @return
     */
    int selectConfirm(@Param("userId") Integer userId, @Param("roomCode") Long roomCode);

    @Update("UPDATE tb_game_member_group SET is_confirm=#{isConfirm} WHERE room_code=#{roomCode}")
    int updateByRoomCode(GameMemberGroup gameMemberGroup);
}
