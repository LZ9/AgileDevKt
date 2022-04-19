package com.lodz.android.pandora.picker.take

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.documentfile.provider.DocumentFile
import com.lodz.android.corekt.anko.getColorCompat
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
    fun setOnImgLoader(imgLoader: OnImgLoader<DocumentFile>?): TakeBuilder {
        takeBean.imgLoader = imgLoader
        return this
    }

    /** 拍照回调[listener] */
    fun setOnPhotoTakeListener(listener: OnPhotoTakeListener?): TakeBuilder {
        takeBean.photoTakeListener = listener
        return this
    }

    /** 设置公共目录名称[directoryName]，默认值 Environment.DIRECTORY_PICTURES */
    fun setPublicDirectoryName(directoryName: String): TakeBuilder {
        if (directoryName.isNotEmpty()){
            takeBean.publicDirectoryName = directoryName
        }
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
    fun setStatusBarColor(@ColorInt color: Int): TakeBuilder {
        takeBean.statusBarColor = color
        return this
    }

    /** 设置顶部状态栏颜色[color] */
    fun setStatusBarColor(context: Context, @ColorRes color: Int): TakeBuilder = setStatusBarColor(context.getColorCompat(color))

    /** 设置底部导航栏颜色[color] */
    fun setNavigationBarColor(@ColorInt color: Int): TakeBuilder {
        takeBean.navigationBarColor = color
        return this
    }

    /** 设置底部导航栏颜色[color] */
    fun setNavigationBarColor(context: Context, @ColorRes color: Int): TakeBuilder = setNavigationBarColor(context.getColorCompat(color))

    /** 设置预览页背景色[color] */
    fun setPreviewBgColor(@ColorInt color: Int): TakeBuilder {
        takeBean.previewBgColor = color
        return this
    }

    /** 设置预览页背景色[color] */
    fun setPreviewBgColor(context: Context, @ColorRes color: Int): TakeBuilder = setPreviewBgColor(context.getColorCompat(color))

    /** 完成构建（选择手机里的全部图片） */
    fun build(): TakePhotoManager {
        return TakePhotoManager(takeBean)
    }

}