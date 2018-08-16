/***
 * @pName management
 * @name DictionaryMapper
 * @user HongWei
 * @date 2018/8/16
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.Dictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface DictionaryMapper extends MyMapper<Dictionary> {
    @Select("SELECT * FROM tb_dictionary")
    /**
     * 查询全部
     * @return
     */
    List<Dictionary> selectAll();
}
