package com.powerbridge.bssp.schedule.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

/**
 * Created by 宋轲 on 2017/7/11.
 */
@Controller
public class ScheduleJob implements Job {

    private final static Logger logger = LoggerFactory.getLogger(ScheduleJob.class);

    @Autowired
    private RestTemplate restTemplate;//此处restTemplate注入不成功

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("ScheduleJob:{}.{} 开始启动",context.getJobDetail().getKey().getGroup(),context.getJobDetail().getKey().getName());
        System.out.println("=======================================");
        System.out.println(restTemplate);
        Object result = restTemplate.getForObject(context.getJobDetail().getKey().getName(),String.class);
        System.out.println(result);
        System.out.println("=======================================");
        logger.info("ScheduleJob:{}.{} 执行完成",context.getJobDetail().getKey().getGroup(),context.getJobDetail().getKey().getName());
    }
}
