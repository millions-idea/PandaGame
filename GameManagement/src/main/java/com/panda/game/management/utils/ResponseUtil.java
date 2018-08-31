package com.panda.game.management.utils;

import com.panda.game.management.entity.JsonResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ResponseUtil {


    public static void render(HttpServletResponse response, String contentType, String text){
        response.setContentType(contentType);
        try {
            response.getWriter().write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**发送json*/
    public static void renderJson(HttpServletResponse response, String text){
        response.addHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

        render(response, "application/json;charset=UTF-8", text);
    }

    public static void renderJson(HttpServletResponse response, JsonResult result){
        response.addHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

        render(response, "application/json;charset=UTF-8", JsonUtil.getJsonNotEscape(result));
    }

    /**发送xml*/
    public static void renderXml(HttpServletResponse response, String text){
        render(response, "text/xml;charset=UTF-8", text);
    }

    /**发送text*/
    public static void renderText(HttpServletResponse response, String text){
        render(response, "application/plain;charset=UTF-8", text);
    }

}
