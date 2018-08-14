/***
 * @pName mi-ocr-web-captcha
 * @name GlobalExceptionHandler
 * @user HongWei
 * @date 2018/6/25
 * @desc
 */
package com.panda.game.management.handler;

import com.panda.game.management.entity.JsonResult;
import com.panda.game.management.exception.InfoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeoutException;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    final static Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(value = InfoException.class)
    public JsonResult preException(HttpServletRequest request,
                                   InfoException exception) throws Exception {
        logger.error(String.format("自定义异常消息: %s",exception.toString()));
        return  new JsonResult().exceptionAsString(exception.getMsg() == null ? "错误" : exception.getMsg());
    }



    @ExceptionHandler(value = TimeoutException.class)
    public JsonResult preException(HttpServletRequest request,
                                 TimeoutException exception) throws Exception {
        logger.error(String.format("超时异常消息: %s",exception.toString()));
        return new JsonResult().failingAsString("超时");
    }

    @ExceptionHandler(value = RuntimeException.class)
    public JsonResult preException(HttpServletRequest request,
                                 RuntimeException exception) throws Exception {
        logger.error(String.format("运行时异常: %s",exception.toString()));
        return new JsonResult().failingAsString("异常");
    }

    @ExceptionHandler(value = Exception.class)
    public JsonResult preException(HttpServletRequest request,
                                 Exception exception) throws Exception {
        logger.error(String.format("全局异常: %s",exception.toString()));
        return new JsonResult().failingAsString("故障");
    }
}

