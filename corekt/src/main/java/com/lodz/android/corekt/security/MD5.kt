package com.lodz.android.corekt.security

import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.util.*

/**
 * MD5信息摘要
 * Created by zhouL on 2018/6/25.
 */
object MD5 {

    private val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

    /** 将文件[file]转为信息摘要 */
    @JvmStatic
    fun md(file: File): String? {
        if (!file.isFile){
            return null
        }
        val array = ByteArray(1024)
        FileInputStream(file).use {
            var len = 0
            while (len != -1) {
                len = it.read(array, 0, 1024)
            }
        }
        return md(array)
    }

    /** 将字符串[content]转为信息摘要 */
    @JvmStatic
    fun md(content: String): String? = md(content.toByteArray())

    /** 将Byte数组[array]转为信息摘要 */
    @JvmStatic
    fun md(array: ByteArray): String? {
        // 用来将字节转换成 16 进制表示的字符
        try {
            val mdTemp = MessageDigest.getInstance("MD5")
            mdTemp.update(array)
            val tmp = mdTemp.digest()// MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            val strs = CharArray(16 * 2)
            // 所以表示成 16 进制需要 32 个字符
            var k = 0 // 表示转换结果中对应的字符位置
            for (i in 0..15) {// 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                val b: Byte = tmp[i]// 取第 i 个字节
                strs[k++] = HEX_DIGITS[b.toInt().ushr(4) and 0xf] // 取字节中高 4 位的数字转换,
                strs[k++] = HEX_DIGITS[b.toInt() and 0xf]// 取字节中低 4 位的数字转换
            }
            return String(strs).toUpperCase(Locale.getDefault())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}