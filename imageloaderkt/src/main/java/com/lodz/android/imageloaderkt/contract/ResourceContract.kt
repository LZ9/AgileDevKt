package com.lodz.android.imageloaderkt.contract

import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import androidx.annotation.DrawableRes
import java.io.File

/**
 * 资源加载接口
 * @author zhouL
 * @date 2019/6/11
 */
interface ResourceContract {

    /** 加载网页图片[url] */
    fun loadUrl(url: String?): ImageLoaderContract

    /** 加载URI[uri] */
    fun loadUri(uri: Uri?): ImageLoaderContract

    /** 加载本地文件[file] */
    fun loadFile(file: File?): ImageLoaderContract

    /** 加载本地文件路径[path] */
    fun loadFilePath(path: String?): ImageLoaderContract

    /** 加载资源图片[resId] */
    fun loadResId(@DrawableRes resId: Int): ImageLoaderContract

    /** 加载Base64图片[base64] */
    fun loadBase64(base64: String?, flags: Int = Base64.NO_WRAP): ImageLoaderContract

    /** 加载比特数组[bytes] */
    fun loadBytes(bytes: ByteArray?): ImageLoaderContract

    /** 加载[bitmap] */
    fun loadBitmap(bitmap: Bitmap?): ImageLoaderContract

}