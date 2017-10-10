package com.demo.job.listener

import com.demo.job.config.SchedulerConfig
import com.demo.job.factory.MyJobFactory
import org.quartz.SchedulerException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.scheduling.quartz.SchedulerFactoryBean


@Configuration
class SchedulerListener : ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private val myJobFactory: MyJobFactory? = null
    @Autowired
    var schedulerConfig: SchedulerConfig? = null

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        try {
            schedulerConfig!!.scheduleJobs(schedulerFactoryBean())
        } catch (e: SchedulerException) {
            e.printStackTrace()
        }
    }

    @Bean
    fun schedulerFactoryBean(): SchedulerFactoryBean {
        val factory = SchedulerFactoryBean()
        factory.setOverwriteExistingJobs(true)
        // 延时启动
        factory.setStartupDelay(1)
        // 自定义Job Factory，用于Spring注入
        factory.setJobFactory(myJobFactory!!)
        return factory
//        return SchedulerFactoryBean()
    }
}