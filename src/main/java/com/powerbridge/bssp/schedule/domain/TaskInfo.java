package com.powerbridge.bssp.schedule.domain;

import com.powerbridge.bssp.schedule.base.BaseEntity;

import java.io.Serializable;

/**
 * 管理定时任务
 * Created by 宋轲 on 2017/7/11.
 */
public class TaskInfo extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -8054692082716173379L;

	/**任务名称*/
	private String jobName;
	
	/**任务分组*/
	private String jobGroup;
	
	/**任务描述*/
	private String jobDescription;
	
	/**任务状态*/
	private String jobStatus;
	
	/**任务表达式*/
	private String cronExpression;
	
	private String createTime;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}