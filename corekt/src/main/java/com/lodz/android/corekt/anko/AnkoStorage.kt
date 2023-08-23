package com.lodz.android.corekt.anko

import android.content.Context
import android.os.storage.StorageManager
import java.lang.reflect.Array

/**
 * 存储扩展类
 * Created by zhouL on 2018/7/30.
 */

/** 获取存储路径，first为内置存储路径，second为外置存储路径 */
fun Context.getStoragePath(): Pair<String?, String?> {
    try {
        val storageManager = getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val storageVolumeClass = Class.forName("android.os.storage.StorageVolume")
        val getVolumeList = storageManager.javaClass.getMethod("getVolumeList")
        val getPath = storageVolumeClass.getMethod("getPath")
        val isRemovable = storageVolumeClass.getMethod("isRemovable")
        val result = getVolumeList.invoke(storageManager) ?: return Pair(null, null)
        val length = Array.getLength(result)

        var internal: String? = null
        var external: String? = null

        for (i in 0 until length) {
            val storageVolumeElement = Array.get(result, i)
            val path: String? = getPath.invoke(storageVolumeElement) as? String
            val removable: Boolean = isRemovable.invoke(storageVolumeElement) as Boolean
            if (!removable) {
                internal = path// 内置路径
                continue
            } else {
                external = path// 外置路径
                continue
            }
        }
        return Pair(internal, external)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return Pair(null, null)
}