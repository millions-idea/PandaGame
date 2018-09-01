/***
 * @pName management
 * @name MemberController
 * @user HongWei
 * @date 2018/8/31
 * @desc
 */
package com.panda.game.management.controller;

import com.panda.game.management.biz.IPermissionRelationService;
import com.panda.game.management.biz.IUserService;
import com.panda.game.management.entity.JsonArrayResult;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.db.Users;
import com.panda.game.management.entity.dbExt.UserDetailInfo;
import com.panda.game.management.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/management/member")
public class MemberController extends BaseController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IPermissionRelationService permissionRelationService;

    @GetMapping("/index")
    public String  index(){
        return "member/index";
    }

    /**
     * 会员列表 韦德 2018年8月29日11:42:31
     * @return
     */
    @GetMapping("/getMemberLimit")
    @ResponseBody
    public JsonArrayResult<UserDetailInfo> getMemberLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime){
        Integer count = 0;
        List<UserDetailInfo> list = userService.getLimit(page, limit, condition, state, beginTime, endTime);
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, list);
        if (StringUtil.isBlank(condition)
                && StringUtil.isBlank(beginTime)
                && StringUtil.isBlank(endTime)
                && (state == null || state == 0)){
            count = userService.getCount();
        }else{
            count = userService.getLimitCount(condition, state, beginTime, endTime);
        }
        jsonArrayResult.setCount(count);
        return jsonArrayResult;
    }

    /**
     * 更新密码
     * @return
     */
    @PostMapping("/updatePassword")
    @ResponseBody
    public JsonResult updatePassword(Users users){
        userService.updatePassword(users);
        return JsonResult.successful();
    }

    /**
     * 删除用户
     * @return
     */
    @PostMapping("/updateAvailability")
    @ResponseBody
    public JsonResult updateAvailability(Users users){
        userService.updateAvailability(users);
        return JsonResult.successful();
    }


    /**
     * 冻结
     * @return
     */
    @PostMapping("/updateEnable")
    @ResponseBody
    public JsonResult updateEnable(Users users){
        userService.updateEnable(users);
        return JsonResult.successful();
    }


    /**
     * 设置权限
     * @param userId
     * @param roleName
     * @return
     */
    @PostMapping("/changeRole")
    @ResponseBody
    public JsonResult changeRole(Integer userId, String roleName){
        permissionRelationService.replace(userId, roleName);
        return JsonResult.successful();
    }
}
