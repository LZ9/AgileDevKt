package com.lodz.android.corekt.cache

import com.lodz.android.corekt.threadpool.ThreadPoolManager
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.collections.HashMap

/**
 * 缓存管理器
 * Created by Michael Yang on 2013/8/7.
 * update by zhouL on 2018/11/7.
 */
internal class ACacheManager(private var cacheDir: File, private var sizeLimit: Long, private var countLimit: Int) {
    private var cacheSize: AtomicLong
    private var cacheCount: AtomicInteger
    private val lastUsageDates = Collections.synchronizedMap(HashMap<File, Long>())

    init {
        cacheSize = AtomicLong()
        cacheCount = AtomicInteger()
        calculateCacheSizeAndCacheCount()
    }

    /** 计算 cacheSize和cacheCount */
    private fun calculateCacheSizeAndCacheCount() {
        ThreadPoolManager.get().executeNormal(Runnable {
            var size = 0L
            var count = 0
            val cachedFiles = cacheDir.listFiles()
            if (cachedFiles != null) {
                for (cachedFile in cachedFiles) {
                    size += cachedFile.length()
                    count += 1
                    lastUsageDates.put(cachedFile, cachedFile.lastModified())
                }
                cacheSize.set(size)
                cacheCount.set(count)
            }
        })
    }

    fun put(file: File) {
        var curCacheCount = cacheCount.get()
        while (curCacheCount + 1 > countLimit) {
            val freedSize = removeNext()
            cacheSize.addAndGet(-freedSize)
            curCacheCount = cacheCount.addAndGet(-1)
        }
        cacheCount.addAndGet(1)

        val valueSize = file.length()
        var curCacheSize = cacheSize.get()
        while (curCacheSize + valueSize > sizeLimit) {
            val freedSize = removeNext()
            curCacheSize = cacheSize.addAndGet(-freedSize)
        }
        cacheSize.addAndGet(valueSize)

        val currentTime = System.currentTimeMillis()
        file.setLastModified(currentTime)
        lastUsageDates.put(file, currentTime)
    }

    fun get(key: String): File {
        val file = newFile(key)
        val currentTime = System.currentTimeMillis()
        file.setLastModified(currentTime)
        lastUsageDates.put(file, currentTime)
        return file
    }

    fun newFile(key: String): File = File(cacheDir, key.hashCode().toString())

    fun remove(key: String): Boolean = get(key).delete()

    fun clear() {
        lastUsageDates.clear()
        cacheSize.set(0)
        val files = cacheDir.listFiles()
        if (files != null) {
            for (file in files) {
                file.delete()
            }
        }
    }

    /** 移除旧的文件 */
    private fun removeNext(): Long {
        if (lastUsageDates.isEmpty()) {
            return 0
        }
        var oldestUsage: Long = 0
        var mostLongUsedFile: File? = null
        val entries = lastUsageDates.entries
        synchronized(lastUsageDates) {
            for (entry in entries) {
                if (mostLongUsedFile == null) {
                    mostLongUsedFile = entry.key
                    oldestUsage = entry.value
                } else {
                    val lastValueUsage = entry.value
                    if (lastValueUsage < oldestUsage) {
                        oldestUsage = lastValueUsage
                        mostLongUsedFile = entry.key
                    }
                }
            }
        }

        val fileSize = if (mostLongUsedFile != null) mostLongUsedFile!!.length() else 0
        if (mostLongUsedFile != null && mostLongUsedFile!!.delete()) {
            lastUsageDates.remove(mostLongUsedFile)
        }
        return fileSize
    }
}