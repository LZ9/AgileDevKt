package com.lodz.android.corekt.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.ByteArrayOutputStream


/**
 * 时间计算工具类
 * Created by Michael Yang on 2013/8/7.
 * update by zhouL on 2018/11/7.
 */
internal object Utils {

    private const val SEPARATOR = ' '

    /** 判断缓存的数据[str]是否到期 */
    fun isDue(str: String): Boolean = isDue(str.toByteArray())

    /** 判断缓存的数据[data]是否到期 */
    fun isDue(data: ByteArray): Boolean {
        val strs = getDateInfoFromDate(data)
        if (strs != null && strs.size == 2) {
            var saveTimeStr = strs[0]
            while (saveTimeStr.startsWith("0")) {
                saveTimeStr = saveTimeStr.substring(1, saveTimeStr.length)
            }
            if (System.currentTimeMillis() > saveTimeStr.toLong() + strs[1].toLong() * 1000) {
                return true
            }
        }
        return false
    }

    fun newStringWithDateInfo(second: Int, strInfo: String): String = createDateInfo(second) + strInfo

    fun newByteArrayWithDateInfo(second: Int, data2: ByteArray): ByteArray {
        val data1 = createDateInfo(second).toByteArray()
        val retdata = ByteArray(data1.size + data2.size)
        System.arraycopy(data1, 0, retdata, 0, data1.size)
        System.arraycopy(data2, 0, retdata, data1.size, data2.size)
        return retdata
    }

    fun clearDateInfo(strInfo: String): String {
        var info = strInfo
        if (hasDateInfo(info.toByteArray())) {
            info = info.substring(info.indexOf(SEPARATOR) + 1, info.length)
        }
        return info
    }

    fun clearDateInfo(data: ByteArray): ByteArray {
        if (hasDateInfo(data)) {
            return copyOfRange(data, indexOf(data, SEPARATOR) + 1, data.size)
        }
        return data
    }

    private fun getDateInfoFromDate(data: ByteArray): Array<String>? {
        if (hasDateInfo(data)) {
            val saveDate = String(copyOfRange(data, 0, 13))
            val deleteAfter = String(copyOfRange(data, 14, indexOf(data, SEPARATOR)))
            return arrayOf(saveDate, deleteAfter)
        }
        return null
    }

    private fun hasDateInfo(data: ByteArray): Boolean = data.size > 15 && data[13] == '-'.toByte() && indexOf(data, SEPARATOR) > 14


    private fun indexOf(data: ByteArray, c: Char): Int {
        for (i in data.indices) {
            if (data[i] == c.toByte()) {
                return i
            }
        }
        return -1
    }

    private fun copyOfRange(original: ByteArray, from: Int, to: Int): ByteArray {
        val newLength = to - from
        if (newLength < 0) {
            throw IllegalArgumentException("$from > $to")
        }
        val copy = ByteArray(newLength)
        System.arraycopy(original, from, copy, 0, Math.min(original.size - from, newLength))
        return copy
    }

    private fun createDateInfo(second: Int): String {
        var currentTime = System.currentTimeMillis().toString()
        while (currentTime.length < 13) {
            currentTime = "0" + currentTime
        }
        return currentTime + "-" + second + SEPARATOR
    }

    fun Bitmap2Bytes(bitmap: Bitmap): ByteArray {
        ByteArrayOutputStream().use { baos: ByteArrayOutputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            return baos.toByteArray()
        }
    }

    fun Bytes2Bimap(b: ByteArray): Bitmap? {
        if (b.size == 0) {
            return null
        }
        return BitmapFactory.decodeByteArray(b, 0, b.size)
    }

    fun drawable2Bitmap(drawable: Drawable): Bitmap {
        // 取 drawable 的长宽
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight
        // 取 drawable 的颜色格式
        val config = if (drawable.opacity != PixelFormat.OPAQUE) {
            Bitmap.Config.ARGB_8888
        } else {
            Bitmap.Config.RGB_565
        }
        // 建立对应 bitmap
        val bitmap = Bitmap.createBitmap(w, h, config)
        // 建立对应 bitmap 的画布
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        // 把 drawable 内容画到画布中
        drawable.draw(canvas)
        return bitmap
    }

    fun bitmap2Drawable(bitmap: Bitmap):Drawable = BitmapDrawable(null, bitmap)
}