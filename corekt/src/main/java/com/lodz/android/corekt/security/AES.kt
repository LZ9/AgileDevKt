package com.lodz.android.corekt.security

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * AES加密
 * Created by zhouL on 2018/6/22.
 */
object AES {

    /** AES java-javascript 只支持部填充摸索 */
    private const val FORMAT = "AES/CBC/NoPadding"
    /** AES 加密key，必须为16位 */
    const val KEY = "com.lodz.android"
    /** AES 偏移量，必须为16位 */
    private const val IV = "123456abc2345678"

    /** 加密原始数据[dataBytes]，秘钥[key]必须为16位，填充方式[transformation]默认"AES/CBC/NoPadding"，偏移量[iv]默认"123456abc2345678" */
    @JvmStatic
    @JvmOverloads
    fun encrypt(data: String, key: String, transformation: String = FORMAT, iv: String = IV): String? = encrypt(data.toByteArray(), key, transformation, iv)

    /** 加密原始数据[dataBytes]，秘钥[key]必须为16位，填充方式[transformation]默认"AES/CBC/NoPadding"，偏移量[iv]默认"123456abc2345678" */
    @JvmStatic
    @JvmOverloads
    fun encrypt(dataBytes: ByteArray, key: String, transformation: String = FORMAT, iv: String = IV): String? {
        if (dataBytes.isEmpty() || key.isEmpty()) {
            return null
        }
        if (key.length != 16) {
            return null
        }

        try {
            val cipher = Cipher.getInstance(transformation)
            val blockSize = cipher.blockSize

            var plaintextLength = dataBytes.size
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize))
            }

            val plaintext = ByteArray(plaintextLength)
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.size)

            val keyspec = SecretKeySpec(key.toByteArray(), "AES")
            val ivspec = IvParameterSpec(iv.toByteArray())

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec)
            val encrypted = cipher.doFinal(plaintext)
            return Base64.encodeToString(encrypted, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 解密密文[data]，秘钥[key]必须为16位，填充方式[transformation]默认"AES/CBC/NoPadding"，偏移量[iv]默认"123456abc2345678" */
    @JvmStatic
    @JvmOverloads
    fun decrypt(data: String, key: String, transformation: String = FORMAT, iv: String = IV): String? {
        if (data.isEmpty() || key.isEmpty()) {
            return null
        }
        if (key.length != 16) {
            return null
        }
        try {
            val decrypt = Base64.decode(data, Base64.NO_WRAP)

            val cipher = Cipher.getInstance(transformation)
            val keyspec = SecretKeySpec(key.toByteArray(), "AES")
            val ivspec = IvParameterSpec(iv.toByteArray())

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec)

            val originalString: String? = String(cipher.doFinal(decrypt))
            return originalString?.trim { it <= ' ' } ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


}