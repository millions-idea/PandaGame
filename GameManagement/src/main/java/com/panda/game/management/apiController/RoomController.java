/***
 * @pName management
 * @name RoomController
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.apiController;

import com.panda.game.management.biz.GameRoomService;
import com.panda.game.management.entity.JsonArrayResult;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.db.GameRoom;
import com.panda.game.management.entity.dbExt.GameRoomDetailInfo;
import com.panda.game.management.facade.GameRoomFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    @Autowired
    private GameRoomService gameRoomService;
    @Autowired
    private GameRoomFacadeService gameRoomFacadeService;

    @PostMapping("/create")
    public JsonResult create(String token, GameRoom gameRoom){
        gameRoomService.insert(token, gameRoom);
        return JsonResult.successful();
    }

    @GetMapping("/getRoomList")
    public JsonArrayResult<GameRoomDetailInfo> getRoomList(String token){
        List<GameRoomDetailInfo> list = gameRoomService.getRoomList(token);
        return new JsonArrayResult<>(list);
    }

    @GetMapping("/getAllRoomList")
    public JsonArrayResult<GameRoomDetailInfo> getAllRoomList(){
        List<GameRoomDetailInfo> list = gameRoomService.getAllRoomList();
        return new JsonArrayResult<>(list);
    }

    @PostMapping("/disband")
    public JsonResult disband(String token, GameRoom gameRoom){
        gameRoomService.disband(token, gameRoom);
        return JsonResult.successful();
    }

    @PostMapping("/closeAccounts")
    public JsonResult closeAccounts(String token, Long roomCode, Double standings, Double beRouted){
        gameRoomFacadeService.closeAccounts(token, roomCode, standings, beRouted);
        return JsonResult.successful();
    }

    @PostMapping("/join")
    public JsonResult join(String token, GameRoom gameRoom){
        gameRoomService.join(token, gameRoom);
        return JsonResult.successful();
    }


    @GetMapping("/getLimitRoom")
    public JsonResult<GameRoomDetailInfo> getLimitRoom(String subareasId){
        GameRoomDetailInfo model =  gameRoomService.getLimitRoom(subareasId);
        if(model == null) return new JsonResult<>().normalExceptionAsString("没有匹配到合适的房间");
        return new JsonResult<>().successful(model);
    }
}
