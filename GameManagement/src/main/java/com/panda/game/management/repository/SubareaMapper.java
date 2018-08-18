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
}
