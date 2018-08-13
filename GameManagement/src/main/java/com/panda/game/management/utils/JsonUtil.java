/***
 * @pName proback
 * @name JsonUtil
 * @user HongWei
 * @date 2018/8/7
 * @desc
 */
package com.panda.game.management.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

public class JsonUtil {

    public static String getJson(Object obj){
        String str = JSON.toJSONString(obj);
        return str.replace("\"", "");
    }

    public static <T> List<T> getModelAsList(String jsonString, Class<T> clazz){
        return JSON.parseArray(jsonString, clazz);
    }

    public static <T> T getModel(String jsonString, Class<T> clazz){
        try{
            return JSON.parseObject(jsonString, clazz);
        }catch (Exception e){
            System.err.println(e.toString());
            return null;
        }
    }
}