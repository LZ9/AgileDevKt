package com.lodz.android.imageloaderkt

import android.content.Context
import android.support.annotation.DrawableRes
import com.lodz.android.imageloaderkt.glide.config.GlideApp
import com.lodz.android.imageloaderkt.utils.CompileUtils
import java.io.File

/**
 * 图片库管理类
 * Created by zhouL on 2018/7/10.
 */
class ImageloaderManager private constructor() {

    companion object {
        private val sInstance = ImageloaderManager()
        fun get() = sInstance
    }

    private var mBuilder: Builder? = null

    /** 设置构建对象 */
    fun newBuilder(): Builder {
        mBuilder = null
        mBuilder = Builder()
        return mBuilder!!
    }

    /** 获取构建对象 */
    fun getBuilder(): Builder {
        if (mBuilder == null) {
            mBuilder = newBuilder()
        }
        return mBuilder!!
    }

    /** 清除内存缓存（必须在主线程） */
    fun clearMemoryCaches(context: Context) {
        GlideApp.get(context).clearMemory()
    }

    /** 清除内存缓存（包括手动GC内存，必须在主线程） */
    fun clearMemoryCachesWithGC(context: Context) {
        GlideApp.get(context).clearMemory()
        System.gc()
    }

    /** 清除磁盘缓存（必须异步线程） */
    fun clearDiskCaches(context: Context) {
        GlideApp.get(context).clearDiskCache()
    }

    /** 暂停加载 */
    fun pauseLoad(context: Context) {
        GlideApp.with(context).pauseRequests()
    }

    /** 恢复加载 */
    fun resumeLoad(context: Context) {
        GlideApp.with(context).resumeRequests()
    }

    /** 是否暂停加载 */
    fun isPaused(context: Context) = GlideApp.with(context).isPaused

    inner class Builder {

        /** 占位符图片资源id */
        @DrawableRes
        private var placeholderResId = 0
        /** 加载失败图片资源id */
        @DrawableRes
        private var errorResId = 0
        /** 图片缓存目录 */
        private var directoryFile: File? = null
        /** 缓存图片文件夹名称  */
        private var directoryName = ""

        init {
            if (!CompileUtils.isClassExists("com.bumptech.glide.Glide")) {
                throw RuntimeException("请在你的build.gradle文件中配置Glide依赖")
            }
        }

        /** 设置占位符图片资源[placeholderResId] */
        fun setPlaceholderResId(placeholderResId: Int): Builder {
            this.placeholderResId = placeholderResId
            return this
        }

        /** 设置加载失败图片资源[errorResId] */
        fun setErrorResId(errorResId: Int): Builder {
            this.errorResId = errorResId
            return this
        }

        /** 设置图片缓存目录文件[file] */
        fun setDirectoryFile(file: File): Builder {
            this.directoryFile = file
            return this
        }

        /** 设置缓存图片文件夹名称[name] */
        fun setDirectoryName(name: String): Builder {
            this.directoryName = name
            return this
        }

        /** 完成构建并初始化  */
        fun build() {}

        /** 获取默认占位符图片 */
        @DrawableRes
        fun getPlaceholderResId() = placeholderResId

        /** 获取默认加载失败图片 */
        @DrawableRes
        fun getErrorResId() = errorResId

        /** 获取图片缓存目录文件 */
        fun getDirectoryFile() = directoryFile

        /** 获取缓存图片文件夹名称 */
        fun getDirectoryName() = directoryName
    }
}