package com.lodz.android.imageloaderkt

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import com.lodz.android.imageloaderkt.contract.ImageLoaderContract
import com.lodz.android.imageloaderkt.glide.impl.GlideImageLoader

/**
 * 图片加载器
 * Created by zhouL on 2018/7/11.
 */
object ImageLoader {

    /** 构造加载器，[any]必须是Context、FragmentActivity、Activity、Fragment、View中的一种 */
    fun create(any: Any): ImageLoaderContract {
        if (any is FragmentActivity){
            return GlideImageLoader.with(any)
        }
        if (any is Activity){
            return GlideImageLoader.with(any)
        }
        if (any is Context){
            return GlideImageLoader.with(any)
        }
        if (any is Fragment){
            return GlideImageLoader.with(any)
        }
        if (any is View){
            return GlideImageLoader.with(any)
        }
        throw RuntimeException("你传入的Any对象不属于Context、FragmentActivity、Activity、Fragment、View中的一种")
    }
}