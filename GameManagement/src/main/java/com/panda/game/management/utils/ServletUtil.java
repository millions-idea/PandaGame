/***
 * @pName mi-ocr-web-app
 * @name ServletUtil
 * @user HongWei
 * @date 2018/7/29
 * @desc
 */
package com.panda.game.management.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtil {

    private static ServletRequestAttributes getServletRequestAttributes(){
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static HttpServletRequest getRequest(){
        return getServletRequestAttributes().getRequest();
    }

    public static HttpServletResponse getResponse(){
        return getServletRequestAttributes().getResponse();
    }
}
