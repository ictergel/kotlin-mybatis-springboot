package com.demo.web

import com.demo.base.entity.BaseVO
import com.demo.base.utils.DateTimeUtil
import com.demo.base.utils.EncryptDecryptUtil
import org.joda.time.DateTime
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

fun main(args: Array<String>) {

    println(DateTimeUtil.FORMAT_ONE)
    println(DateTimeUtil.FORMAT_TWO)
    println(DateTimeUtil.FORMAT_THREE)
    println(DateTimeUtil.FORMAT_FOUR)
    println(DateTimeUtil.FORMAT_FIVE)
    println(DateTimeUtil.FORMAT_SIX)
    println(DateTimeUtil.FORMAT_SEVEN)
    println(DateTimeUtil.FORMAT_EIGHT)
    println(DateTimeUtil.FORMAT_NINE)
    println(DateTimeUtil.FORMAT_TEN)

    println(DateTimeUtil.stringToDate("2017-07-12 12:00:00", DateTimeUtil.FORMAT_EIGHT))
    println(DateTimeUtil.timeSub(DateTime.now(),DateTime.now()))
    println(DateTimeUtil.getDaysOfMonth("2017","12"))
    println(DateTimeUtil.dayDiff(DateTime.now(),DateTime.now()))
    println(DateTimeUtil.getConstellation("1988-05-18"))
    println(DateTimeUtil.isOldFile("1988-05-18"))
    println(DateTimeUtil.isOldFile("1988-05-18",DateTime.now()))
    println(DateTimeUtil.getHourStartAndEnd("1988-05-18"))
    println(DateTimeUtil.getDayStartAndEnd("1988-05-18"))
    println(DateTimeUtil.getMonthStartAndEnd("1988-05-18"))
    println(DateTimeUtil.getMinJobTime())


    println(DateTimeUtil.stepMinDate("1988-05-18"))


    val test ="测试text_1234"
    println(test)
    println("MD5:" + EncryptDecryptUtil.SHA(test, EncryptDecryptUtil.KEY_MD5)!!)
    println("SHA-256:" + EncryptDecryptUtil.SHA(test, EncryptDecryptUtil.KEY_SHA256)!!)
    println("SHA-512:" + EncryptDecryptUtil.SHA(test, EncryptDecryptUtil.KEY_SHA512)!!)
    println("getBASE64:" + EncryptDecryptUtil.getBase64(test)!!)
    println("getFromBASE64:" + EncryptDecryptUtil.getFromBase64(EncryptDecryptUtil.getBase64(test))!!)

    val aes = EncryptDecryptUtil.AES()
    aes.generateKey()
    val file = File("C:/asmis/bak/temp/pri.key")
    val inPut = FileInputStream(file)
    val file1 = File("C:/asmis/bak/temp/pub.key")
    val out = FileOutputStream(file1)
    aes.encrypt(inPut, out)
    aes.decrypt(inPut, out)

    val keyPair = EncryptDecryptUtil.generateKeyPair()
    //用公钥加密
    val srcBytes = test.toByteArray()
    val resultBytes = EncryptDecryptUtil.encrypt(keyPair.public as RSAPublicKey, srcBytes)
    //用私钥解密
    val decBytes = EncryptDecryptUtil.decrypt(keyPair.private as RSAPrivateKey, resultBytes)
    println("原文: " + test)
    println("加密: " + String(resultBytes!!))
    println("解密: " + String(decBytes!!))

//    println(ProcessUtil.execCommand("dir"))
//    println(ProcessUtil.execShell("dir"))

    println(BaseVO().exportFlag)

}