package com.lodz.android.imageloaderkt.contract

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.AnimRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.transition.ViewPropertyTransition
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.io.File

/**
 * 图片加载库接口
 * Created by zhouL on 2018/7/9.
 */
interface ImageLoaderContract {

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

    /** 将图片装载进[imageView] */
    fun into(imageView: ImageView)

    /** 将图片装载进[imageView]，设置请求监听器[listener] */
    fun into(imageView: ImageView, listener: RequestListener<Drawable>? = null)

    /** 将图片装载进[target] */
    fun into(target: com.bumptech.glide.request.target.Target<Drawable>)

    /** 将图片装载进[target]，设置请求监听器[listener]  */
    fun into(target: com.bumptech.glide.request.target.Target<Drawable>, listener: RequestListener<Drawable>? = null)

    /** 将图片转为bitmap并装载到[imageView] */
    fun asBitmapInto(imageView: ImageView)

    /** 将图片转为bitmap并装载到[imageView]，设置请求监听器[listener]  */
    fun asBitmapInto(imageView: ImageView, listener: RequestListener<Bitmap>? = null)

    /** 将图片转为bitmap并装载到[target] */
    fun asBitmapInto(target: com.bumptech.glide.request.target.Target<Bitmap>)

    /** 将图片转为bitmap并装载到[target]，设置请求监听器[listener] */
    fun asBitmapInto(target: com.bumptech.glide.request.target.Target<Bitmap>, listener: RequestListener<Bitmap>? = null)

    /** 将图片转为gif并装载到[imageView] */
    fun asGifInto(imageView: ImageView)

    /** 将图片转为gif并装载到[imageView]，设置请求监听器[listener] */
    fun asGifInto(imageView: ImageView, listener: RequestListener<GifDrawable>? = null)

    /** 将图片转为gif并装载到[target] */
    fun asGifInto(target: com.bumptech.glide.request.target.Target<GifDrawable>)

    /** 将图片转为gif并装载到[target]，设置请求监听器[listener] */
    fun asGifInto(target: com.bumptech.glide.request.target.Target<GifDrawable>, listener: RequestListener<GifDrawable>? = null)

    /** 下载图片 */
    fun download()

    /** 下载图片 */
    fun download(listener: RequestListener<File>? = null)
}