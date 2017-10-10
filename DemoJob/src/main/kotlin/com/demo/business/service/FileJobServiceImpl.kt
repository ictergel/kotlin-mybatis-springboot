package com.demo.business.service

import com.demo.business.entity.UserVO
import com.demo.common.service.CommonDBService
import org.joda.time.DateTime
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

class FileJobServiceImpl : Job {
    private val logger4j: Logger = LoggerFactory.getLogger(FileJobServiceImpl::class.java)
    @Autowired
    private var commonDBService: CommonDBService? = null

    @Throws(JobExecutionException::class)
    override fun execute(context: JobExecutionContext) {
        logger4j.info("AAAA: now " + DateTime.now().toString("HH:mm:ss") + commonDBService!!.selectTotalCount(UserVO()))
    }
}