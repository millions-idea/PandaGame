/***
 * @pName management
 * @name JsonArrayResult
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.entity;

import java.util.ArrayList;
import java.util.List;

public class JsonArrayResult<T> extends JsonResult{

    public JsonArrayResult(List<T> data) {
        super.setCode(200);
        if(data == null || data.isEmpty()){
            this.data = new ArrayList<>();
        }else{
            this.data = data;
        }
    }

    public JsonArrayResult(Integer code, List<T> data) {
        super.setCode(code);
        this.data = data;
    }

    public JsonArrayResult(Integer code, String msg) {
        super.setCode(code);
        super.setMsg(msg);
    }

    private Integer count;

    public Integer getCount() {
        if(count == null || count == 0) return this.data.size();
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * 失败JSON 韦德 2018年8月13日13:03:35
     * @return
     */
    public static JsonArrayResult failing(){
        return new JsonArrayResult(500, "fail");
    }
}