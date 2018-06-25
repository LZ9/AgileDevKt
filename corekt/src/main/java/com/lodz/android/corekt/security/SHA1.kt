package com.lodz.android.corekt.security

import java.lang.StringBuilder
import java.security.MessageDigest


/**
 * SHA1信息摘要
 * Created by zhouL on 2018/6/25.
 */
object SHA1 {

    /** 将内容[content]转为信息摘要 */
    fun md(content: String): String? {
        try {
            val digest = MessageDigest.getInstance("SHA-1")
            digest.update(content.toByteArray())
            val messageDigest = digest.digest()
            // Create Hex String
            val hexString = StringBuilder()
            // 字节数组转换为 十六进制 数
            for (md in messageDigest) {
                val shaHex = Integer.toHexString(md.toInt() and 0xFF)
                if (shaHex.length < 2) {
                    hexString.append(0)
                }
                hexString.append(shaHex)
            }
            return hexString.toString().toUpperCase()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}