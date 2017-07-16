package com.powerbridge.bssp.schedule.base;

/**
 * Created by 宋轲 on 2017/6/21.
 */
public class ExceptionConstant {

    //ok
    public final static int OK = 1;
    //参数错误
    public final static int PARAMETER_ERROR = -40001;
    //参数大于最大长度
    public final static int PARAMETER_MORE_THAN_RANGE = -40002;
    //参数小于最小长度
    public final static int PARAMETER_LESS_THAN_RANGE = -40003;
    //类名不存在或执行表达式错误
    public final static int CLASS_NOT_FIND_OR_CRON_ERROR = -40004;
    //任务不存在
    public final static int JOB_NOT_FIND = -40005;
    //任务已经存在
    public final static int JOB_ALREADY_EXIST = -40006;
}
