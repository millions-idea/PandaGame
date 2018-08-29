/***
 * @pName management
 * @name MessageMapper
 * @user HongWei
 * @date 2018/8/29
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.Messages;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper extends MyMapper<Messages> {
    @Select("SELECT * FROM tb_messages WHERE user_id=#{userId} ORDER BY add_time DESC")
    List<Messages> selectOrderBy(Messages messages);
}
