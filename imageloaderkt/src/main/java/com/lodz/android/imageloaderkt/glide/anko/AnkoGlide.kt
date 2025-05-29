package com.lodz.android.imageloaderkt.glide.anko

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Base64
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers
import com.bumptech.glide.request.RequestListener
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.imageloaderkt.contract.ImageLoaderContract
import java.io.File

/**
 * Glide扩展类
 * @author zhouL
 * @date 2021/10/29
 */


/** 加载网络的[url]图片，上下文[context]，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadUrl(
    url: String?,
    context: Context = this.context,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(context).loadUrl(url).apply(block).into(this, listener)

/** 加载网络的[url]图片，头信息[headers]，上下文[context]，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadUrl(
    url: String?,
    headers : Headers,
    context: Context = this.context,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(context).loadUrl(url, headers).apply(block).into(this, listener)

/** 加载网络的[glideUrl]加载自定义地址，上下文[context]，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadUrl(
    glideUrl: GlideUrl?,
    context: Context = this.context,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(context).loadGlideUrl(glideUrl).apply(block).into(this, listener)


/** 加载[uri]图片，上下文[context]，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadUri(
    uri: Uri,
    context: Context = this.context,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(context).loadUri(uri).apply(block).into(this, listener)

/** 加载手机文件[file]图片，上下文[context]，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadFile(
    file: File,
    context: Context = this.context,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(context).loadFile(file).apply(block).into(this, listener)

/** 加载手机文件路径[path]图片，上下文[context]，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadFilePath(
    path: String,
    context: Context = this.context,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(context).loadFilePath(path).apply(block).into(this, listener)

/** 加载资源[resId]图片，上下文[context]，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadResId(
    @DrawableRes resId: Int,
    context: Context = this.context,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(context).loadResId(resId).apply(block).into(this, listener)

/** 加载[base64]图片，[flags]为Base64的解码类型，上下文[context]，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadBase64(
    base64: String,
    flags: Int = Base64.NO_WRAP,
    context: Context = this.context,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(context).loadBase64(base64, flags).apply(block).into(this, listener)

/** 加载[bytes]数组图片，上下文[context]，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadBytes(
    bytes: ByteArray,
    context: Context = this.context,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(context).loadBytes(bytes).apply(block).into(this, listener)

/** 加载[bitmap]图片，上下文[context]，请求监听器[listener]，配置代码块[block] */
fun ImageView.loadBitmap(
    bitmap: Bitmap,
    context: Context = this.context,
    listener: RequestListener<Drawable>? = null,
    block: ImageLoaderContract.() -> Unit = {}
) = ImageLoader.create(context).loadBitmap(bitmap).apply(block).into(this, listener)

/** 下载网络图片，上下文[context]，请求监听器[listener] */
fun String.downloadImg(
    context: Context,
    listener: RequestListener<File>? = null,
) = ImageLoader.create(context).loadUrl(this).download(listener)
