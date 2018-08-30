/***
 * @pName management
 * @name RoomController
 * @user HongWei
 * @date 2018/8/28
 * @desc
 */
package com.panda.game.management.controller;

import com.panda.game.management.biz.IGameRoomService;
import com.panda.game.management.biz.IMessageService;
import com.panda.game.management.biz.IRoomCardService;
import com.panda.game.management.biz.ISettlementService;
import com.panda.game.management.entity.JsonArrayResult;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.db.Accounts;
import com.panda.game.management.entity.db.Messages;
import com.panda.game.management.entity.db.RoomCard;
import com.panda.game.management.entity.db.Settlement;
import com.panda.game.management.entity.dbExt.RoomCardDetailInfo;
import com.panda.game.management.entity.dbExt.SettlementDetailInfo;
import com.panda.game.management.entity.resp.AccountsResp;
import com.panda.game.management.facade.GameRoomFacadeService;
import com.panda.game.management.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/management/room")
public class RoomController extends BaseController {
    @Autowired
    private ISettlementService settlementService;
    @Autowired
    private GameRoomFacadeService gameRoomFacadeService;
    @Autowired
    private IRoomCardService roomCardService;
    @Autowired
    private IMessageService messageService;

    @GetMapping("/close-accounts")
    public String  closeAccounts(){
        return "room/close-accounts";
    }

    /**
     * 结算审核 韦德 2018年8月29日11:42:31
     * @return
     */
    @GetMapping("/getSettlementLimit")
    @ResponseBody
    public JsonArrayResult<SettlementDetailInfo> getSettlementLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime){
        Integer count = 0;
        List<SettlementDetailInfo> list = settlementService.getSettlementLimit(page, limit, condition, state, beginTime, endTime);
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0,list);
        if (StringUtil.isBlank(condition)
                && StringUtil.isBlank(beginTime)
                && StringUtil.isBlank(endTime)
                && (state == null || state == 0)){
            count = settlementService.getSettlementCount();
        }else{
            count = settlementService.getSettlementLimitCount(condition, state, beginTime, endTime);
        }
        jsonArrayResult.setCount(count);
        return jsonArrayResult;
    }

    @GetMapping("/edit-settlement")
    public String editSettlement(SettlementDetailInfo param, final Model model){
        model.addAttribute("model", param);
        List<SettlementDetailInfo> memberList = settlementService.getMemberList(param.getRoomCode());
        model.addAttribute("list", memberList);
        return "room/edit-settlement";
    }

    @GetMapping("/getRoomMemberList")
    @ResponseBody
    public JsonArrayResult<SettlementDetailInfo> getRoomMemberList(Long roomCode){
        List<SettlementDetailInfo> memberList = settlementService.getMemberList(roomCode);
        return new JsonArrayResult<>(memberList);
    }

    @PostMapping("/editGrade")
    @ResponseBody
    public JsonResult editGrade(Integer userId, Long grade, Long roomCode){
        settlementService.editGrade(userId, grade, roomCode);
        return JsonResult.successful();
    }

    @PostMapping("/executeCloseAccounts")
    @ResponseBody
    public JsonResult executeCloseAccounts(Long roomCode){
        gameRoomFacadeService.closeAccounts(roomCode);
        return JsonResult.successful();
    }

    @GetMapping("/room-card")
    public String roomCard(){
        return "room/room-card";
    }

    @GetMapping("/getRoomCardLimit")
    @ResponseBody
    public JsonArrayResult<RoomCardDetailInfo> getRoomCardLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime){
        Integer count = 0;
        List<RoomCardDetailInfo> list = roomCardService.getRoomCardLimit(page, limit, condition, state, beginTime, endTime);
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0,list);
        if (StringUtil.isBlank(condition)
                && StringUtil.isBlank(beginTime)
                && StringUtil.isBlank(endTime)
                && (state == null || state == 0)){
            count = roomCardService.getRoomCardCount();
        }else{
            count = roomCardService.getRoomCardLimitCount(condition, state, beginTime, endTime);
        }
        jsonArrayResult.setCount(count);
        return jsonArrayResult;
    }


    @PostMapping("/recharge")
    @ResponseBody
    public JsonResult recharge(RoomCard roomCard){
        roomCardService.recharge(roomCard);
        messageService.pushMessage(new Messages(null, roomCard.getUserId(), "您的30张熊猫麻将房卡已到账！", 0, new Date()));
        return JsonResult.successful();
    }

    @PostMapping("/pass")
    @ResponseBody
    public JsonResult pass(RoomCard roomCard){
        roomCardService.pass(roomCard);
        messageService.pushMessage(new Messages(null, roomCard.getUserId(), "领取熊猫麻将30张房卡失败！", 0, new Date()));
        return JsonResult.successful();
    }
}
