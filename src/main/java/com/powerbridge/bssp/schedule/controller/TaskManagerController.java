package com.powerbridge.bssp.schedule.controller;

import com.powerbridge.bssp.schedule.base.CustomException;
import com.powerbridge.bssp.schedule.base.ExceptionConstant;
import com.powerbridge.bssp.schedule.base.JsonResult;
import com.powerbridge.bssp.schedule.domain.TaskInfo;
import com.powerbridge.bssp.schedule.service.TaskService;
import org.quartz.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by 宋轲 on 2017/7/11.
 */
@CrossOrigin
@RestController
@RequestMapping("/schedule")
public class TaskManagerController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test")
    public JsonResult test() throws ClassNotFoundException {
        System.out.println(restTemplate);
        Object object = restTemplate.getForObject("http://localhost:9001/select",String.class);
        System.out.println(object);
        return new JsonResult(object);
    }

    /**
     * 列表查询
     * @return
     */
    @GetMapping("/list")
    public JsonResult list() {
        List<TaskInfo> infos = taskService.list();
        JsonResult jsonResult = new JsonResult(infos);
        return jsonResult;
    }

    /**
     * 添加任务
     * @param info
     * @return
     * @throws CustomException
     */
    @RequestMapping(value = "/list/save",method = RequestMethod.POST)
    public JsonResult save(TaskInfo info) throws CustomException {
        taskService.save(info);
        return new JsonResult(ExceptionConstant.OK);
    }

    /**
     * 修改任务
     * @param info
     * @return
     * @throws CustomException
     */
    @RequestMapping(value = "/list/edit",method = RequestMethod.POST)
    public JsonResult edit(TaskInfo info) throws CustomException{
        taskService.edit(info);
        return new JsonResult(ExceptionConstant.OK);
    }

    /**
     * 删除任务
     * @param jobName
     * @param jobGroup
     * @return
     * @throws CustomException
     */
    @RequestMapping(value="/list/delete/{jobName}/{jobGroup}", produces = "application/json; charset=UTF-8",method = RequestMethod.GET)
    public JsonResult delete(@PathVariable String jobName, @PathVariable String jobGroup) throws CustomException {
        validParam(jobName, jobGroup);
        taskService.delete(jobName, jobGroup);
        return new JsonResult(ExceptionConstant.OK);
    }

    /**
     * 暂停任务
     * @param jobName
     * @param jobGroup
     * @return
     * @throws CustomException
     */
    @RequestMapping(value="/list/pause/{jobName}/{jobGroup}", produces = "application/json; charset=UTF-8",method = RequestMethod.GET)
    public JsonResult pause(@PathVariable String jobName, @PathVariable String jobGroup) throws CustomException {
        validParam(jobName, jobGroup);
        taskService.pause(jobName, jobGroup);
        return new JsonResult(ExceptionConstant.OK);
    }

    /**
     * 重启任务
     * @param jobName
     * @param jobGroup
     * @return
     * @throws CustomException
     */
    @RequestMapping(value="/list/resume/{jobName}/{jobGroup}", produces = "application/json; charset=UTF-8",method = RequestMethod.GET)
    public JsonResult resume(@PathVariable String jobName, @PathVariable String jobGroup) throws CustomException {
        validParam(jobName, jobGroup);
        taskService.resume(jobName, jobGroup);
        return new JsonResult(ExceptionConstant.OK);
    }

    /**
     * 查看任务
     * @param jobName
     * @param jobGroup
     * @return
     * @throws CustomException
     */
    @RequestMapping(value="/list/view/{jobName}/{jobGroup}", produces = "application/json; charset=UTF-8",method = RequestMethod.GET)
    public JsonResult view(@PathVariable String jobName, @PathVariable String jobGroup)  throws CustomException {
        validParam(jobName, jobGroup);
        TaskInfo taskInfo = taskService.view(jobName, jobGroup);
        return new JsonResult(ExceptionConstant.OK,taskInfo);
    }

    /**
     * 参数校验
     * @param jobName
     * @param jobGroup
     * @throws CustomException
     */
    private void validParam(String jobName, String jobGroup) throws CustomException {
        if (StringUtils.isEmpty(jobName) || StringUtils.isEmpty(jobGroup)){
            throw new CustomException(ExceptionConstant.PARAMETER_ERROR);
        }
    }
}
