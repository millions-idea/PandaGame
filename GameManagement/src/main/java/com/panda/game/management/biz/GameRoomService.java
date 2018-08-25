/***
 * @pName management
 * @name GameRoomService
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.entity.db.GameRoom;
import com.panda.game.management.entity.db.Users;
import com.panda.game.management.entity.dbExt.GameRoomDetailInfo;
import com.panda.game.management.entity.resp.GameRoomCallbackResp;

import java.util.List;
import java.util.function.Consumer;

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
     * @param param
     */
    void disband(String token, GameRoom param);


    /**
     * 申请结算 韦德 2018年8月20日01:07:26
     * @param token
     * @param roomCode
     * @param grade
     * @param lastPersonCallback
     */
    void closeAccounts(String token, Long roomCode, Double grade, Consumer<GameRoomCallbackResp> lastPersonCallback);

    /**
     * 获取所有游戏房间 韦德 2018年8月20日21:20:09
     * @return
     */
    List<GameRoomDetailInfo> getAllRoomList();

    /**
     * 加入房间 韦德 2018年8月20日21:40:51
     * @param token
     * @param gameRoom
     */
    boolean join(String token, GameRoom gameRoom);

    /**
     * 领取房卡 韦德 2018年8月20日23:28:15
     * @param token
     * @param users
     */
    void getRoomCard(String token, Users users);

    /**
     * 根据分区匹配房间 韦德 2018年8月21日17:11:02
     * @param subareasId
     * @return
     */
    GameRoomDetailInfo getLimitRoom(String subareasId);

    /**
     * 查询房间人数 韦德 2018年8月24日15:48:45
     * @param roomCode
     */
    Integer getPersonCount(String roomCode);
}
