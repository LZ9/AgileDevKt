package com.lodz.android.imageloaderkt.glide.impl

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.ViewPropertyTransition
import com.bumptech.glide.signature.ObjectKey
import com.lodz.android.imageloaderkt.contract.ImageLoaderContract
import com.lodz.android.imageloaderkt.glide.transformations.CropRoundedCornersTransformation
import jp.wasabeef.glide.transformations.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Glide图片加载库
 * Created by zhouL on 2018/7/11.
 */
@SuppressLint("CheckResult")
internal class GlideImageLoader internal constructor(
    private val mGlideBuilderBean: GlideBuilderBean,
    private val mRequestManager: RequestManager
) : ImageLoaderContract {

    override fun setPlaceholder(placeholderResId: Int): ImageLoaderContract {
        mGlideBuilderBean.placeholderResId = placeholderResId
        return this
    }

    override fun setError(errorResId: Int): ImageLoaderContract {
        mGlideBuilderBean.errorResId = errorResId
        return this
    }

    override fun setImageSize(widthPx: Int, heightPx: Int): ImageLoaderContract {
        mGlideBuilderBean.widthPx = widthPx
        mGlideBuilderBean.heightPx = heightPx
        return this
    }

    override fun useBlur(): ImageLoaderContract {
        mGlideBuilderBean.useBlur = true
        return this
    }

    override fun setBlurRadius(radius: Int): ImageLoaderContract {
        useBlur()
        mGlideBuilderBean.blurRadius = radius
        return this
    }

    override fun useRoundCorner(): ImageLoaderContract {
        mGlideBuilderBean.useRoundCorner = true
        return this
    }

    override fun setRoundCorner(radius: Int): ImageLoaderContract {
        useRoundCorner()
        mGlideBuilderBean.roundCornerRadius = radius
        return this
    }

    override fun useCircle(): ImageLoaderContract {
        mGlideBuilderBean.useCircle = true
        return this
    }

    override fun skipMemoryCache(): ImageLoaderContract {
        mGlideBuilderBean.saveToMemoryCache = false
        return this
    }

    override fun diskCacheStrategy(diskCacheStrategy: DiskCacheStrategy): ImageLoaderContract {
        mGlideBuilderBean.diskCacheStrategy = diskCacheStrategy
        return this
    }

    override fun setCenterCrop(): ImageLoaderContract {
        mGlideBuilderBean.centerCrop = true
        return this
    }

    override fun setFitCenter(): ImageLoaderContract {
        mGlideBuilderBean.fitCenter = true
        return this
    }

    override fun setCenterInside(): ImageLoaderContract {
        mGlideBuilderBean.centerInside = true
        return this
    }

    override fun dontAnimate(): ImageLoaderContract {
        mGlideBuilderBean.dontAnimate = true
        return this
    }

    override fun userCrossFade(): ImageLoaderContract {
        mGlideBuilderBean.crossFade = true
        mGlideBuilderBean.dontAnimate = false
        return this
    }

    override fun setAnimResId(animResId: Int): ImageLoaderContract {
        mGlideBuilderBean.animResId = animResId
        mGlideBuilderBean.dontAnimate = false
        return this
    }

    override fun setAnim(animator: ViewPropertyTransition.Animator): ImageLoaderContract {
        mGlideBuilderBean.animator = animator
        mGlideBuilderBean.dontAnimate = false
        return this
    }

    override fun useFilterColor(): ImageLoaderContract {
        mGlideBuilderBean.useFilterColor = true
        return this
    }

    override fun setFilterColor(color: Int): ImageLoaderContract {
        useFilterColor()
        mGlideBuilderBean.filterColor = color
        return this
    }

    override fun setRoundedCornersMargin(margin: Int): ImageLoaderContract {
        mGlideBuilderBean.roundedCornersMargin = margin
        return this
    }

    override fun setRoundedCornerType(type: RoundedCornersTransformation.CornerType): ImageLoaderContract {
        mGlideBuilderBean.cornerType = type
        return this
    }

    override fun useGrayscale(): ImageLoaderContract {
        mGlideBuilderBean.useGrayscale = true
        return this
    }

    override fun useCropSquare(): ImageLoaderContract {
        mGlideBuilderBean.useCropSquare = true
        return this
    }

    override fun useMask(): ImageLoaderContract {
        mGlideBuilderBean.useMask = true
        return this
    }

    override fun setMaskResId(maskResId: Int): ImageLoaderContract {
        useMask()
        mGlideBuilderBean.maskResId = maskResId
        return this
    }

    override fun setVideo(): ImageLoaderContract {
        mGlideBuilderBean.isVideo = true
        return this
    }

    override fun download() {
        download(null)
    }

    override fun download(listener: RequestListener<File>?) {
        val bean = mGlideBuilderBean
        mRequestManager.download(bean.path).apply {
            this.apply(getRequestOptions(bean))
        }.apply {
            if (listener != null) {// 设置请求监听器
                this.listener(listener)
            }
        }.submit()
    }

    override fun into(imageView: ImageView) {
        into(imageView, null)
    }

    override fun into(imageView: ImageView, listener: RequestListener<Drawable>?) {
        val bean = mGlideBuilderBean

        mRequestManager.load(bean.path).apply {
            this.apply(getRequestOptions(bean))
        }.apply {
            if (bean.crossFade) {
                this.transition(DrawableTransitionOptions().crossFade())
            }
            if (bean.animResId != -1) {
                this.transition(DrawableTransitionOptions().transition(bean.animResId))
            }
            if (bean.animator != null) {
                this.transition(DrawableTransitionOptions().transition(bean.animator!!))
            }
        }.apply {
            if (listener != null) {// 设置请求监听器
                this.listener(listener)
            }
        }.apply {
            this.into(imageView)
        }
    }

    override fun into(target: com.bumptech.glide.request.target.Target<Drawable>) {
        into(target, null)
    }

    override fun into(target: com.bumptech.glide.request.target.Target<Drawable>, listener: RequestListener<Drawable>?) {
        val bean = mGlideBuilderBean

        mRequestManager.load(bean.path).apply {
            this.apply(getRequestOptions(bean))
        }.apply {
            if (bean.crossFade) {
                this.transition(DrawableTransitionOptions().crossFade())
            }
            if (bean.animResId != -1) {
                this.transition(DrawableTransitionOptions().transition(bean.animResId))
            }
            if (bean.animator != null) {
                this.transition(DrawableTransitionOptions().transition(bean.animator!!))
            }
        }.apply {
            if (listener != null) {// 设置请求监听器
                this.listener(listener)
            }
        }.apply {
            this.into(target)
        }
    }

    override fun asBitmapInto(imageView: ImageView) {
        asBitmapInto(imageView, null)
    }

    override fun asBitmapInto(imageView: ImageView, listener: RequestListener<Bitmap>?) {
        val bean = mGlideBuilderBean

        mRequestManager.asBitmap().load(bean.path).apply {
            this.apply(getRequestOptions(bean))
        }.apply {
            if (bean.crossFade) {
                this.transition(BitmapTransitionOptions().crossFade())
            }
            if (bean.animResId != -1) {
                this.transition(BitmapTransitionOptions().transition(bean.animResId))
            }
            if (bean.animator != null) {
                this.transition(BitmapTransitionOptions().transition(bean.animator!!))
            }
        }.apply {
            if (listener != null) {// 设置请求监听器
                this.listener(listener)
            }
        }.apply {
            this.into(imageView)
        }
    }

    override fun asBitmapInto(target: com.bumptech.glide.request.target.Target<Bitmap>) {
        asBitmapInto(target, null)
    }

    override fun asBitmapInto(target: com.bumptech.glide.request.target.Target<Bitmap>, listener: RequestListener<Bitmap>?) {
        val bean = mGlideBuilderBean

        mRequestManager.asBitmap().load(bean.path).apply {
            this.apply(getRequestOptions(bean))
        }.apply {
            if (bean.crossFade) {
                this.transition(BitmapTransitionOptions().crossFade())
            }
            if (bean.animResId != -1) {
                this.transition(BitmapTransitionOptions().transition(bean.animResId))
            }
            if (bean.animator != null) {
                this.transition(BitmapTransitionOptions().transition(bean.animator!!))
            }
        }.apply {
            if (listener != null) {// 设置请求监听器
                this.listener(listener)
            }
        }.apply {
            this.into(target)
        }
    }

    override fun asGifInto(imageView: ImageView) {
        asGifInto(imageView, null)
    }

    override fun asGifInto(imageView: ImageView, listener: RequestListener<GifDrawable>?) {
        val bean = mGlideBuilderBean

        mRequestManager.asGif().load(bean.path).apply {
            this.apply(getRequestOptions(bean))
        }.apply {
            if (bean.animResId != -1) {
                this.transition(GenericTransitionOptions<GifDrawable>().transition(bean.animResId))
            }
            if (bean.animator != null) {
                this.transition(GenericTransitionOptions<GifDrawable>().transition(bean.animator!!))
            }
        }.apply {
            if (listener != null) {// 设置请求监听器
                this.listener(listener)
            }
        }.apply {
            this.into(imageView)
        }
    }

    override fun asGifInto(target: com.bumptech.glide.request.target.Target<GifDrawable>) {
        asGifInto(target, null)
    }

    override fun asGifInto(target: com.bumptech.glide.request.target.Target<GifDrawable>, listener: RequestListener<GifDrawable>?) {
        val bean = mGlideBuilderBean

        mRequestManager.asGif().load(bean.path).apply {
            this.apply(getRequestOptions(bean))
        }.apply {
            if (bean.animResId != -1) {
                this.transition(GenericTransitionOptions<GifDrawable>().transition(bean.animResId))
            }
            if (bean.animator != null) {
                this.transition(GenericTransitionOptions<GifDrawable>().transition(bean.animator!!))
            }
        }.apply {
            if (listener != null) {// 设置请求监听器
                this.listener(listener)
            }
        }.apply {
            this.into(target)
        }
    }

    /** 获取请求配置项 */
    private fun getRequestOptions(bean: GlideBuilderBean): RequestOptions = RequestOptions().apply {
        if (bean.path is ByteArray) {
            this.signature(ObjectKey(UUID.randomUUID().toString()))// 修复glide加载byte数组图片无法缓存的BUG
        }
        this.placeholder(bean.placeholderResId)// 设置加载图
        this.error(bean.errorResId)// 设置加载失败图
        this.skipMemoryCache(!bean.saveToMemoryCache)// 设置跳过内存缓存

        // 设置磁盘缓存
        if (bean.isVideo) {//显示视频的第一帧需要设置为NONE
            this.diskCacheStrategy(DiskCacheStrategy.NONE)
        } else if (bean.path is File || bean.path is Int || bean.path is ByteArray) {
            this.diskCacheStrategy(DiskCacheStrategy.NONE)// 资源文件、本地手机存储的文件和byte数组不需要进行磁盘缓存
        } else {// 其他情况根据自定义缓存策略设置
            this.diskCacheStrategy(bean.diskCacheStrategy)
        }

        if (bean.widthPx > 0 && bean.heightPx > 0) {
            this.override(bean.widthPx, bean.heightPx)// 设置图片宽高
        }
        if (bean.centerCrop) {
            this.centerCrop()
        }
        if (bean.fitCenter) {
            this.fitCenter()
        }
        if (bean.centerInside) {
            this.centerInside()
        }
        if (bean.dontAnimate) {
            this.dontAnimate()
        }

        val list = getTransformationList(bean)
        if (list.isNotEmpty()) {
            this.transform(*list.toTypedArray())
        }
    }

    /** 获取图片变换 */
    private fun getTransformationList(bean: GlideBuilderBean): List<Transformation<Bitmap>> {
        val list = ArrayList<Transformation<Bitmap>>()
        if (bean.useBlur) {
            list.add(BlurTransformation(bean.blurRadius))
        }
        if (bean.useFilterColor) {
            list.add(ColorFilterTransformation(bean.filterColor))
        }
        if (bean.useRoundCorner) {
            if (bean.centerCrop) {//如果居中裁剪则使用裁剪后的圆角转换
                list.add(CropRoundedCornersTransformation(bean.roundCornerRadius, bean.roundedCornersMargin, bean.cornerType))
            } else {
                list.add(RoundedCornersTransformation(bean.roundCornerRadius, bean.roundedCornersMargin, bean.cornerType))
            }
        }
        if (bean.useGrayscale) {
            list.add(GrayscaleTransformation())
        }
        if (bean.useCircle) {
            list.add(CropCircleTransformation())
        }
        if (bean.useCropSquare) {
            list.add(CropSquareTransformation())
        }
        if (bean.useMask) {
            list.add(MaskTransformation(bean.maskResId))
        }
        return list
    }

}