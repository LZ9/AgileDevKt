package com.lodz.android.pandora.picker.file

import android.view.View
import androidx.annotation.IntRange
import androidx.lifecycle.DefaultLifecycleObserver
import com.lodz.android.pandora.picker.contract.OnImgLoader
import com.lodz.android.pandora.picker.contract.picker.OnFilePickerListener
import com.lodz.android.pandora.picker.preview.AbsImageView

/**
 * 文件选择构建类
 * Created by zhouL on 2018/12/19.
 */
class PickerBuilder<V : View, T : Any> constructor(private val pickerBean: PickerBean<V, T>) {

    /** 设置图片加载器[imgLoader] */
    fun setImgLoader(imgLoader: OnImgLoader<T>): PickerBuilder<V, T> {
        pickerBean.imgLoader = imgLoader
        return this
    }

    /** 设置文件选中回调[listener] */
    fun setOnFilePickerListener(listener: OnFilePickerListener<T>): PickerBuilder<V, T> {
        pickerBean.filePickerListener = listener
        return this
    }

    /** 设置生命周期观察者[observer] */
    fun setOnLifecycleObserver(observer: DefaultLifecycleObserver): PickerBuilder<V, T> {
        pickerBean.lifecycleObserver = observer
        return this
    }

    /** 设置图片可选最大数量[maxCount] */
    fun setMaxCount(@IntRange(from = 1) maxCount: Int): PickerBuilder<V, T> {
        if (maxCount > 0) {
            pickerBean.maxCount = maxCount
        }
        return this
    }

    /** 设置是否需要相机功能[needCamera]，公共目录名称[directoryName]，不设置默认值为Environment.DIRECTORY_PICTURES */
    @JvmOverloads
    fun setNeedCamera(needCamera: Boolean, directoryName: String = ""): PickerBuilder<V, T> {
        pickerBean.isNeedCamera = needCamera
        if (directoryName.isNotEmpty()){
            pickerBean.publicDirectoryName = directoryName
        }
        return this
    }

    /** 设置是否需要预览功能[needPreview] */
    fun setNeedPreview(needPreview: Boolean): PickerBuilder<V, T> {
        pickerBean.isNeedPreview = needPreview
        return this
    }

    /** 设置选择器的界面配置[config] */
    fun setPickerUIConfig(config: PickerUIConfig): PickerBuilder<V, T> {
        pickerBean.pickerUIConfig = config
        return this
    }

    /** 设置7.0的FileProvider名字[authority] */
    fun setAuthority(authority: String): PickerBuilder<V, T> {
        pickerBean.authority = authority
        return this
    }

    /** 设置图片控件[view] */
    fun setImageView(view: AbsImageView<V, T>): PickerBuilder<V, T> {
        pickerBean.imgView = view
        return this
    }

    /** 设置文件类型[array]，例如：PickerManager.MIME_TYPE_IMAGE */
    fun setMimeType(vararg array: String): PickerBuilder<V, T> {
        pickerBean.mimeTypeArray = array
        return this
    }

    /** 完成构建 */
    fun build(): PickerManager<V, T> {
        return PickerManager(pickerBean)
    }

}