/***
 * @pName management
 * @name GameRoomMapper
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.GameRoom;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GameRoomMapper extends MyMapper<GameRoom> {

}
