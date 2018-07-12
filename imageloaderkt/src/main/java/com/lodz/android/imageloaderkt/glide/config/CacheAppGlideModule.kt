package com.lodz.android.imageloaderkt.glide.config

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.lodz.android.imageloaderkt.ImageloaderManager
import com.lodz.android.imageloaderkt.utils.CompileUtils
import java.io.InputStream

/**
 * Glide配置
 * Created by zhouL on 2018/7/10.
 */
@GlideModule
class CacheAppGlideModule : AppGlideModule() {

    /** 缓存文件夹名称 */
    private val IMAGE_PIPELINE_CACHE_DIR = "image_cache"

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)

        // 获取配置缓存文件夹名称
        var directoryName = ImageloaderManager.get().getBuilder().getDirectoryName()
        if (directoryName.isEmpty()) {
            directoryName = IMAGE_PIPELINE_CACHE_DIR
        }

        // 获取配置缓存文件路径文件
        var fileCacheDir = ImageloaderManager.get().getBuilder().getDirectoryFile()
        if (fileCacheDir == null) {
            fileCacheDir = context.applicationContext.cacheDir
            ImageloaderManager.get().getBuilder().setDirectoryFile(fileCacheDir)
        } else {
            if (!fileCacheDir.exists()) {// 传入的文件路径未创建，则创建该文件
                try {
                    fileCacheDir.mkdirs()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (!fileCacheDir.exists()) {// 创建失败则使用默认路径
                fileCacheDir = context.applicationContext.cacheDir
                ImageloaderManager.get().getBuilder().setDirectoryFile(fileCacheDir)
            }
        }

        val diskCacheSize = 1024 * 1024 * 30;//最多可以缓存多少字节的数据
        //设置磁盘缓存大小
        builder.setDiskCache(DiskLruCacheFactory(fileCacheDir!!.absolutePath, directoryName, diskCacheSize.toLong()))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        if (CompileUtils.isClassExists("com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader")) {
            registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory())
        }
    }
}