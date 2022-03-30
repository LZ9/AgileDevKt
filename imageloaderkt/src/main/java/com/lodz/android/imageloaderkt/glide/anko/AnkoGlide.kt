package com.lodz.android.imageloaderkt.glide.anko

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Base64
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.request.RequestListener
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.imageloaderkt.contract.ImageLoaderContract
import java.io.File

/**
 * Glide扩展类
 * @author zhouL
 * @date 2021/10/29
 */

/** 加载网络的[url]图片，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadUrl(
    url: String,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = this.loadConfigUrl(url, block).into(this, listener)

/** 配置网络的[url]图片，配置代码块[block] */
fun ImageView.loadConfigUrl(
    url: String,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(this.context).loadUrl(url).apply(block)

/** 加载手机文件路径[path]图片，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadFilePath(
    path: String,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = this.loadConfigFilePath(path, block).into(this, listener)

/** 配置手机文件路径[path]图片，配置代码块[block] */
fun ImageView.loadConfigFilePath(
    path: String,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(this.context).loadFilePath(path).apply(block)

/** 加载手机文件[file]图片，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadFile(
    file: File,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = loadConfigFile(file, block).into(this, listener)

/** 配置手机文件[file]图片，配置代码块[block] */
fun ImageView.loadConfigFile(
    file: File,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(this.context).loadFile(file).apply(block)

/** 加载[base64]图片，[flags]为Base64的解码类型，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadBase64(
    base64: String,
    flags: Int = Base64.NO_WRAP,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = this.loadConfigBase64(base64, flags, block).into(this, listener)

/** 配置[base64]图片，[flags]为Base64的解码类型，配置代码块[block] */
fun ImageView.loadConfigBase64(
    base64: String,
    flags: Int = Base64.NO_WRAP,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(this.context).loadBase64(base64, flags).apply(block)

/** 加载[bytes]数组图片，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadBytes(
    bytes: ByteArray,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = this.loadConfigBytes(bytes, block).into(this, listener)

/** 配置[bytes]数组图片，配置代码块[block] */
fun ImageView.loadConfigBytes(
    bytes: ByteArray,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(this.context).loadBytes(bytes).apply(block)

/** 加载[bitmap]图片，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadBitmap(
    bitmap: Bitmap,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = this.loadConfigBitmap(bitmap, block).into(this, listener)

/** 配置[bitmap]图片，配置代码块[block] */
fun ImageView.loadConfigBitmap(
    bitmap: Bitmap,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(this.context).loadBitmap(bitmap).apply(block)

/** 加载资源[resId]图片，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadResId(
    @DrawableRes resId: Int,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = this.loadConfigResId(resId, block).into(this, listener)

/** 配置资源[resId]图片，配置代码块[block] */
fun ImageView.loadConfigResId(
    @DrawableRes resId: Int,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(this.context).loadResId(resId).apply(block)

/** 加载[uri]图片，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadUri(
    uri: Uri,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = loadConfigUri(uri, block).into(this, listener)

/** 配置[uri]图片，配置代码块[block] */
fun ImageView.loadConfigUri(
    uri: Uri,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(this.context).loadUri(uri).apply(block)

/** 下载网络图片，上下文[context]，请求监听器[listener] */
fun String.downloadUrlImg(
    context: Context,
    listener: RequestListener<File>? = null,
) = ImageLoader.create(context).loadUrl(this).download(listener)
