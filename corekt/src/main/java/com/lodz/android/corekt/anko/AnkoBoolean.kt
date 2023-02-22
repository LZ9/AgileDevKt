package com.lodz.android.corekt.anko

/**
 * Boolean型扩展类
 * Created by zhouL on 2018/7/12.
 */

/** 替代java三目运算符（condition ? a : b）为 condition.then { a } ?: b */
infix fun <T> Boolean.then(param: () -> T): T? = if (this) param() else null

/** 将布尔型转为Int，0为false，1为true */
fun Boolean.toInt(): Int = if (this) 1 else 0

/** 将Int转为布尔型，true为1 */
fun Int.toBoolean(): Boolean = this == 1