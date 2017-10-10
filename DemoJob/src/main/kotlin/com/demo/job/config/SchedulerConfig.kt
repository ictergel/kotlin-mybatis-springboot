package com.demo.job.config

import com.demo.base.utils.BaseUtils
import com.demo.business.service.DayJobServiceImpl
import com.demo.business.service.FileJobServiceImpl
import com.demo.job.consts.JobConsts
import org.quartz.*
import org.quartz.impl.triggers.CronTriggerImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Component


@Component
class SchedulerConfig {
    private val logger4j: Logger = LoggerFactory.getLogger(SchedulerConfig::class.java)
    @Throws(SchedulerException::class)
    fun scheduleJobs(schedulerFactoryBean: SchedulerFactoryBean) {
        val scheduler = schedulerFactoryBean.scheduler
        val jobMap = BaseUtils.getJobProperties()
        ScheduleUtil.scheduler = scheduler
        val fileJob = jobMap[JobConsts.FILE_JOB_STR] ?: ""
        val dayJob = jobMap[JobConsts.DAY_JOB_STR] ?: ""
        if (!(fileJob.isNullOrEmpty())) {
            startFileJob(scheduler, fileJob)
        }
        if (!(dayJob.isNullOrEmpty())) {
            startDayJob(scheduler, dayJob)
        }
    }

    @Throws(SchedulerException::class)
    private fun startFileJob(scheduler: Scheduler, cronExpression: String) {
        val jobDetail = JobBuilder.newJob(FileJobServiceImpl::class.java).withIdentity(JobConsts.FILE_JOB_STR, JobConsts.SCHEDULED_TRIGGER_GROUP).build()
        val scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
        val cronTrigger = TriggerBuilder.newTrigger().withIdentity(JobConsts.FILE_JOB_STR, JobConsts.SCHEDULED_TRIGGER_GROUP)
                .withSchedule(scheduleBuilder).build()
        scheduler.scheduleJob(jobDetail, cronTrigger)
        ScheduleUtil.jobDetailSet.add(jobDetail)
        ScheduleUtil.triggerSet.add(scheduler.getTrigger(cronTrigger.key) as CronTriggerImpl)
        logger4j.info(cronTrigger.key.toString())
    }

    @Throws(SchedulerException::class)
    private fun startDayJob(scheduler: Scheduler, cronExpression: String) {
        val jobDetail = JobBuilder.newJob(DayJobServiceImpl::class.java).withIdentity(JobConsts.DAY_JOB_STR, JobConsts.SCHEDULED_TRIGGER_GROUP).build()
        val scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
        val cronTrigger = TriggerBuilder.newTrigger().withIdentity(JobConsts.DAY_JOB_STR, JobConsts.SCHEDULED_TRIGGER_GROUP).withSchedule(scheduleBuilder).build()
        scheduler.scheduleJob(jobDetail, cronTrigger)
        ScheduleUtil.jobDetailSet.add(jobDetail)
        ScheduleUtil.triggerSet.add(scheduler.getTrigger(cronTrigger.key) as CronTriggerImpl)
        logger4j.info(cronTrigger.key.toString())
    }





}