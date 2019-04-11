package com.lodz.android.corekt.security

import java.security.MessageDigest

/**
 * MD5信息摘要
 * Created by zhouL on 2018/6/25.
 */
object MD5 {

    /** 将内容[content]转为信息摘要 */
    @JvmStatic
    fun md(content: String): String? {
        // 用来将字节转换成 16 进制表示的字符
        val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

        try {
            val strTemp = content.toByteArray()
            val mdTemp = MessageDigest.getInstance("MD5")
            mdTemp.update(strTemp)
            val tmp = mdTemp.digest()// MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            val strs = CharArray(16 * 2)
            // 所以表示成 16 进制需要 32 个字符
            var k = 0 // 表示转换结果中对应的字符位置
            for (i in 0..15) {// 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                val b: Byte = tmp[i]// 取第 i 个字节
                strs[k++] = hexDigits[b.toInt().ushr(4) and 0xf] // 取字节中高 4 位的数字转换,
                strs[k++] = hexDigits[b.toInt() and 0xf]// 取字节中低 4 位的数字转换
            }
            return String(strs).toUpperCase()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}