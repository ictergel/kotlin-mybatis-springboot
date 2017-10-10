package com.demo.test

import com.demo.base.entity.FtpConfigVO
import com.demo.base.utils.FtpUtil


fun main(args: Array<String>) {

    val fileFtpConfig = FtpConfigVO()

    FtpUtil.createDirectory(fileFtpConfig,"/test")
    FtpUtil.removeDirectory(fileFtpConfig,"/test")
    FtpUtil.existDirectory(fileFtpConfig,"/test")
    FtpUtil.deleteFile(fileFtpConfig,"/test")
    FtpUtil.changeDirectory(fileFtpConfig,"/test")
    FtpUtil.getFileList(fileFtpConfig,"/test","ddd")
    FtpUtil.listRegexDirectory(fileFtpConfig,"/test")
    FtpUtil.listRegexFiles(fileFtpConfig,"/test")
    FtpUtil.rename(fileFtpConfig,"/test","bbb")
    FtpUtil.uploadFile(fileFtpConfig,"/test")
    FtpUtil.download(fileFtpConfig,"/test","bbb")
    FtpUtil.readConfigFileForFTP(fileFtpConfig,"/test","")
    FtpUtil.listRegexDirectory(fileFtpConfig,"/test")

}