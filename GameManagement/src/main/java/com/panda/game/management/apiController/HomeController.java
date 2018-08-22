/***
 * @pName management
 * @name HomeController
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.apiController;

import com.panda.game.management.biz.*;
import com.panda.game.management.entity.Constant;
import com.panda.game.management.entity.JsonArrayResult;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.db.Subareas;
import com.panda.game.management.entity.db.Users;
import com.panda.game.management.entity.db.Withdraw;
import com.panda.game.management.entity.param.PayParam;
import com.panda.game.management.entity.resp.GroupInformation;
import com.panda.game.management.exception.MsgException;
import com.panda.game.management.facade.FinanceFacadeService;
import com.panda.game.management.facade.UserFacadeService;
import com.panda.game.management.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    @Autowired
    private SubareaService subareaService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private GameRoomService gameRoomService;
    @Autowired
    private PayService payService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private FinanceFacadeService financeFacadeService;

    @GetMapping("/getLevelSubareas")
    public JsonArrayResult<Subareas> getLevelSubareas(){
        List<Subareas> list = subareaService.getLevelSubareas();
        return new JsonArrayResult<>(list);
    }

    @GetMapping("/getSubareas")
    public JsonArrayResult<Subareas> getSubareas(Integer subareaId){
        List<Subareas> list = subareaService.getSubareas(subareaId);
        return new JsonArrayResult<>(list);
    }

    @GetMapping("/getGroupInformation")
    public JsonResult<GroupInformation> getGroupInformation(){
        GroupInformation groupInformation= dictionaryService.getGroupInformation();
        return new JsonResult<>().successful(groupInformation);
    }

    @PostMapping("/getRoomCard")
    public JsonResult getRoomCard(String token, Users users){
        gameRoomService.getRoomCard(token, users);
        return JsonResult.successful();
    }

    @PostMapping("/recharge")
    public JsonResult recharge(String token, Double amount){
        // 加载用户信息
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) JsonResult.failing();
        String userId = map.get("userId");
        if(userId == null || userId.isEmpty()) throw new MsgException("身份校验失败");

        // 转账
        PayParam payParam = new PayParam();
        payParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
        payParam.setToUid(Integer.valueOf(userId));
        payParam.setAmount(amount);
        payService.transfer(payParam);
        return JsonResult.successful();
    }

    @PostMapping("/withdraw")
    public JsonResult withdraw(String token, Double amount){
        financeFacadeService.addWithdraw(token, amount);
        return JsonResult.successful();
    }

    @GetMapping("/getWithdrawAmount")
    public JsonResult<Double> getWithdrawAmount(String token){
        // 加载用户信息
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) JsonResult.failing();
        String userId = map.get("userId");
        if(userId == null || userId.isEmpty()) throw new MsgException("身份校验失败");

        // 查询可用余额
        Double withdrawAmount = payService.getWithdrawAmount(Integer.valueOf(userId));
        return new JsonResult().successful(withdrawAmount);
    }
}
