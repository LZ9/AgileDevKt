package com.lodz.android.imageloaderkt

import android.content.Context
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.lodz.android.imageloaderkt.utils.CompileUtils
import okhttp3.OkHttpClient
import java.io.File

/**
 * 图片库管理类
 * Created by zhouL on 2018/7/10.
 */
class ImageloaderManager private constructor() {

    companion object {
        private val sInstance = ImageloaderManager()
        @JvmStatic
        fun get(): ImageloaderManager = sInstance
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
        Glide.get(context).clearMemory()
    }

    /** 清除内存缓存（包括手动GC内存，必须在主线程） */
    fun clearMemoryCachesWithGC(context: Context) {
        Glide.get(context).clearMemory()
        System.gc()
    }

    /** 清除磁盘缓存（必须异步线程） */
    fun clearDiskCaches(context: Context) {
        Glide.get(context).clearDiskCache()
    }

    /** 暂停加载 */
    fun pauseLoad(context: Context) {
        Glide.with(context).pauseRequests()
    }

    /** 恢复加载 */
    fun resumeLoad(context: Context) {
        Glide.with(context).resumeRequests()
    }

    /** 是否暂停加载 */
    fun isPaused(context: Context): Boolean = Glide.with(context).isPaused

    inner class Builder {

        /** 占位符图片资源id */
        @DrawableRes
        private var placeholderResId = 0
        /** 加载失败图片资源id */
        @DrawableRes
        private var errorResId = 0
        /** 图片缓存目录 */
        private var directoryFile: File? = null
        /** 缓存图片文件夹名称 */
        private var directoryName = ""
        /** OkHttp客户端 */
        private var client: OkHttpClient? = null

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

        /** 设置OkHttp客户端[client] */
        fun setOkHttpClient(client: OkHttpClient): Builder {
            this.client = client
            return this
        }

        /** 完成构建并初始化 */
        fun build() {}

        /** 获取默认占位符图片 */
        @DrawableRes
        fun getPlaceholderResId(): Int = placeholderResId

        /** 获取默认加载失败图片 */
        @DrawableRes
        fun getErrorResId(): Int = errorResId

        /** 获取图片缓存目录文件 */
        fun getDirectoryFile(): File? = directoryFile

        /** 获取缓存图片文件夹名称 */
        fun getDirectoryName(): String = directoryName

        /** 获取OkHttp客户端 */
        fun getOkHttpClient(): OkHttpClient? = client
    }
}