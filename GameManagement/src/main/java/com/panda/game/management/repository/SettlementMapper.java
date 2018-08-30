/***
 * @pName management
 * @name SettlementMapper
 * @user HongWei
 * @date 2018/8/20
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.Settlement;
import com.panda.game.management.entity.dbExt.SettlementDetailInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SettlementMapper extends MyMapper<Settlement>{
    @Select("SELECT COUNT(*) FROM(SELECT COUNT(DISTINCT t1.room_code) FROM tb_settlement t1\n" +
            "            LEFT JOIN tb_game_room t2 ON t1.room_code = t2.room_code GROUP BY t2.room_code) t3")
    /**
     * 查询记录总数 韦德 2018年8月27日09:52:40
     */
    int selectCountDistinct();

    @Select("SELECT COUNT(*) FROM(SELECT COUNT(DISTINCT t1.room_code) FROM tb_settlement t1\n" +
            "LEFT JOIN tb_game_room t2 ON t1.room_code = t2.room_code \n" +
            "WHERE ${condition} GROUP BY t2.room_code) t3")
    /**
     * 查询分页记录总数 韦德 2018年8月27日09:52:40
     */
    int selectLimitCountGroup(@Param("state") Integer state
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition")  String condition);


    @Select("SELECT t1.*,SUM(t1.grade) AS countGrade, t2.name AS roomName, t2.room_code, t2.status, t2.external_room_id FROM tb_settlement t1 " +
            "LEFT JOIN tb_game_room t2 ON t1.room_code = t2.room_code " +
            "WHERE ${condition} GROUP BY t2.room_code ORDER BY t1.add_time DESC LIMIT #{page},${limit}")
    /**
     * 查询分页 韦德 2018年8月27日00:39:38
     * @return
     */
    List<SettlementDetailInfo> selectLimitGroup(@Param("page") Integer page, @Param("limit") String limit
            , @Param("state") Integer state
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition")  String condition);

    @Update("UPDATE tb_settlement SET state = 0 WHERE user_id=#{userId} AND room_code=#{roomCode}")
    /**
     * 更新确认状态 韦德 2018年8月29日15:59:25
     * @param userId
     * @param roomCode
     * @return
     */
    int updateConfirm(@Param("userId") Integer userId, @Param("roomCode") Long roomCode);


    @Select("SELECT t1.*,t2.phone,t2.panda_id FROM tb_settlement t1\n" +
            "LEFT JOIN tb_users t2 ON t1.user_id = t2.user_id\n" +
            "WHERE t1.room_code = #{roomCode}")
    /**
     * 查询 韦德 2018年8月27日00:39:38
     * @return
     */
    List<SettlementDetailInfo> selectDetailInfo(@Param("roomCode") Long roomCode);

    @Update("UPDATE tb_settlement SET grade=#{grade} WHERE user_id=#{userId} AND room_code=#{roomCode}")
    /**
     * 修改成绩
     * @param userId
     * @param grade
     */
    int updateGrade(@Param("userId") Integer userId, @Param("grade") Long grade, @Param("roomCode") Long roomCode);

    @Update("UPDATE tb_settlement SET update_time=NOW(),state=#{state} WHERE room_code=#{roomCode}")
    int updateByRoomCode(Settlement settlement);

    @Update("UPDATE tb_settlement SET update_time=NOW(),state=#{state} WHERE room_code=#{roomCode}")
    int updateStatusByRoomCode(@Param("roomCode") Long roomCode,@Param("state") int state);
}
