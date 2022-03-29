package com.lodz.android.corekt.anko

import android.text.Editable
import java.io.IOException

/**
 * String扩展类
 * @author zhouL
 * @date 2019/12/12
 */

/** 把String转为Editable */
fun CharSequence.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

/** 在String尾部进行文字拼接 */
fun <T> CharSequence.append(t: T?): String = StringBuilder(this).append((t != null).then { t } ?: "").toString()


/**
 * ping一个ip地址，true表示连通，false表示不连通
 */
@Throws(InterruptedException::class, IOException::class)
fun String.pingIp(): Boolean {
    val p = Runtime.getRuntime().exec("ping -c 1 -w 2 -s 32 $this")
    val status = p.waitFor()
    return status == 0 // 0表示ping通，非0表示ping不通
}