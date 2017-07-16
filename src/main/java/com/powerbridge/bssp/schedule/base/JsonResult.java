package com.powerbridge.bssp.schedule.base;

import java.util.Date;

/**
 * Created by 宋轲 on 2017/6/15.
 */
public class JsonResult {

    /**
     * 结果集
     */
    private Object data;

    /**
     * 状态码
     */
    private Integer code;

    private String message;

    /**
     * 用户信息
     */
//    private String userMsg;

    /**
     * 开发者信息
     */
//    private String developerMsg;

    /**
     * 时间戳
     */
    private Long timestamp;

    public JsonResult() {
        this.code = ExceptionConstant.OK;
        this.timestamp = new Date().getTime();
    }

    public JsonResult(Integer code) {
        this.code = code;
        this.message = CustomException.getMessage(code);
        this.timestamp = new Date().getTime();
    }

    public JsonResult(Object data) {
        this.data = data;
        this.code = ExceptionConstant.OK;
        this.timestamp = new Date().getTime();
    }

    public JsonResult(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = new Date().getTime();
    }

    public JsonResult(Integer code, Object data){
        this.data = data;
        this.code = code;
        this.message = CustomException.getMessage(code);
        this.timestamp = new Date().getTime();
    }

    public JsonResult(Object data, Integer code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.timestamp = new Date().getTime();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
