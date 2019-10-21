package com.lodz.android.corekt.anko

import java.lang.reflect.Field

/**
 * 注解扩展类
 * @author zhouL
 * @date 2019/5/29
 */

/** 类型[T]为注解类，方法[block]为注解操作 */
inline fun <reified T : Annotation> Any.inject(block: (c: Class<out Any>, obj: Any, inject: T, field: Field) -> Unit) {
    try {
        val cls = this::class.java
        val fields = cls.declaredFields
        for (field in fields) {
            if (!field.isAnnotationPresent(T::class.java)) {
                continue
            }
            val inject = field.getAnnotation(T::class.java)
            if (inject != null){
                block(cls, this, inject, field)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/** 类型[T]为注解类，[R]为对象类型，方法[block]为注解操作 */
inline fun <reified T : Annotation, reified R> injects(obj: R, block: (c: Class<out R>, obj: R, inject: T, field: Field) -> Unit) {
    try {
        val cls = R::class.java
        val fields = cls.declaredFields
        for (field in fields) {
            if (!field.isAnnotationPresent(T::class.java)) {
                continue
            }
            val inject = field.getAnnotation(T::class.java)
            if (inject != null){
                block(cls, obj, inject, field)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}