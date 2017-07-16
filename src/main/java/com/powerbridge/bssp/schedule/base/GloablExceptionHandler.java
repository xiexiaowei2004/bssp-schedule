package com.powerbridge.bssp.schedule.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * 自定义异常，404，500以json返回
 * Created by 宋轲 on 2017/6/15.
 */
@ResponseBody
@ControllerAdvice
public class GloablExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GloablExceptionHandler.class);

    @ExceptionHandler(value = CustomException.class)
    public Object customErrorHandler(Exception e, HttpServletRequest request){
        JsonResult result = new JsonResult(CustomException.getCode(),e.getMessage());
        logger.error("url: {}, errorCode: {}, errorMsg: {}", request.getRequestURL(), result.getCode(), e.getMessage());
        return result;
    }

    @ExceptionHandler(value = Exception.class)
    public Object defaultErrorHandler(Exception e, HttpServletRequest request){
        JsonResult result = null;
        if (e instanceof NoHandlerFoundException) {
            result = new JsonResult(HttpStatus.NOT_FOUND.value(),e.getMessage());
        } else {
            e.printStackTrace();
            result = new JsonResult(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
        logger.error("url: {}, errorCode: {}, errorMsg: {}", request.getRequestURL(), result.getCode(), e.getMessage());
        return result;
    }
}
