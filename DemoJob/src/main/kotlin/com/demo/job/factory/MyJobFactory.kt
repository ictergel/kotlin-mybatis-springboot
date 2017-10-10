package com.demo.job.factory

import org.quartz.spi.TriggerFiredBundle
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.quartz.AdaptableJobFactory
import org.springframework.stereotype.Component

@Component
class MyJobFactory : AdaptableJobFactory() {
    @Autowired
    private val capableBeanFactory: AutowireCapableBeanFactory? = null

    @Throws(Exception::class)
    override fun createJobInstance(bundle: TriggerFiredBundle): Any {
        // 调用父类的方法
        val jobInstance = super.createJobInstance(bundle)
        // 进行注入
        capableBeanFactory!!.autowireBean(jobInstance)
        return jobInstance
    }
}