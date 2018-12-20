package com.lodz.android.componentkt.photopicker.picker

import androidx.annotation.IntRange
import com.lodz.android.componentkt.photopicker.contract.OnPhotoLoader
import com.lodz.android.componentkt.photopicker.contract.picker.OnPhotoPickerListener

/**
 * 照片选择管理类
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

    /** 设置预览图图加载器[previewLoader] */
    fun setPreviewImgLoader(previewLoader: OnPhotoLoader<String>): PickerBuilder {
        pickerBean.previewLoader = previewLoader
        return this
    }

    /** 设置图片选中回调[listener] */
    fun setOnPhotoPickerListener(listener: OnPhotoPickerListener): PickerBuilder {
        pickerBean.photoPickerListener = listener
        return this
    }

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