package com.powerbridge.bssp.schedule.service.TaskServiceImpl;

import com.powerbridge.bssp.schedule.base.CustomException;
import com.powerbridge.bssp.schedule.base.ExceptionConstant;
import com.powerbridge.bssp.schedule.domain.TaskInfo;
import com.powerbridge.bssp.schedule.jobs.ScheduleJob;
import com.powerbridge.bssp.schedule.service.TaskService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by 宋轲 on 2017/7/11.
 */
@Service
public class TaskServiceImpl implements TaskService {

	private final static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
	
	@Autowired
	private Scheduler scheduler;
	
	/**
	 * 所有任务列表
	 */
	@Override
	public List<TaskInfo> list(){
		List<TaskInfo> list = new ArrayList<>();
		try {
			for(String groupJob: scheduler.getJobGroupNames()){
				for(JobKey jobKey: scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(groupJob))){
					List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
					for (Trigger trigger: triggers) {
						Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
						JobDetail jobDetail = scheduler.getJobDetail(jobKey);
						
						String cronExpression = "", createTime = "";
						
						if (trigger instanceof CronTrigger) {
				            CronTrigger cronTrigger = (CronTrigger) trigger;
				            cronExpression = cronTrigger.getCronExpression();
				            createTime = cronTrigger.getDescription();
				        }
						TaskInfo info = new TaskInfo();
						info.setJobName(jobKey.getName());
						info.setJobGroup(jobKey.getGroup());
						info.setJobDescription(jobDetail.getDescription());
						info.setJobStatus(triggerState.name());
						info.setCronExpression(cronExpression);
						info.setCreateTime(createTime);
						list.add(info);
					}					
				}
			}			
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 保存定时任务
	 * @param info
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void save(TaskInfo info) throws CustomException {
		String jobName = info.getJobName(), 
			   jobGroup = info.getJobGroup(), 
			   cronExpression = info.getCronExpression(),
			   jobDescription = info.getJobDescription(),
			   createTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		try {
			if (checkExists(jobName, jobGroup)) {
		        throw new CustomException(ExceptionConstant.JOB_ALREADY_EXIST);
		    }
			
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
			
			CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withDescription(createTime).withSchedule(schedBuilder).build();
		
		
			/*Class<? extends Job> clazz = (Class<? extends Job>)Class.forName(jobName);*/
			JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(jobKey).withDescription(jobDescription).build();
			scheduler.scheduleJob(jobDetail, trigger);
			logger.info("===> save success, jobGroup:{},jobName:{}", jobGroup ,jobName);
		} catch (SchedulerException e/*| ClassNotFoundException e*/) {
			throw new CustomException(ExceptionConstant.CLASS_NOT_FIND_OR_CRON_ERROR);
		}
	}
	
	/**
	 * 修改定时任务
	 * @param info
	 */
	@Override
	public void edit(TaskInfo info) throws CustomException {
		String jobName = info.getJobName(), 
			   jobGroup = info.getJobGroup(), 
			   cronExpression = info.getCronExpression(),
			   jobDescription = info.getJobDescription(),
			   createTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		try {
			if (!checkExists(jobName, jobGroup)) {
				throw new CustomException(ExceptionConstant.JOB_NOT_FIND);
			}
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
	        JobKey jobKey = new JobKey(jobName, jobGroup);
	        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
	        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withDescription(createTime).withSchedule(cronScheduleBuilder).build();
	        
	        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
	        jobDetail.getJobBuilder().withDescription(jobDescription);
	        HashSet<Trigger> triggerSet = new HashSet<>();
	    	triggerSet.add(cronTrigger);
	        
	    	scheduler.scheduleJob(jobDetail, triggerSet, true);
		} catch (SchedulerException e) {
			throw new CustomException(ExceptionConstant.CLASS_NOT_FIND_OR_CRON_ERROR);
		}
	}
	
	/**
	 * 删除定时任务
	 * @param jobName
	 * @param jobGroup
	 */
	@Override
	public void delete(String jobName, String jobGroup) throws CustomException {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
			if (checkExists(jobName, jobGroup)) {
				scheduler.pauseTrigger(triggerKey);
			    scheduler.unscheduleJob(triggerKey);
			    logger.info("===> delete success, triggerKey:{}", triggerKey);
			}
		} catch (SchedulerException e) {
			throw new CustomException(e.getMessage());
		}
	}
	
	/**
	 * 暂停定时任务
	 * @param jobName
	 * @param jobGroup
	 */
	@Override
	public void pause(String jobName, String jobGroup) throws CustomException {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		try {
			if (checkExists(jobName, jobGroup)) {
				scheduler.pauseTrigger(triggerKey);
			    logger.info("===> Pause success, triggerKey:{}", triggerKey);
			}
		} catch (SchedulerException e) {
			throw new CustomException(e.getMessage());
		}
	}
	
	/**
	 * 重新开始任务
	 * @param jobName
	 * @param jobGroup
	 */
	@Override
	public void resume(String jobName, String jobGroup) throws CustomException {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        
        try {
			if (checkExists(jobName, jobGroup)) {
				scheduler.resumeTrigger(triggerKey);
			    logger.info("===> Resume success, triggerKey:{}",triggerKey);
			}
		} catch (SchedulerException e) {
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	public TaskInfo view(String jobName, String jobGroup) throws CustomException {
		TaskInfo taskInfo = new TaskInfo();
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		JobKey jobKey = new JobKey(jobName, jobGroup);
		try {
			if (checkExists(jobName, jobGroup)) {
				Trigger trigger = scheduler.getTrigger(triggerKey);
				JobDetail jobDetail = scheduler.getJobDetail(jobKey);
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());

				String cronExpression = "", createTime = "";
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					cronExpression = cronTrigger.getCronExpression();
					createTime = cronTrigger.getDescription();
				}
				taskInfo.setJobName(jobName);
				taskInfo.setJobGroup(jobGroup);
				taskInfo.setJobDescription(jobDetail.getDescription());
				taskInfo.setJobStatus(triggerState.name());
				taskInfo.setCronExpression(cronExpression);
				taskInfo.setCreateTime(createTime);
				logger.info("===> view success, triggerKey:{}",triggerKey);
			}
		}catch (SchedulerException e){
			throw new CustomException(e.getMessage());
		}
		return taskInfo;
	}
	
	/**
	 * 验证是否存在
	 * @param jobName
	 * @param jobGroup
	 * @throws SchedulerException
	 */
	private boolean checkExists(String jobName, String jobGroup) throws SchedulerException{
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		return scheduler.checkExists(triggerKey);
	}
}
