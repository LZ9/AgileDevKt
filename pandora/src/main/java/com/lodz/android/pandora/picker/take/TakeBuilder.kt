package com.lodz.android.pandora.picker.take

import android.content.Context
import android.widget.ImageView
import androidx.annotation.ColorRes
import com.lodz.android.pandora.picker.contract.OnImgLoader
import com.lodz.android.pandora.picker.contract.take.OnPhotoTakeListener

/**
 * 拍照构建
 * Created by zhouL on 2018/12/25.
 */
class TakeBuilder {

    /** 图片数据 */
    private val takeBean = TakeBean()

    /** 设置预览图图加载器[imgLoader] */
    fun setOnImgLoader(imgLoader: OnImgLoader<String>?): TakeBuilder {
        takeBean.imgLoader = imgLoader
        return this
    }

    /** 设置预览图图加载器[imgLoader] */
    fun setOnImgLoader(imgLoader: (context: Context, source: String, imageView: ImageView) -> Unit): TakeBuilder =
        setOnImgLoader(OnImgLoader<String> { context, source, imageView ->
            imgLoader.invoke(context, source, imageView)
        })

    /** 拍照回调[listener] */
    fun setOnPhotoTakeListener(listener: OnPhotoTakeListener?): TakeBuilder {
        takeBean.photoTakeListener = listener
        return this
    }

    /** 设置拍照保存地址[savePath] */
    fun setCameraSavePath(savePath: String): TakeBuilder {
        takeBean.cameraSavePath = savePath
        return this
    }

    /** 设置7.0的FileProvider名字[authority] */
    fun setAuthority(authority: String): TakeBuilder {
        takeBean.authority = authority
        return this
    }

    /** 设置拍照后立即返回结果[isImmediately] */
    fun setImmediately(isImmediately: Boolean): TakeBuilder {
        takeBean.isImmediately = isImmediately
        return this
    }

    /** 设置顶部状态栏颜色[color] */
    fun setStatusBarColor(@ColorRes color: Int): TakeBuilder {
        if (color != 0) {
            takeBean.statusBarColor = color
        }
        return this
    }

    /** 设置底部导航栏颜色[color] */
    fun setNavigationBarColor(@ColorRes color: Int): TakeBuilder {
        if (color != 0) {
            takeBean.navigationBarColor = color
        }
        return this
    }

    /** 设置预览页背景色[color] */
    fun setPreviewBgColor(@ColorRes color: Int): TakeBuilder {
        if (color != 0) {
            takeBean.previewBgColor = color
        }
        return this
    }

    /** 完成构建（选择手机里的全部图片） */
    fun build(): TakePhotoManager {
        return TakePhotoManager(takeBean)
    }

}