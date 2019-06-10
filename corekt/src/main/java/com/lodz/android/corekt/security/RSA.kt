package com.lodz.android.corekt.security

import android.util.Base64
import java.io.ByteArrayOutputStream
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

/**
 * RSA安全编码组件
 * Created by zhouL on 2018/6/29.
 */
object RSA {

    /** 非对称加密密钥算法 */
    private const val KEY_ALGORITHM = "RSA"
    /** 加密模式 */
    private const val CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding"

    /** 获取公钥的KEY */
    private const val PUBLIC_KEY = "RSAPublicKey"
    /** 获取私钥的KEY */
    private const val PRIVATE_KEY = "RSAPrivateKey"

    /** RSA密钥长度 默认1024位， 密钥长度必须是64的倍数， 范围在512至65536位之间。  */
    private const val KEY_SIZE = 512
    /** RSA最大加密明文大小  */
    private const val MAX_ENCRYPT_BLOCK = KEY_SIZE / 8 - 11
    /** RSA最大解密密文大小  */
    private const val MAX_DECRYPT_BLOCK = KEY_SIZE / 8

    /** 初始化密钥 */
    @JvmStatic
    fun initKey(): Pair<RSAPublicKey, RSAPrivateKey> {
        // 实例化密钥对生成器
        val keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM)
        // 初始化密钥对生成器
        keyPairGen.initialize(KEY_SIZE)
        // 生成密钥对
        val keyPair = keyPairGen.generateKeyPair()
        return Pair(keyPair.public as RSAPublicKey, keyPair.private as RSAPrivateKey)
    }

    /** 初始化密钥，用base64编码 */
    @JvmStatic
    fun initKeyBase64(): Pair<String, String> {
        // 实例化密钥对生成器
        val keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM)
        // 初始化密钥对生成器
        keyPairGen.initialize(KEY_SIZE)
        // 生成密钥对
        val keyPair = keyPairGen.generateKeyPair()
        // 公钥
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey

        return Pair(
            Base64.encodeToString(publicKey.encoded, Base64.NO_WRAP),
            Base64.encodeToString(privateKey.encoded, Base64.NO_WRAP)
        )
    }

    /** 用公钥加密数据 */
    @JvmStatic
    fun encryptByPublicKey(data: String, key: String): String? {
        if (key.isEmpty() || data.isEmpty()) {
            return null
        }
        try {
            val encryptByPublicKey = encryptByPublicKey(data.toByteArray(), Base64.decode(key, Base64.NO_WRAP))
            return Base64.encodeToString(encryptByPublicKey, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 公钥[key]加密内容[data] */
    private fun encryptByPublicKey(data: ByteArray, key: ByteArray): ByteArray {
        // 取得公钥
        val publicKey = KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(X509EncodedKeySpec(key))

        // 对数据加密
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val inputLen = data.size
        ByteArrayOutputStream().use { baos: ByteArrayOutputStream ->
            var offSet = 0
            var cache: ByteArray
            var i = 0
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                cache = if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK)
                } else {
                    cipher.doFinal(data, offSet, inputLen - offSet)
                }
                baos.write(cache, 0, cache.size)
                i++
                offSet = i * MAX_ENCRYPT_BLOCK
            }
            return baos.toByteArray()
        }
    }

    /** 私钥[key]解密数据[data] */
    @JvmStatic
    fun decryptByPrivateKey(data: String, key: String): String? {
        if (key.isEmpty() || data.isEmpty()) {
            return null
        }
        try {
            return String(decryptByPrivateKey(Base64.decode(data, Base64.NO_WRAP), Base64.decode(key, Base64.NO_WRAP)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 私钥[key]解密数据[data] */
    private fun decryptByPrivateKey(data: ByteArray, key: ByteArray): ByteArray {
        // 生成私钥
        val privateKey = KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(PKCS8EncodedKeySpec(key))

        // 对数据解密
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val inputLen = data.size
        ByteArrayOutputStream().use { baos: ByteArrayOutputStream ->
            var offSet = 0
            var cache: ByteArray
            var i = 0
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                cache = if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK)
                } else {
                    cipher.doFinal(data, offSet, inputLen - offSet)
                }
                baos.write(cache, 0, cache.size)
                i++
                offSet = i * MAX_DECRYPT_BLOCK
            }
            return baos.toByteArray()
        }
    }
}