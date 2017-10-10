package com.demo.base.entity

import org.apache.commons.net.ftp.FTP

/**
 * Ftp连接信息类
 */
data class FtpConfigVO(
        var ftpHost: String? = null,
        var ftpPort: Int = 0,
        var ftpUserName: String? = null,
        var ftpPassword: String? = null,
        var remotePath: String? = null,
        var type: Int = FTP.ASCII_FILE_TYPE,
        var encoding: String = "UTF-8",
        var timeout: Int = 30000) {
    override fun toString(): String {
        return StringBuilder().append("ftp://").append(ftpUserName).append("@").append(ftpHost).append(":").append(ftpPort)
                .toString()
    }

}