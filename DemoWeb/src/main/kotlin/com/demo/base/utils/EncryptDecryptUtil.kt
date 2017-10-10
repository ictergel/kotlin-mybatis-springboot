package com.demo.base.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder
import java.io.*
import java.security.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import javax.crypto.*

object EncryptDecryptUtil {
    private val logger: Logger = LoggerFactory.getLogger(EncryptDecryptUtil::class.java)

    private val KEY_SIZE = 1024
    private val KEY_RSA = "RSA"
    val KEY_MD5 = "MD5"
    val KEY_SHA256 = "SHA-256"
    val KEY_SHA512 = "SHA-512"
    val KEY_AES = "AES"
    /**
     * 加密
     *
     * @param publicKey
     * @param srcBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    @Throws(NoSuchAlgorithmException::class, NoSuchPaddingException::class, InvalidKeyException::class,
            IllegalBlockSizeException::class, BadPaddingException::class)
    fun encrypt(publicKey: RSAPublicKey?, srcBytes: ByteArray): ByteArray? {
        if (publicKey != null) {
            //Cipher负责完成加密或解密工作，基于RSA
            val cipher = Cipher.getInstance(KEY_RSA)
            //根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            return cipher.doFinal(srcBytes)
        }
        return null
    }

    /**
     * 解密
     *
     * @param privateKey
     * @param srcBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    @Throws(NoSuchAlgorithmException::class, NoSuchPaddingException::class, InvalidKeyException::class,
            IllegalBlockSizeException::class, BadPaddingException::class)
    fun decrypt(privateKey: RSAPrivateKey?, srcBytes: ByteArray?): ByteArray? {
        if (privateKey != null) {
            //Cipher负责完成加密或解密工作，基于RSA
            val cipher = Cipher.getInstance(KEY_RSA)
            //根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            return cipher.doFinal(srcBytes!!)
        }
        return null
    }


    /**
     * BASE64Decoder 加密
     *
     * @param key
     * @return
     */
    fun getBase64(key: String): String? {
        var result: String? = null
        var b: ByteArray? = null
        try {
            b = key.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            logger.error(e.message)
            e.printStackTrace()
        }

        if (b != null) {
            result = BASE64Encoder().encode(b)
        }
        return result
    }

    /**
     * BASE64Decoder 解密
     */
    fun getFromBase64(key: String?): String? {
        if (key == null) {
            return null
        }
        var result: String? = null
        val decoder = BASE64Decoder()
        try {
            result = java.lang.String(decoder.decodeBuffer(key), "UTF-8").toString()
        } catch (e: Exception) {
            logger.error(e.message)
            e.printStackTrace()
        }
        return result
    }

    /**
     * 字符串 SHA 加密
     *
     * @param strText
     * @param strType
     * @return
     */
    fun SHA(strText: String?, strType: String): String? {
        // 返回值
        var strResult: String? = null

        // 是否是有效字符串
        if (strText != null && strText.isNotEmpty()) {
            try {
                // SHA 加密开始
                // 创建加密对象 并传入加密类型
                val messageDigest = MessageDigest.getInstance(strType)
                // 传入要加密的字符串
                messageDigest.update(strText.toByteArray())
                // 得到 byte 类型结果
                val byteBuffer = messageDigest.digest()

                // 将 byte 转换为 string
                val strHexString = StringBuffer()
                // 遍历 byte buffer
                for (i in byteBuffer.indices) {
                    val hex = Integer.toHexString(0xff and byteBuffer[i].toInt())
                    if (hex.length == 1) {
                        strHexString.append('0')
                    }
                    strHexString.append(hex)
                }
                // 得到返回結果
                strResult = strHexString.toString()
            } catch (e: NoSuchAlgorithmException) {
                logger.error(e.message)
                e.printStackTrace()
            }

        }

        return strResult
    }

    /**
     * 生成秘钥对
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    @Throws(NoSuchAlgorithmException::class)
    fun generateKeyPair(): KeyPair {
        val pairGen = KeyPairGenerator.getInstance(KEY_RSA)
        val random = SecureRandom()
        pairGen.initialize(KEY_SIZE, random)
        return pairGen.generateKeyPair()
    }


    /**
     * AES对称加密,解密
     */
    class AES {

        var key: Key? = null

        /**
         * 生成AES对称秘钥
         *
         * @throws NoSuchAlgorithmException
         */
        @Throws(NoSuchAlgorithmException::class)
        fun generateKey() {
            val keygen = KeyGenerator.getInstance(KEY_AES)
            val random = SecureRandom()
            keygen.init(random)
            this.key = keygen.generateKey()
        }


        /**
         * 加密
         *
         * @param input
         * @param output
         * @throws InvalidKeyException
         * @throws ShortBufferException
         * @throws IllegalBlockSizeException
         * @throws BadPaddingException
         * @throws NoSuchAlgorithmException
         * @throws NoSuchPaddingException
         * @throws IOException
         */
        @Throws(InvalidKeyException::class, ShortBufferException::class, IllegalBlockSizeException::class,
                BadPaddingException::class, NoSuchAlgorithmException::class, NoSuchPaddingException::class, IOException::class)
        fun encrypt(input: InputStream, output: OutputStream) {
            this.crypt(input, output, Cipher.ENCRYPT_MODE)
        }

        /**
         * 解密
         *
         * @param input
         * @param output
         * @throws InvalidKeyException
         * @throws ShortBufferException
         * @throws IllegalBlockSizeException
         * @throws BadPaddingException
         * @throws NoSuchAlgorithmException
         * @throws NoSuchPaddingException
         * @throws IOException
         */
        @Throws(InvalidKeyException::class, ShortBufferException::class, IllegalBlockSizeException::class,
                BadPaddingException::class, NoSuchAlgorithmException::class, NoSuchPaddingException::class, IOException::class)
        fun decrypt(input: InputStream, output: OutputStream) {
            this.crypt(input, output, Cipher.DECRYPT_MODE)
        }

        /**
         * 实际的加密解密过程
         *
         * @param input
         * @param output
         * @param mode
         * @throws IOException
         * @throws ShortBufferException
         * @throws IllegalBlockSizeException
         * @throws BadPaddingException
         * @throws NoSuchAlgorithmException
         * @throws NoSuchPaddingException
         * @throws InvalidKeyException
         */
        @Throws(IOException::class, ShortBufferException::class, IllegalBlockSizeException::class,
                BadPaddingException::class, NoSuchAlgorithmException::class, NoSuchPaddingException::class, InvalidKeyException::class)
        private fun crypt(input: InputStream, output: OutputStream, mode: Int) {
            val cipher = Cipher.getInstance(KEY_AES)
            cipher.init(mode, this.key)

            val blockSize = cipher.blockSize
            val outputSize = cipher.getOutputSize(blockSize)
            val inBytes = ByteArray(blockSize)
            var outBytes = ByteArray(outputSize)

            var inLength = 0
            var more = true
            while (more) {
                inLength = input.read(inBytes)
                if (inLength == blockSize) {
                    val outLength = cipher.update(inBytes, 0, blockSize, outBytes)
                    output.write(outBytes, 0, outLength)
                } else {
                    more = false
                }
            }
            outBytes = if (inLength > 0)
                cipher.doFinal(inBytes, 0, inLength)
            else
                cipher.doFinal()
            output.write(outBytes)
            output.flush()
        }

    }


    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val test = "测试text_1234"
        println(test)
        println("MD5:" + SHA(test, KEY_MD5)!!)
        println("SHA-256:" + SHA(test, KEY_SHA256)!!)
        println("SHA-512:" + SHA(test, KEY_SHA512)!!)
        println("getBASE64:" + getBase64(test)!!)
        println("getFromBASE64:" + getFromBase64(getBase64(test))!!)
        val aes = AES()
        aes.generateKey()
        val file = File("D:/demo/bak/temp/pri.key")
        val inPut = FileInputStream(file)
        val file1 = File("C:/demo/bak/temp/pub.key")
        val output = FileOutputStream(file1)
        aes.encrypt(inPut, output)
        aes.decrypt(inPut, output)
        val keyPair = generateKeyPair()
        //用公钥加密
        val srcBytes = test.toByteArray()
        val resultBytes = encrypt(keyPair.public as RSAPublicKey, srcBytes)
        //用私钥解密
        val decBytes = decrypt(keyPair.private as RSAPrivateKey, resultBytes)
        println("原文: " + test)
        println("加密: " + String(resultBytes!!))
        println("解密: " + String(decBytes!!))
    }
}