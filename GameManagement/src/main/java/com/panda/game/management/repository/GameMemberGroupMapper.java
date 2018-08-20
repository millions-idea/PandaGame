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

    @Delete("DELETE FROM tb_game_member_group WHERE user_id=#{userId} AND room_code=#{roomCode} AND status = 0")
    /**
     * 删除成员 韦德 2018年8月20日23:18:42
     * @param userId
     * @param roomCode
     * @return
     */
    int deleteMember(@Param("userId")  Integer userId, @Param("roomCode")  Long roomCode);
}
