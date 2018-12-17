package com.lodz.android.imageloaderkt.contract

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Base64
import android.widget.ImageView
import androidx.annotation.AnimRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.ViewPropertyTransition
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.io.File

/**
 * 图片加载库接口
 * Created by zhouL on 2018/7/9.
 */
interface ImageLoaderContract {

    /** 加载网页图片[url] */
    fun loadUrl(url: String): ImageLoaderContract

    /** 加载URI[uri] */
    fun loadUri(uri: Uri): ImageLoaderContract

    /** 加载本地文件[file] */
    fun loadFile(file: File): ImageLoaderContract

    /** 加载本地文件路径[path] */
    fun loadFilePath(path: String): ImageLoaderContract

    /** 加载资源图片[resId] */
    fun loadResId(@DrawableRes resId: Int): ImageLoaderContract

    /** 加载Base64图片[base64] */
    fun loadBase64(base64: String, flags: Int = Base64.NO_WRAP): ImageLoaderContract

    /** 在家比特数组[bytes] */
    fun loadBytes(bytes: ByteArray): ImageLoaderContract

    /** 设置加载图资源[placeholderResId] */
    fun setPlaceholder(@DrawableRes placeholderResId: Int): ImageLoaderContract

    /** 设置加载失败图资源[errorResId] */
    fun setError(@DrawableRes errorResId: Int): ImageLoaderContract

    /** 设置图片宽[widthPx]高[heightPx] */
    fun setImageSize(widthPx: Int, heightPx: Int): ImageLoaderContract

    /** 使用高斯模糊 */
    fun useBlur(): ImageLoaderContract

    /** 设置高斯模糊率[radius]（0-25） */
    fun setBlurRadius(@IntRange(from = 0, to = 25) radius: Int): ImageLoaderContract

    /** 使用圆角 */
    fun useRoundCorner(): ImageLoaderContract

    /** 设置半径为[radius]的圆角 */
    fun setRoundCorner(radius: Int): ImageLoaderContract

    /** 使用圆形图片 */
    fun useCircle(): ImageLoaderContract

    /** 跳过图片缓存入内存 */
    fun skipMemoryCache(): ImageLoaderContract

    /** 设置磁盘缓存方式[diskCacheStrategy] */
    fun diskCacheStrategy(diskCacheStrategy: DiskCacheStrategy): ImageLoaderContract

    /** 设置居中裁切 */
    fun setCenterCrop(): ImageLoaderContract

    /** 设置居中自适应  */
    fun setFitCenter(): ImageLoaderContract

    /** 设置内部居中  */
    fun setCenterInside(): ImageLoaderContract

    /** 设置使用动画  */
    fun dontAnimate(): ImageLoaderContract

    /** 使用默认渐变效果  */
    fun userCrossFade(): ImageLoaderContract

    /** 设置动画资源[animResId]  */
    fun setAnimResId(@AnimRes animResId: Int): ImageLoaderContract

    /** 设置动画编辑器[animator] */
    fun setAnim(animator: ViewPropertyTransition.Animator): ImageLoaderContract

    /** 使用覆盖颜色  */
    fun useFilterColor(): ImageLoaderContract

    /** 设置覆盖颜色[color]  */
    fun setFilterColor(@ColorInt color: Int): ImageLoaderContract

    /** 设置圆角图片的间距[margin] */
    fun setRoundedCornersMargin(margin: Int): ImageLoaderContract

    /** 设置圆角图片的位置参数[type] */
    fun setRoundedCornerType(type: RoundedCornersTransformation.CornerType): ImageLoaderContract

    /** 使用灰度化  */
    fun useGrayscale(): ImageLoaderContract

    /** 切正方形图  */
    fun useCropSquare(): ImageLoaderContract

    /** 使用蒙板图片  */
    fun useMask(): ImageLoaderContract

    /** 设置蒙板图片资源[maskResId]  */
    fun setMaskResId(@DrawableRes maskResId: Int): ImageLoaderContract

    /** 显示视频第一帧  */
    fun setVideo(): ImageLoaderContract

    /** 添加图片请求监听器[listener]  */
    fun setRequestListener(listener: RequestListener<*>): ImageLoaderContract

    /** 将图片装载进[imageView] */
    fun into(imageView: ImageView)

    /** 将图片装载进[target] */
    fun into(target: SimpleTarget<Drawable>)

    /** 将图片转为bitmap并装载到[imageView] */
    fun asBitmapInto(imageView: ImageView)

    /** 将图片转为bitmap并装载到[target] */
    fun asBitmapInto(target: SimpleTarget<Bitmap>)

    /** 将图片转为gif并装载到[imageView] */
    fun asGifInto(imageView: ImageView)

    /** 将图片转为gif并装载到[target] */
    fun asGifInto(target: SimpleTarget<GifDrawable>)
}