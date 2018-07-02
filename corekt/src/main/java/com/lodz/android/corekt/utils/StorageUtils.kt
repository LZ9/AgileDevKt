package com.lodz.android.corekt.utils

import android.content.Context
import android.os.StatFs
import android.os.storage.StorageManager
import java.lang.reflect.Array

/**
 * 存储帮助类
 * Created by zhouL on 2018/6/28.
 */
object StorageUtils {

    /** 获得路径[path]下存储的大小，单位byte*/
    fun getStorageSize(path: String): Long {
        try {
            val statFs = StatFs(path)
            return statFs.availableBlocksLong * statFs.blockSizeLong
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    /** 获取存储路径，first为内置存储路径，second为外置存储路径 */
    fun getStoragePath(context: Context): Pair<String?, String?> {
        try {
            val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            val storageVolumeClass = Class.forName("android.os.storage.StorageVolume")
            if (storageVolumeClass == null) {
                return Pair(null, null)
            }
            val getVolumeList = storageManager.javaClass.getMethod("getVolumeList")
            val getPath = storageVolumeClass.getMethod("getPath")
            val isRemovable = storageVolumeClass.getMethod("isRemovable")
            val result = getVolumeList.invoke(storageManager)
            val length = Array.getLength(result)
            for (i in 0 until length) {
                val storageVolumeElement = Array.get(result, i)
                val path: String? = getPath.invoke(storageVolumeElement) as String
                val removable: Boolean = isRemovable.invoke(storageVolumeElement) as Boolean
                if (!removable) {
                    return Pair(path, null)// 内置路径
                } else {
                    return Pair(null, path)// 外置路径
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Pair(null, null)
    }

}