package com.lodz.android.pandora.photopicker.picker

import android.content.Context
import android.widget.ImageView
import androidx.annotation.IntRange
import com.lodz.android.pandora.photopicker.contract.OnPhotoLoader
import com.lodz.android.pandora.photopicker.contract.picker.OnPhotoPickerListener

/**
 * 照片选择构建类
 * Created by zhouL on 2018/12/19.
 */
class PickerBuilder {

    /** 图片数据 */
    private val pickerBean = PickerBean()

    /** 设置图片加载器[photoLoader] */
    fun setImgLoader(photoLoader: OnPhotoLoader<String>): PickerBuilder {
        pickerBean.photoLoader = photoLoader
        return this
    }

    /** 设置图片加载器[photoLoader] */
    fun setImgLoader(photoLoader: (context: Context, source: String, imageView: ImageView) -> Unit): PickerBuilder =
            setImgLoader(object : OnPhotoLoader<String> {
                override fun displayImg(context: Context, source: String, imageView: ImageView) {
                    photoLoader.invoke(context, source, imageView)
                }
            })

    /** 设置预览图图加载器[previewLoader] */
    fun setPreviewImgLoader(previewLoader: OnPhotoLoader<String>): PickerBuilder {
        pickerBean.previewLoader = previewLoader
        return this
    }

    /** 设置预览图图加载器[previewLoader] */
    fun setPreviewImgLoader(previewLoader: (context: Context, source: String, imageView: ImageView) -> Unit): PickerBuilder =
            setPreviewImgLoader(object : OnPhotoLoader<String> {
                override fun displayImg(context: Context, source: String, imageView: ImageView) {
                    previewLoader.invoke(context, source, imageView)
                }
            })

    /** 设置图片选中回调[listener] */
    fun setOnPhotoPickerListener(listener: OnPhotoPickerListener): PickerBuilder {
        pickerBean.photoPickerListener = listener
        return this
    }

    /** 设置图片选中回调[listener] */
    fun setOnPhotoPickerListener(listener: (photos: List<String>) -> Unit): PickerBuilder =
            setOnPhotoPickerListener(object : OnPhotoPickerListener {
                override fun onPickerSelected(photos: List<String>) {
                    listener.invoke(photos)
                }
            })

    /** 设置图片可选最大数量[maxCount] */
    fun setMaxCount(@IntRange(from = 1) maxCount: Int): PickerBuilder {
        if (maxCount > 0) {
            pickerBean.maxCount = maxCount
        }
        return this
    }

    /** 设置是否需要相机功能[needCamera] */
    fun setNeedCamera(needCamera: Boolean): PickerBuilder {
        pickerBean.isNeedCamera = needCamera
        return this
    }

    /** 设置是否需要item的预览功能[needItemPreview] */
    fun setNeedItemPreview(needItemPreview: Boolean): PickerBuilder {
        pickerBean.isNeedItemPreview = needItemPreview
        return this
    }

    /** 设置拍照保存地址[savePath] */
    fun setCameraSavePath(savePath: String): PickerBuilder {
        pickerBean.cameraSavePath = savePath
        return this
    }

    /** 设置选择器的界面配置[config] */
    fun setPickerUIConfig(config: PickerUIConfig): PickerBuilder {
        pickerBean.pickerUIConfig = config
        return this
    }

    /** 设置7.0的FileProvider名字[authority] */
    fun setAuthority(authority: String): PickerBuilder {
        pickerBean.authority = authority
        return this
    }

    /** 设置预览是否缩放图片[isScale] */
    fun setScale(isScale: Boolean): PickerBuilder {
        pickerBean.isScale = isScale
        return this
    }

    /** 设置是否点击关闭预览[isClickClosePreview] */
    fun setClickClosePreview(isClickClosePreview: Boolean): PickerBuilder {
        pickerBean.isClickClosePreview = isClickClosePreview
        return this
    }

    /** 完成构建（选择手机里的全部图片） */
    fun build(): PickerManager {
        pickerBean.isPickAllPhoto = true
        return PickerManager(pickerBean)
    }

    /** 完成构建（选择指定的图片） */
    fun build(sourceList: List<String>): PickerManager {
        pickerBean.isPickAllPhoto = false
        pickerBean.sourceList = sourceList
        return PickerManager(pickerBean)
    }

    /** 完成构建（选择指定的图片） */
    fun build(sourceArray: Array<String>): PickerManager {
        pickerBean.isPickAllPhoto = false
        pickerBean.sourceList = sourceArray.toList()
        return PickerManager(pickerBean)
    }

}