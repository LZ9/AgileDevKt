package com.lodz.android.corekt.anko

import android.text.Editable
import java.io.IOException
import java.net.URLDecoder
import java.net.URLEncoder

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

/** 用UTF-8解码文字 */
fun String.decodeUtf8(): String {
    try {
        if (this.isNotEmpty()) {
            return URLDecoder.decode(this, "UTF-8")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/** 用UTF-8编码文字[str] */
fun String.encodeUtf8(): String {
    try {
        if (this.isNotEmpty()) {
            return URLEncoder.encode(this, "UTF-8")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/** 根据分隔符[separator]组装数组 */
fun Array<String>.getStringBySeparator(separator: String): String = this.toList().getStringBySeparator(separator)

/** 根据分隔符[separator]组装列表 */
fun List<String>.getStringBySeparator(separator: String): String {
    var result = ""
    if (this.isEmpty()) {
        return result
    }
    for (i in this.indices) {
        result = result + this[i] + if (i == (this.size - 1)) "" else separator
    }
    return result
}

/** 根据分隔符[separator]将字符串转为列表 */
fun String.getListBySeparator(separator: String): List<String> {
    var source = this
    val list = ArrayList<String>()
    while (source.contains(separator)) {
        val value = source.substring(0, source.indexOf(separator))
        if (value.isNotEmpty()) {
            list.add(value)
        }
        source = source.substring(source.indexOf(separator) + 1, source.length)
    }
    if (source.isNotEmpty()) {
        list.add(source)
    }
    return list
}

/** 根据分隔符[separator]将字符串转为数组 */
fun String.getArrayBySeparator(separator: String): Array<String> = this.getListBySeparator(separator).toTypedArray()

