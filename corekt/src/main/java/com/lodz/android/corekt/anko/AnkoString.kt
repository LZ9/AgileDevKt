package com.lodz.android.corekt.anko

import android.text.Editable

/**
 * String扩展类
 * @author zhouL
 * @date 2019/12/12
 */

/** 把String转为Editable */
fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

/** 在String尾部进行文字拼接 */
fun <T> String.append(t: T?): String = StringBuilder(this).append((t != null).then { t } ?: "").toString()

