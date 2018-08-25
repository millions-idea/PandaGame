/***
 * @pName management
 * @name GameRoomCallbackResp
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.panda.game.management.entity.resp;

import com.panda.game.management.entity.db.GameRoom;
import com.panda.game.management.entity.db.Settlement;
import com.panda.game.management.entity.db.Subareas;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GameRoomCallbackResp {
    private GameRoom gameRoom;
    private Subareas subareas;
}
