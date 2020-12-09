package com.lodz.android.agiledevkt.modules.annotation

import android.app.Activity
import android.view.View
import com.lodz.android.corekt.anko.injectAs
import com.lodz.android.corekt.security.AES
import com.lodz.android.corekt.utils.ReflectUtils

/**
 * 注解测试帮助类
 * @author zhouL
 * @date 2019/5/28
 */
object AnnotationTestUtils {

    /** 加密注解解析 */
    fun <T> injectEncryption(any: T) {
        any?.injectAs<EncryptionAES> { c, obj, inject, field ->
            val key = if (inject.key.length == 16) inject.key else AES.KEY
            val value = ReflectUtils.getFieldValue(c, obj, field.name)
            val source = if (value is String) value else ""
            ReflectUtils.setFieldValue(c, obj, field.name, AES.encrypt(source, key) ?: "")
        }
    }

    /** 控件绑定注解解析 */
    inline fun <reified T : Activity> bind(activity: T) {
        activity.injectAs<BindViews, T>(activity) { c, obj, inject, field ->
            val id = inject.id
            val view = obj.findViewById<View>(id) ?: throw IllegalStateException("View ID $id for '${c.name}' not found.")
            ReflectUtils.setFieldValue(c, obj, field.name, view)
        }
    }
}