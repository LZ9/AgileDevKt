package com.lodz.android.imageloaderkt

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.lodz.android.imageloaderkt.contract.ResourceContract
import com.lodz.android.imageloaderkt.glide.impl.GlideResource

/**
 * 图片加载器
 * Created by zhouL on 2018/7/11.
 */
object ImageLoader {

    /** 构造加载器，[any]必须是Context、FragmentActivity、Activity、Fragment、View中的一种 */
    @JvmStatic
    fun create(any: Any): ResourceContract {
        if (any is FragmentActivity) {
            return GlideResource.with(any)
        }
        if (any is Activity) {
            return GlideResource.with(any)
        }
        if (any is Context) {
            return GlideResource.with(any)
        }
        if (any is Fragment) {
            return GlideResource.with(any)
        }
        if (any is View) {
            return GlideResource.with(any)
        }
        throw RuntimeException("你传入的Any对象不属于Context、FragmentActivity、Activity、Fragment、View中的一种")
    }
}