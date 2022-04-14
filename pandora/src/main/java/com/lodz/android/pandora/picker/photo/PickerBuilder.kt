package com.lodz.android.pandora.picker.photo

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.IntRange
import androidx.lifecycle.DefaultLifecycleObserver
import com.lodz.android.corekt.album.PicInfo
import com.lodz.android.pandora.picker.contract.OnImgLoader
import com.lodz.android.pandora.picker.contract.picker.OnPhotoPickerListener
import com.lodz.android.pandora.picker.preview.AbsImageView

/**
 * 照片选择构建类
 * Created by zhouL on 2018/12/19.
 */
class PickerBuilder<V : View> {

    /** 图片数据 */
    private val pickerBean = PickerBean<V>()

    /** 设置图片加载器[imgLoader] */
    fun setImgLoader(imgLoader: OnImgLoader<PicInfo>): PickerBuilder<V> {
        pickerBean.imgLoader = imgLoader
        return this
    }

    /** 设置图片加载器[imgLoader] */
    fun setImgLoader(imgLoader: (context: Context, source: PicInfo, imageView: ImageView) -> Unit): PickerBuilder<V> =
        setImgLoader(OnImgLoader<PicInfo> { context, source, imageView ->
            imgLoader.invoke(context, source, imageView)
        })

    /** 设置图片选中回调[listener] */
    fun setOnPhotoPickerListener(listener: OnPhotoPickerListener): PickerBuilder<V> {
        pickerBean.photoPickerListener = listener
        return this
    }

    /** 设置生命周期观察者[observer] */
    fun setOnLifecycleObserver(observer: DefaultLifecycleObserver): PickerBuilder<V> {
        pickerBean.lifecycleObserver = observer
        return this
    }

    /** 设置图片可选最大数量[maxCount] */
    fun setMaxCount(@IntRange(from = 1) maxCount: Int): PickerBuilder<V> {
        if (maxCount > 0) {
            pickerBean.maxCount = maxCount
        }
        return this
    }

    /** 设置是否需要相机功能[needCamera] */
    fun setNeedCamera(needCamera: Boolean): PickerBuilder<V> {
        pickerBean.isNeedCamera = needCamera
        return this
    }

    /** 设置是否需要item的预览功能[needItemPreview] */
    fun setNeedItemPreview(needItemPreview: Boolean): PickerBuilder<V> {
        pickerBean.isNeedItemPreview = needItemPreview
        return this
    }

    /** 设置拍照保存地址[savePath] */
    fun setCameraSavePath(savePath: String): PickerBuilder<V> {
        pickerBean.cameraSavePath = savePath
        return this
    }

    /** 设置选择器的界面配置[config] */
    fun setPickerUIConfig(config: PickerUIConfig): PickerBuilder<V> {
        pickerBean.pickerUIConfig = config
        return this
    }

    /** 设置7.0的FileProvider名字[authority] */
    fun setAuthority(authority: String): PickerBuilder<V> {
        pickerBean.authority = authority
        return this
    }

    /** 设置图片控件[view] */
    fun setImageView(view: AbsImageView<V, PicInfo>): PickerBuilder<V> {
        pickerBean.imgView = view
        return this
    }

    /** 完成构建（选择手机里的全部图片） */
    fun build(): PickerManager<V> {
        pickerBean.isPickAllPhoto = true
        return PickerManager(pickerBean)
    }

    /** 完成构建（选择指定的图片） */
    fun build(sourceList: List<String>): PickerManager<V> {
        val infos = ArrayList<PicInfo>()
        for (url in sourceList) {
            infos.add(PicInfo(url, Uri.EMPTY))
        }
        pickerBean.isPickAllPhoto = false
        pickerBean.sourceList = infos
        return PickerManager(pickerBean)
    }

    /** 完成构建（选择指定的图片） */
    fun build(sourceArray: Array<String>): PickerManager<V> {
        val infos = ArrayList<PicInfo>()
        for (url in sourceArray) {
            infos.add(PicInfo(url, Uri.EMPTY))
        }
        pickerBean.isPickAllPhoto = false
        pickerBean.sourceList = infos
        return PickerManager(pickerBean)
    }

}