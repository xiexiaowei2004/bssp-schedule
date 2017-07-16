package com.powerbridge.bssp.schedule.base;

/**
 * Created by 宋轲 on 2017/6/16.
 */
public class CustomException extends Exception {

    private static int code;

    /**
     * 获取异常信息
     * @param code
     * @return
     */
    public static String getMessage(int code){
        switch (code) {
            case ExceptionConstant.PARAMETER_ERROR :
                return "请求参数错误";
            case ExceptionConstant.PARAMETER_MORE_THAN_RANGE :
                return "参数大于最大长度";
            case ExceptionConstant.PARAMETER_LESS_THAN_RANGE :
                return "参数小于最小长度";
            case ExceptionConstant.CLASS_NOT_FIND_OR_CRON_ERROR:
                return "类名不存在或执行表达式错误";
            case ExceptionConstant.JOB_NOT_FIND:
                return "任务不存在";
            case ExceptionConstant.JOB_ALREADY_EXIST:
                return "任务已经存在";
            default:
                return "成功";
        }
    }

    public CustomException(String message) {
        super(message);
        this.code = ExceptionConstant.OK;
    }

    public CustomException(int code) {
        super(getMessage(code));
        this.code = code;
    }

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 获取异常状态码
     * @return
     */
    public static int getCode() {
        return code;
    }
}
