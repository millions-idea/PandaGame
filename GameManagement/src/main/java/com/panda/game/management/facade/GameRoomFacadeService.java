/***
 * @pName management
 * @name GameRoomFacadeService
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.panda.game.management.facade;

import java.util.function.Consumer;

public interface GameRoomFacadeService {
    /**
     * 结算 韦德 2018年8月21日01:02:27
     * @param token
     * @param roomCode
     * @param grade
     */
    void  closeAccounts(String token, Long roomCode, Double grade);

    /**
     * 结算 韦德 2018年8月29日20:53:36
     * @param roomCode
     */
    void  closeAccounts(Long roomCode);
}
