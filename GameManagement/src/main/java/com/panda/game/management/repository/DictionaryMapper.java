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
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

    @Select("SELECT * FROM tb_dictionary WHERE `key` LIKE '${likeKey}%'")
    /**
     * 模糊查询 韦德 2018年8月18日13:14:02
     * @param likeKey
     * @return
     */
    List<Dictionary> selectByKey(@Param("likeKey") String likeKey);

    @Update("UPDATE tb_dictionary SET `value`=#{url} WHERE dictionary_id=#{dictionaryId}")
    int updateUrlById(@Param("dictionaryId") Integer dictionaryId,@Param("url") String url);
}
