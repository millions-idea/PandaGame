/***
 * @pName management
 * @name RoomController
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.apiController;

import com.panda.game.management.biz.IGameRoomService;
import com.panda.game.management.entity.JsonArrayResult;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.db.GameRoom;
import com.panda.game.management.entity.dbExt.GameRoomDetailInfo;
import com.panda.game.management.exception.MsgException;
import com.panda.game.management.facade.GameRoomFacadeService;
import com.panda.game.management.utils.IdWorker;
import com.panda.game.management.utils.StringUtil;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/room")
public class RoomApiController {
    @Autowired
    private IGameRoomService gameRoomService;
    @Autowired
    private GameRoomFacadeService gameRoomFacadeService;
    private static Integer lock = 103697367;

    @PostMapping("/create")
    public JsonResult create(String token, GameRoom gameRoom){
        // 验证房间号格式是否正确
        String roomId = String.valueOf(gameRoom.getExternalRoomId());
        if(StringUtil.isBlank(roomId)) return new JsonResult().normalExceptionAsString("请输入房间号");
        if(roomId.length() != 6) return new JsonResult().normalExceptionAsString("请输入正确的房间号");
        gameRoomService.insert(token, gameRoom);
        return new JsonResult().successful(gameRoom);
    }

    @GetMapping("/getRoomList")
    public JsonArrayResult<GameRoomDetailInfo> getRoomList(String token){
        List<GameRoomDetailInfo> list = gameRoomService.getRoomList(token);
        return new JsonArrayResult<>(list);
    }

    @GetMapping("/getAllRoomList")
    public JsonArrayResult<GameRoomDetailInfo> getAllRoomList(){
        List<GameRoomDetailInfo> list = gameRoomService.getAllRoomList();

        String[] parentAreaNameList = {"两人麻将", "四人两房", "三人两房", "血战到底"};
        Double[] priceList = {0.1, 1D, 2D, 0.1};


        for (int i = 0; i < 100; i++) {
            Random rand = new Random();
            int randNum = rand.nextInt(4) + 1;
            GameRoomDetailInfo roomDetailInfo = new GameRoomDetailInfo();
            roomDetailInfo.setRoomId(i);
            long roomCode = 0;
            try {

                roomCode = Long.valueOf(com.panda.game.management.utils.RandomUtils.generateNumberString(6));
            } catch (Exception e) {

            }
            roomDetailInfo.setName(parentAreaNameList[randNum - 1]);
            roomDetailInfo.setRoomCode(roomCode);
            roomDetailInfo.setIsOwner(0);
            roomDetailInfo.setExternalRoomId(0);
            roomDetailInfo.setMaxPersonCount(4);
            roomDetailInfo.setStatus(4);

            /*randNum = rand.nextInt(3) + 1;*/
            if(randNum-1 == 0){
                roomDetailInfo.setPersonCount(2);
            }else if(randNum-1 == 1){
                roomDetailInfo.setPersonCount(4);
            }else if(randNum-1 == 2){
                roomDetailInfo.setPersonCount(3);
            }else if(randNum-1 == 3){
                roomDetailInfo.setPersonCount(4);
            }
            roomDetailInfo.setStatus(2);
            roomDetailInfo.setParentPrice(priceList[randNum-1]);

            list.add(roomDetailInfo);
        }
        return new JsonArrayResult<>(list);
    }

    @PostMapping("/disband")
    public JsonResult disband(String token, GameRoom gameRoom){
        synchronized (lock){
            gameRoomService.disband(token, gameRoom);
        }
        return JsonResult.successful();
    }

    @PostMapping("/closeAccounts")
    public JsonResult closeAccounts(String token, Long roomCode, Double grade){
        synchronized(lock){
            gameRoomFacadeService.closeAccounts(token, roomCode, grade);
        }
        return JsonResult.successful();
    }

    @PostMapping("/join")
    public JsonResult join(String token, GameRoom gameRoom){
        gameRoomService.checkRoomExpire(gameRoom.getRoomCode());
        gameRoomService.join(token, gameRoom);
        return JsonResult.successful();
    }

    @GetMapping("/getLimitRoom")
    public JsonResult<GameRoomDetailInfo> getLimitRoom(String parentAreaId, String subareasId){
        GameRoomDetailInfo model =  gameRoomService.getLimitRoom(parentAreaId, subareasId);
        if(model == null) return new JsonResult<>().normalExceptionAsString("没有匹配到合适的房间");
        return new JsonResult<>().successful(model);
    }

    @GetMapping("/getPersonCount")
    public JsonResult<Integer> getPersonCount(String roomCode){
        gameRoomService.checkRoomExpire(Long.valueOf(roomCode));
        Integer count = gameRoomService.getPersonCount(roomCode);
        return new JsonResult<>().successful(count);
    }
}
