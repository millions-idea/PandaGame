/***
 * @pName management
 * @name GameMemberGroupMapper
 * @user HongWei
 * @date 2018/8/19
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.GameMemberGroup;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GameMemberGroupMapper extends MyMapper<GameMemberGroup> {
}
