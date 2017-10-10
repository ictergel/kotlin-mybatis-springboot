package com.demo.job.config

import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.impl.triggers.CronTriggerImpl

object ScheduleUtil {
    var scheduler: Scheduler? = null
    var triggerSet: MutableSet<CronTriggerImpl> = mutableSetOf()
    var jobDetailSet: MutableSet<JobDetail> = mutableSetOf()
}