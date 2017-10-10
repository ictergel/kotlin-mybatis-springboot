package com.demo.base.utils

import com.demo.base.entity.FtpConfigVO
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.*
import java.net.SocketException
import java.util.*

object FtpUtil{

    private val logger: Logger = LoggerFactory.getLogger(FtpUtil::class.java)

    /**
     * 根据连接信息连接ftp
     * @param config
     * @return
     * @throws Exception
     */
    private fun connectServer(config: FtpConfigVO): FTPClient? {
        if (config.ftpHost.isNullOrEmpty()) {
            throw Exception("host is null. check on FtpConfigVO.")
        }
        val ftpClient = FTPClient()
        try {
            ftpClient.controlEncoding = config.encoding
            ftpClient.connect(config.ftpHost, config.ftpPort)
            val replyCode = ftpClient.replyCode
            if (FTPReply.isPositiveCompletion(replyCode)) {
                if (ftpClient.login(config.ftpUserName, config.ftpPassword)) {
                    ftpClient.setFileType(config.type)
                    ftpClient.enterLocalActiveMode()
                    ftpClient.remoteAddress
                    ftpClient.soTimeout = config.timeout
                    if (config.remotePath != null) {
                        ftpClient.changeWorkingDirectory(config.remotePath)
                    }
                    return ftpClient
                } else {
                    logger.error("Incorrect username or password. check on FtpConfigVO.")
                    ftpClient.disconnect()
                }
            } else {
                logger.error("FTP server refused connection.")
                ftpClient.disconnect()
            }
        } catch (e: Exception) {
            if (ftpClient.isConnected) {
                try {
                    ftpClient.disconnect()
                } catch (ex: Exception) {
                    logger.error(e.message)
                    e.printStackTrace()
                }
            }
            logger.error(e.message)
            e.printStackTrace()
        }
        return null
    }

    /**
     * 关闭Ftp连接
     * @param ftpClient
     * @throws Exception
     */
    private fun closeServer(ftpClient: FTPClient) {
        try {
            if (ftpClient.isConnected) {
                ftpClient.disconnect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            logger.error(e.message)
        }
    }

    /**
     * Ftp上创建目录
     * @param config
     * @param pathName
     * @return
     * @throws Exception
     */
    fun createDirectory(config: FtpConfigVO, pathName: String): Boolean {
        var ftpClient: FTPClient? = FTPClient()
        return try {
            ftpClient = connectServer(config)
            ftpClient!!.makeDirectory(pathName)
        } catch (e: Exception) {
            e.printStackTrace()
            logger.error(e.message)
            false
        } finally {
            closeServer(ftpClient ?: FTPClient())
        }
    }

    /**
     * Ftp上删除目录
     *
     * @param config
     * @param path
     * @return
     * @throws Exception
     */
    fun removeDirectory(config: FtpConfigVO, path: String): Boolean {
        var ftpClient: FTPClient? = FTPClient()
        return try {
            ftpClient = connectServer(config)
            ftpClient!!.removeDirectory(path)
        } catch (e: Exception) {
            e.printStackTrace()
            logger.error(e.message)
            false
        } finally {
            closeServer(ftpClient ?: FTPClient())
        }
    }

    /**
     * 判断Ftp是否存在目录
     * @param config
     * @param path
     * @return
     * @throws Exception
     */
    fun existDirectory(config: FtpConfigVO, path: String): Boolean {
        var ftpClient: FTPClient? = FTPClient()
        var flag = false
        try {
            ftpClient = connectServer(config)
            val ftpFileArr = ftpClient!!.listFiles()
            for (ftpFile in ftpFileArr) {
                if (ftpFile.isDirectory && ftpFile.name.equals(path, ignoreCase = true)) {
                    flag = true
                    break
                }
            }
            return flag
        } catch (e: Exception) {
            logger.error(e.message)
        } finally {
            closeServer(ftpClient ?: FTPClient())
        }
        return false
    }

    /**
     * 删除Ftp指定路径的文件
     * @param config
     * @param pathName
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun deleteFile(config: FtpConfigVO, pathName: String): Boolean {
        var ftpClient: FTPClient? = FTPClient()
        return try {
            ftpClient = connectServer(config)
            ftpClient!!.deleteFile(pathName)
        } catch (e: Exception) {
            logger.error(e.message)
            e.printStackTrace()
            false
        } finally {
            closeServer(ftpClient ?: FTPClient())
        }
    }

    /**
     * 更改Ftp的工作目录
     * @param config
     * @param path
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun changeDirectory(config: FtpConfigVO, path: String): Boolean {
        var ftpClient: FTPClient? = FTPClient()
        return try {
            ftpClient = connectServer(config)
            ftpClient!!.changeWorkingDirectory(path)
        } catch (e: Exception) {
            logger.error(e.message)
            e.printStackTrace()
            false
        } finally {
            closeServer(ftpClient ?: FTPClient())
        }

    }

    /**
     * 获取Ftp上指定路径、扩展名的文件数组
     *
     * @param config
     * @param path
     * @param type
     * @return
     * @throws Exception
     */
    fun getFileList(config: FtpConfigVO, path: String, type: String): List<String> {
        var ftpClient: FTPClient? = FTPClient()
        try {
            ftpClient = connectServer(config)
            val ftpFiles = ftpClient!!.listFiles(path)
            val retList = ArrayList<String>()
            if (ftpFiles == null || ftpFiles.isEmpty()) {
                return retList
            }
            ftpFiles.filter { it.isFile && it.name.indexOf(type) > 0 }.mapTo(retList) { it.name }
            return retList
        } catch (e: Exception) {
            logger.error(e.message)
            return emptyList()
        } finally {
            closeServer(ftpClient ?: FTPClient())
        }

    }

    /**
     * 关键字过滤Ftp上满足条件的目录数组
     * @param config
     * @param regex
     * @return
     * @throws Exception
     */
    fun listRegexDirectory(config: FtpConfigVO, regex: String): List<String> {
        var ftpClient: FTPClient? = FTPClient()
        return try {
            ftpClient = connectServer(config)
            val pathList = ArrayList<String>()
            val ftpFileArr = ftpClient!!.listFiles()
            ftpFileArr.filter { it.isDirectory && it.name.contains(regex) }.mapTo(pathList) { it.name }
            pathList
        } catch (e: Exception) {
            logger.error(e.message)
            emptyList()
        } finally {
            closeServer(ftpClient ?: FTPClient())
        }
    }

    /**
     * 关键字过滤Ftp上满足条件的文件数组
     *
     * @param config
     * @param regex
     * @return
     * @throws Exception
     */
    fun listRegexFiles(config: FtpConfigVO, regex: String): List<String> {
        var ftpClient: FTPClient? = FTPClient()
        return try {
            ftpClient = connectServer(config)
            val ftpFileArr = ftpClient!!.listFiles()
            val pathList = ftpFileArr.filter { !it.isDirectory && it.name.contains(regex) }.map { it.name }
            pathList
        } catch (e: Exception) {
            logger.error(e.message)
            emptyList()
        } finally {
            closeServer(ftpClient ?: FTPClient())
        }

    }

    /**
     * Ftp上文件或目录重命名
     * @param config
     * @param oldName
     * @param newName
     * @return
     */
    fun rename(config: FtpConfigVO, oldName: String, newName: String): Boolean {
        var ftpClient: FTPClient? = FTPClient()
        try {
            ftpClient = connectServer(config)
            return ftpClient!!.rename(oldName, newName)
        } catch (e: Exception) {
            logger.error(e.message)
            e.printStackTrace()
        } finally {
            closeServer(ftpClient ?: FTPClient())
        }

        return false
    }

    /**
     * 向Ftp指定路径上传文件
     *
     * @param config
     * @param savePath
     * @return
     * @throws Exception
     */
    fun uploadFile(config: FtpConfigVO, savePath: String): Boolean {
        var ftpClient: FTPClient? = FTPClient()
        var flag = false
        var iStream: InputStream? = null
        try {
            ftpClient = connectServer(config)
            val file = File(savePath)
            iStream = FileInputStream(file)
            ftpClient!!.changeWorkingDirectory(config.remotePath)
            flag = ftpClient.storeFile(java.lang.String(file.name.toByteArray(charset("UTF-8")), "ISO-8859-1")
                    .toString(), iStream)
            iStream.close()
        } catch (e: Exception) {
            logger.error(e.message)
            e.printStackTrace()
        } finally {
            if (iStream != null) {
                iStream.close()
            }
            closeServer(ftpClient ?: FTPClient())
        }
        return flag
    }

    /**
     * 从Ftp远程路径下载文件至本地路径
     * @param config
     * @param remoteFileName
     * @param localFileName
     * @return
     * @throws Exception
     */
    fun download(config: FtpConfigVO, remoteFileName: String, localFileName: String): Boolean {
        var ftpClient: FTPClient? = FTPClient()
        try {
            ftpClient = connectServer(config)
            var flag: Boolean
            var hasFile = true
            var path: String?
            val outfile = File(localFileName)
            path = localFileName.substring(0, localFileName.lastIndexOf(File.separator))
            if (!File(path).exists()) {
                File(path).mkdirs()
            }
            if (!outfile.exists()) {
                hasFile = outfile.createNewFile()
            }
            if (!hasFile) {
                flag = false
                return flag
            }
            var oStream: OutputStream? = null
            try {
                oStream = FileOutputStream(outfile)
                flag = ftpClient!!.retrieveFile(remoteFileName, oStream)
            } catch (e: IOException) {
                throw e
            } finally {
                if (oStream != null) {
                    oStream.close()
                }
            }
            return flag
        } catch (e: Exception) {
            logger.error(e.message)
            e.printStackTrace()
            return false
        } finally {
            closeServer(ftpClient ?: FTPClient())
        }
    }

    /**
     * 删除指定Ftp下指定路径的文件
     *
     * @param config
     * @param remoteFileName
     * @return
     * @throws Exception
     */
    fun delete(config: FtpConfigVO, remoteFileName: String): Boolean {
        var ftpClient: FTPClient? = FTPClient()
        var result = false
        try {
            ftpClient = connectServer(config)
            result = ftpClient!!.deleteFile(remoteFileName)
        } catch (e: Exception) {
            logger.error(e.message)
            e.printStackTrace()
        } finally {
            closeServer(ftpClient ?: FTPClient())
        }
        return result
    }

    /**
     * 读取指定Ftp指定目录的文件
     *
     * @param config
     * @param ftpPath
     * @param fileName
     * @return
     */
    fun readConfigFileForFTP(config: FtpConfigVO, ftpPath: String, fileName: String): ByteArray {
        var ftpClient: FTPClient? = FTPClient()
        var inputStream: InputStream? = null
        var content = byteArrayOf()
        try {
            ftpClient = connectServer(config)
            ftpClient!!.setFileType(FTPClient.BINARY_FILE_TYPE)
            ftpClient.enterLocalPassiveMode()
            ftpClient.changeWorkingDirectory(ftpPath)
            inputStream = ftpClient.retrieveFileStream(fileName)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: SocketException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (inputStream != null) {
            try {
                val out = ByteArrayOutputStream(1024)
                val temp = ByteArray(1024)
                var size: Int
                while (true) {
                    size = inputStream.read(temp)
                    if (size != -1) {
                        out.write(temp, 0, size)
                    } else {
                        break
                    }
                }
                inputStream.close()
                ftpClient!!.reply
                content = out.toByteArray()
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        closeServer(ftpClient ?: FTPClient())
        return content
    }
}