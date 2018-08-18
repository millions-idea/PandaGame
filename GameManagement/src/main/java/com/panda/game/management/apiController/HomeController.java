/***
 * @pName management
 * @name HomeController
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.apiController;

import com.panda.game.management.biz.DictionaryService;
import com.panda.game.management.biz.SubareaService;
import com.panda.game.management.entity.JsonArrayResult;
import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.entity.db.Subareas;
import com.panda.game.management.entity.resp.GroupInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    @Autowired
    private SubareaService subareaService;
    @Autowired
    private DictionaryService dictionaryService;

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
}
