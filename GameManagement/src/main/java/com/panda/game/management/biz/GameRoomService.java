/***
 * @pName management
 * @name GameRoomService
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.entity.db.GameRoom;
import com.panda.game.management.entity.dbExt.GameRoomDetailInfo;

import java.util.List;

public interface GameRoomService extends BaseService<GameRoom> {
    /**
     * 插入数据 韦德 2018年8月18日23:01:25
     * @param token
     * @param param
     * @return
     */
    int insert(String token, GameRoom param);

    /**
     * 查询数据 韦德 2018年8月19日15:46:11
     * @param token
     * @return
     */
    List<GameRoomDetailInfo> getRoomList(String token);

    /**
     * 解散房间 韦德 2018年8月20日01:05:48
     * @param token
     * @param gameRoom
     */
    void disband(String token, GameRoom gameRoom);

    /**
     * 申请结算 韦德 2018年8月20日01:07:26
     * @param token
     * @param standings
     * @param beRouted
     */
    void closeAccounts(String token, Double standings, Double beRouted);
}
