package com.demo.business.service

import com.demo.base.entity.FtpConfigVO
import com.demo.business.entity.UserVO
import com.demo.common.service.CommonDBService
import org.joda.time.DateTime
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

class DayJobServiceImpl : Job {
    private val logger4j: Logger = LoggerFactory.getLogger(DayJobServiceImpl::class.java)
    @Autowired
    private var commonDBService: CommonDBService? = null
    @Autowired
    var fileFtpConfig: FtpConfigVO? = null

    @Autowired
    var logFtpConfig: FtpConfigVO? = null

    @Throws(JobExecutionException::class)
    override fun execute(context: JobExecutionContext) {
        logger4j.info("BBBB: now " + DateTime.now().toString("HH:mm:ss") + commonDBService!!.selectEntities(UserVO()))

        logger4j.info(fileFtpConfig!!.ftpHost + ":" + fileFtpConfig!!.ftpPort)

        logger4j.info(logFtpConfig!!.ftpHost + ":" + logFtpConfig!!.ftpPort)
    }
}