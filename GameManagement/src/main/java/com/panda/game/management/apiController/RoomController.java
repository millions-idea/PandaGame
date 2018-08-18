/***
 * @pName management
 * @name RoomController
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.apiController;

import com.panda.game.management.biz.GameRoomService;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.db.GameRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    @Autowired
    private GameRoomService gameRoomService;

    @PostMapping("/create")
    public JsonResult create(String token, GameRoom gameRoom){
        gameRoomService.insert(token, gameRoom);
        return JsonResult.successful();
    }
}
