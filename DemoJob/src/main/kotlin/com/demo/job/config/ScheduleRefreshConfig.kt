package com.demo.job.config

import com.demo.base.utils.BaseUtils
import com.demo.job.consts.JobConsts
import org.quartz.SchedulerException
import org.quartz.impl.triggers.CronTriggerImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Configuration
@Component
class ScheduleRefreshConfig {
    private val logger4j: Logger = LoggerFactory.getLogger(ScheduleRefreshConfig::class.java)
    @Scheduled(fixedRate = 15000) // 每隔15s查库，并根据查询结果决定是否重新设置定时任务
    @Throws(SchedulerException::class)
    fun scheduleUpdateCronTrigger() {
        val jobMap = BaseUtils.getJobProperties()
        ScheduleUtil.triggerSet.forEach {
            var currentCron = it.cronExpression// 当前Trigger使用的
            val searchCron = jobMap[it.key.toString().replace(JobConsts.SCHEDULED_TRIGGER_GROUP + ".", "")]
            if (searchCron.isNullOrEmpty() || currentCron == searchCron) {
                logger4j.info("表达式一致，所以不需要刷新任务")
                // 如果当前使用的cron表达式和从数据库中查询出来的cron表达式一致，则不刷新任务
            } else {
                it.cronExpression = searchCron
                val trigger = ScheduleUtil.scheduler!!.getTrigger(it.key) as CronTriggerImpl
                trigger.cronExpression = searchCron
                ScheduleUtil.scheduler!!.rescheduleJob(it.key, trigger)
            }
        }
    }
}