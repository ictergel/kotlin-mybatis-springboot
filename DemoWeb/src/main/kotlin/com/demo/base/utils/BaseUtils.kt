package com.demo.base.utils

import org.springframework.util.ResourceUtils
import java.io.InputStream
import java.util.*

object BaseUtils {

    fun getJobProperties(): Map<String, String> {
        val propFlies = Properties()
        var inputStream: InputStream? = null
        try {
            println("======================" + ResourceUtils.getURL("classpath"))
            println("======================" + ResourceUtils.getURL("classpath:config"))
            inputStream = ResourceUtils.getURL("classpath:config/db.properties").openStream()
            propFlies.load(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (inputStream != null) {
                inputStream.close()
            }
        }
        return propFlies as Map<String, String>
    }
}