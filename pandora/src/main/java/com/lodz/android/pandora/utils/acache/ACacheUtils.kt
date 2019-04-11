package com.lodz.android.pandora.utils.acache

import android.content.Context
import android.os.Environment
import com.lodz.android.corekt.cache.ACache
import com.lodz.android.pandora.base.application.BaseApplication
import java.io.File

/**
 * ACache的帮助类
 * Created by zhouL on 2018/11/7.
 */
class ACacheUtils private constructor() {

    companion object {
        private val sInstance = ACacheUtils()
        @JvmStatic
        fun get(): ACacheUtils = sInstance
    }

    /** 构建对象 */
    private var mBuilder: Builder? = null
    /** 缓存 */
    private var mACache: ACache? = null

    /** 创建构建对象 */
    fun newBuilder(): Builder {
        mBuilder = null
        mBuilder = Builder()
        return mBuilder!!
    }

    /** 获取构建对象 */
    internal fun getBuilder(): Builder {
        if (mBuilder == null) {
            mBuilder = newBuilder()
        }
        return mBuilder!!
    }

    /** 创建ACache对象 */
    fun create(): ACache {
        val builder = getBuilder()
        if (mACache == null) {
            mACache = ACache(builder.getCacheDir(), builder.getMaxSize(), builder.getMaxCount())
        }
        return mACache!!
    }

    inner class Builder {
        /** 缓存目录 */
        private var mCacheDir: File? = null
        /** 缓存大小 */
        private var mMaxSize = 0L
        /** 缓存数量 */
        private var mMaxCount = 0

        /** 设置缓存目录[dirPath] */
        fun setCacheDir(dirPath: String): Builder {
            mCacheDir = File(dirPath)
            return this
        }

        /** 设置缓存大小[maxSize] */
        fun setMaxSize(maxSize: Long): Builder {
            mMaxSize = maxSize
            return this
        }

        /** 设置缓存数量[maxCount] */
        fun setMaxCount(maxCount: Int): Builder {
            mMaxCount = maxCount
            return this
        }

        /** 完成ACache构建 */
        fun build(context: Context) {
            if (mCacheDir == null) {
                mCacheDir = File(context.getCacheDir(), "ACache")
            }
        }

        /** 获取缓存目录 */
        internal fun getCacheDir(): File {
            if (mCacheDir == null) {
                val context = BaseApplication.get()
                if (context != null) {
                    mCacheDir = File(context.getCacheDir(), "ACache")
                } else {
                    mCacheDir = Environment.getDownloadCacheDirectory()
                }
            }
            return mCacheDir!!
        }

        internal fun getMaxSize(): Long = if (mMaxSize == 0L) ACache.MAX_SIZE.toLong() else mMaxSize

        internal fun getMaxCount(): Int = if (mMaxCount == 0) ACache.MAX_COUNT else mMaxCount
    }

}