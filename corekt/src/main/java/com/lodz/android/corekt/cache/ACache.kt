package com.lodz.android.corekt.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import org.json.JSONArray
import org.json.JSONObject
import java.io.*

/**
 * 轻量级缓存
 * Created by Michael Yang on 2013/8/7.
 * update by zhouL on 2018/11/7.
 */
class ACache {
    companion object {
        const val TIME_HOUR = 60 * 60
        const val TIME_DAY = TIME_HOUR * 24
        const val MAX_SIZE = 1000 * 1000 * 50 // 50MB
        const val MAX_COUNT = Integer.MAX_VALUE // 不限制存放数据的数量
    }

    private var mCache: ACacheManager

    @JvmOverloads
    constructor(
        context: Context,
        cacheName: String = "ACache",
        maxSize: Long = MAX_SIZE.toLong(),
        maxCount: Int = MAX_COUNT
    ) : this(File(context.cacheDir, cacheName), maxSize, maxCount)

    @JvmOverloads
    constructor(cacheDir: File, maxSize: Long = MAX_SIZE.toLong(), maxCount: Int = MAX_COUNT) {
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw RuntimeException("can't make dirs in ${cacheDir.absolutePath}")
        }
        mCache = ACacheManager(cacheDir, maxSize, maxCount)
    }

    // =======================================
    // ============ String数据 读写 ==============
    // =======================================

    /** 保存String数据到缓存中，[key]键，[value]值 */
    fun put(key: String, value: String) {
        val file = mCache.newFile(key)
        FileWriter(file).use { fw: FileWriter ->
            BufferedWriter(fw, 1024).use { bf: BufferedWriter ->
                bf.write(value)
            }
        }
        mCache.put(file)
    }

    /** 保存String数据到缓存中，[key]键，[value]值，[saveTime]保存的时间（单位：秒） */
    fun put(key: String, value: String, saveTime: Int) {
        put(key, Utils.newStringWithDateInfo(saveTime, value))
    }

    /** 读取String数据，[key]键 */
    fun getAsString(key: String): String {
        val file = mCache.get(key)
        if (!file.exists()) {
            return ""
        }
        FileReader(file).use { fr: FileReader ->
            BufferedReader(fr).use { br: BufferedReader ->
                var readString = ""
                var currentLine: String?
                do {
                    currentLine = br.readLine()
                    if (currentLine == null) {
                        break
                    }
                    readString += currentLine
                } while (true)

                return if (!Utils.isDue(readString)) {
                    Utils.clearDateInfo(readString)
                } else {
                    remove(key)
                    ""
                }
            }
        }
    }

    // =======================================
    // ============= JSONObject 数据 读写 ==============
    // =======================================

    /** 保存JSONObject数据到缓存中，[key]键，[value]值 */
    fun put(key: String, value: JSONObject) {
        put(key, value.toString())
    }

    /** 保存JSONObject数据到缓存中，[key]键，[value]值，[saveTime]保存的时间（单位：秒） */
    fun put(key: String, value: JSONObject, saveTime: Int) {
        put(key, value.toString(), saveTime)
    }

    /** 读取JSONObject数据，[key]键 */
    fun getAsJSONObject(key: String): JSONObject? {
        val json = getAsString(key)
        try {
            return JSONObject(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    // =======================================
    // ============ JSONArray 数据 读写 =============
    // =======================================

    /** 保存JSONArray数据到缓存中，[key]键，[value]值 */
    fun put(key: String, value: JSONArray) {
        put(key, value.toString())
    }

    /** 保存JSONArray数据到缓存中，[key]键，[value]值，[saveTime]保存的时间（单位：秒） */
    fun put(key: String, value: JSONArray, saveTime: Int) {
        put(key, value.toString(), saveTime)
    }

    /** 读取JSONArray数据，[key]键 */
    fun getAsJSONArray(key: String): JSONArray? {
        val json = getAsString(key)
        try {
            return JSONArray(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    // =======================================
    // ============== byte 数据 读写 =============
    // =======================================

    /** 保存byte数据到缓存中，[key]键，[value]值 */
    fun put(key: String, value: ByteArray) {
        val file = mCache.newFile(key)
        FileOutputStream(file).use { fos: FileOutputStream ->
            fos.write(value)
            mCache.put(file)
        }
    }

    /** 保存byte数据到缓存中，[key]键，[value]值，[saveTime]保存的时间（单位：秒） */
    fun put(key: String, value: ByteArray, saveTime: Int) {
        put(key, Utils.newByteArrayWithDateInfo(saveTime, value))
    }

    /** 读取byte数据，[key]键 */
    fun getAsByteArray(key: String): ByteArray? {
        val file = mCache.get(key)
        if (!file.exists()) {
            return null
        }
        RandomAccessFile(file, "r").use { raf: RandomAccessFile ->
            val byteArray = ByteArray(raf.length().toInt())
            raf.read(byteArray)
            return if (!Utils.isDue(byteArray)) {
                Utils.clearDateInfo(byteArray)
            } else {
                remove(key)
                null
            }
        }
    }

    // =======================================
    // ============= 序列化 数据 读写 ===============
    // =======================================


    /** 保存Serializable数据到缓存中，[key]键，[value]值 */
    fun put(key: String, value: Serializable) {
        put(key, value, -1)
    }

    /** 保存Serializable数据到缓存中，[key]键，[value]值，[saveTime]保存的时间（单位：秒） */
    fun put(key: String, value: Serializable, saveTime: Int) {
        ByteArrayOutputStream().use { baos: ByteArrayOutputStream ->
            ObjectOutputStream(baos).use { oos: ObjectOutputStream ->
                oos.writeObject(value)
                val data = baos.toByteArray()
                if (saveTime != -1) {
                    put(key, data, saveTime)
                } else {
                    put(key, data)
                }
            }
        }
    }

    /** 读取Serializable数据，[key]键 */
    fun getAsAny(key: String): Any? {
        val data = getAsByteArray(key) ?: return null
        ByteArrayInputStream(data).use { bais: ByteArrayInputStream ->
            ObjectInputStream(bais).use { ois: ObjectInputStream ->
                return ois.readObject()
            }
        }
    }

    // =======================================
    // ============== bitmap 数据 读写 =============
    // =======================================

    /** 保存Bitmap数据到缓存中，[key]键，[value]值 */
    fun put(key: String, value: Bitmap) {
        put(key, Utils.bitmap2Bytes(value))
    }

    /** 保存Bitmap数据到缓存中，[key]键，[value]值，[saveTime]保存的时间（单位：秒） */
    fun put(key: String, value: Bitmap, saveTime: Int) {
        put(key, Utils.bitmap2Bytes(value), saveTime)
    }

    /** 读取Bitmap数据，[key]键 */
    fun getAsBitmap(key: String): Bitmap? {
        val array = getAsByteArray(key) ?: return null
        return Utils.bytes2Bimap(array)
    }

    // =======================================
    // ============= drawable 数据 读写 =============
    // =======================================

    /** 保存Drawable数据到缓存中，[key]键，[value]值 */
    fun put(key: String, value: Drawable) {
        put(key, Utils.drawable2Bitmap(value))
    }

    /** 保存Drawable数据到缓存中，[key]键，[value]值，[saveTime]保存的时间（单位：秒） */
    fun put(key: String, value: Drawable, saveTime: Int) {
        put(key, Utils.drawable2Bitmap(value), saveTime)
    }

    /** 读取Drawable数据，[key]键 */
    fun getAsDrawable(key: String): Drawable? {
        val array = getAsByteArray(key) ?: return null
        val bitmap = Utils.bytes2Bimap(array) ?: return null
        return Utils.bitmap2Drawable(bitmap)
    }

    // =======================================
    // ============= 其他 =============
    // =======================================

    /** 获取缓存文件，[key]键 */
    fun file(key: String): File? {
        val file = mCache.newFile(key)
        return if (file.exists()) file else null
    }

    /** 移除某个键值[key] */
    fun remove(key: String): Boolean = mCache.remove(key)

    /** 清除所有数据 */
    fun clear() {
        mCache.clear()
    }
}