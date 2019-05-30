package com.lodz.android.agiledevkt.modules.annotation

import com.lodz.android.corekt.security.AES

/**
 * AES加密注解
 * @author zhouL
 * @date 2019/5/28
 */

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class EncryptionAES(val key: String = AES.KEY)