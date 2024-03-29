package com.lodz.android.corekt.anko

/**
 * Boolean型扩展类
 * Created by zhouL on 2018/7/12.
 */

/** 替代java三目运算符（condition ? a : b）为 condition.then { a } ?: b */
infix fun <T> Boolean.then(param: () -> T): T? = if (this) param() else null

/** 若布尔型方法为null，则返回false */
fun Boolean?.check(): Boolean = this ?: false

/** 将布尔型转为Int，0为false，1为true */
fun Boolean.toInt(): Int = if (this) 1 else 0

/** 将Int转为布尔型，[trueCode]为返回true的数字，默认为1 */
fun Int.toBoolean(trueCode: Int = 1): Boolean = this == trueCode