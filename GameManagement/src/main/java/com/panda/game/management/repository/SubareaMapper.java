/***
 * @pName management
 * @name SubareaMapper
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.Subareas;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SubareaMapper extends MyMapper<Subareas> {
    @Select("SELECT * FROM tb_subareas WHERE  FIND_IN_SET (subarea_id, " +
            "(SELECT " +
            "GROUP_CONCAT( " +
            "t2.join_subarea_id " +
            "ORDER BY " +
            "join_subarea_id ASC " +
            ") AS join_subarea_list " +
            "FROM " +
            "tb_subareas t1 " +
            "LEFT JOIN tb_subarea_relations t2 ON t2.subarea_id = t1.subarea_id " +
            "WHERE " +
            "  t1.subarea_id = #{subareaId} AND t1.is_relation = 1 " +
            "GROUP BY " +
            "t1.subarea_id) " +
            ")")
    /**
     * 查询与指定id建立关系的数据列表 韦德 2018年8月18日17:16:44
     * @param subareaId
     * @return
     */
    List<Subareas> selectRelations(@Param("subareaId") Integer subareaId);


    @Select("SELECT t1.*  FROM tb_subareas t1 " +
            "LEFT JOIN tb_subarea_relations t2 ON t1.subarea_id = t2.subarea_id " +
            "WHERE ${condition} GROUP BY t1.subarea_id ORDER BY t1.is_relation DESC LIMIT #{page},${limit}")
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
    List<Subareas> selectLimit(@Param("page") Integer page, @Param("limit") String limit
            , @Param("isEnable") Integer isEnable
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition") String condition);

    @Select("SELECT COUNT(t1.subarea_id) FROM tb_subareas t1\n" +
            "LEFT JOIN tb_subarea_relations t2 ON t1.subarea_id = t2.subarea_id \n" +
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

    @Select("SELECT * FROM tb_subareas WHERE subarea_id = (SELECT subarea_id FROM tb_game_room WHERE room_code = #{roomCode})")
    /**
     * 通过房间号查询分区 韦德 2018年10月13日14:58:03
     * @param roomCode
     * @return
     */
    Subareas selectByRoomId(@Param("roomCode") Long roomCode);
}
