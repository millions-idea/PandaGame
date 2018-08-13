/***
 * @pName management
 * @name JsonResult
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.entity;

import com.alibaba.druid.support.json.JSONUtils;
import com.panda.game.management.utils.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JsonResult<T> {
    private Integer code;
    private String msg;

    public JsonResult() {
    }

    public JsonResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 成功JSON 韦德 2018年8月13日13:03:35
     * @param msg
     * @return
     */
    public JsonResult successful(T msg){
        return new JsonResult(200, JsonUtil.getJson(msg));
    }

    public JsonResult exception(T msg){
        return new JsonResult(300, JsonUtil.getJson(msg));
    }


    /**
     * 成功JSON 韦德 2018年8月13日13:03:35
     * @return
     */
    public static JsonResult successful(){
        return new JsonResult(200, "success");
    }

    /**
     * 失败JSON 韦德 2018年8月13日13:03:35
     * @param msg
     * @return
     */
    public JsonResult failing(T msg){
        return new JsonResult(500, JsonUtil.getJson(msg));
    }


    /**
     * 失败JSON 韦德 2018年8月13日13:03:35
     * @return
     */
    public static JsonResult failing(){
        return new JsonResult(500, "fail");
    }
}
