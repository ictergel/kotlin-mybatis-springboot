package com.demo.base.config

import com.demo.base.entity.FtpConfigVO
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:config/ftp.properties")
class FtpConfig {

    @Value("\${fileFtpHost}")
    val fileFtpHost: String? = null

    @Value("\${fileFtpPort}")
    val fileFtpPort: String? = null

    @Value("\${fileFtpUserName}")
    val fileFtpUserName: String? = null

    @Value("\${fileFtpPassword}")
    val fileFtpPassword: String? = null

    @Value("\${fileFtpRemotePath}")
    val fileFtpRemotePath: String = ""




    @Bean("fileFtpConfig")     //声明其为Bean实例
    fun ftpConfigVO(): FtpConfigVO {
        val ftpConfigVO = FtpConfigVO()
        ftpConfigVO.ftpHost = this.fileFtpHost
        ftpConfigVO.ftpPort = (fileFtpPort ?: "0").toInt()
        ftpConfigVO.ftpUserName = fileFtpUserName
        ftpConfigVO.ftpPassword = fileFtpPassword
        ftpConfigVO.remotePath = fileFtpRemotePath
        return ftpConfigVO
    }


    @Value("\${logFtpHost}")
    val logFtpHost: String? = null

    @Value("\${logFtpPort}")
    val logFtpPort: String? = null

    @Value("\${logFtpUserName}")
    val logFtpUserName: String? = null

    @Value("\${logFtpPassword}")
    val logFtpPassword: String? = null

    @Value("\${logFtpRemotePath}")
    val logFtpRemotePath: String = ""

    @Bean("logFtpConfig")     //声明其为Bean实例
    fun logFtpConfigVO(): FtpConfigVO {
        val ftpConfigVO = FtpConfigVO()
        ftpConfigVO.ftpHost = this.logFtpHost
        ftpConfigVO.ftpPort = (logFtpPort ?: "0").toInt()
        ftpConfigVO.ftpUserName = logFtpUserName
        ftpConfigVO.ftpPassword = logFtpPassword
        ftpConfigVO.remotePath = logFtpRemotePath
        return ftpConfigVO
    }

}