package com.powerbridge.bssp.schedule.service;

import com.powerbridge.bssp.schedule.base.CustomException;
import com.powerbridge.bssp.schedule.domain.TaskInfo;

import java.util.List;

/**
 * Created by 宋轲 on 2017/7/11.
 */
public interface TaskService {

    public List<TaskInfo> list();

    public void save(TaskInfo info) throws CustomException;

    public void edit(TaskInfo info) throws CustomException;

    public void delete(String jobName, String jobGroup) throws CustomException;

    public void pause(String jobName, String jobGroup) throws CustomException;

    public void resume(String jobName, String jobGroup) throws CustomException;

    public TaskInfo view(String jobName, String jobGroup) throws CustomException;
}
