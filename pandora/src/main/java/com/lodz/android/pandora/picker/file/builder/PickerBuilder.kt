package com.lodz.android.pandora.picker.file.builder

import android.content.Context
import android.os.Build
import android.os.Environment
import android.view.View
import androidx.annotation.IntRange
import androidx.lifecycle.DefaultLifecycleObserver
import com.lodz.android.corekt.anko.deduplication
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.ToastUtils
import com.lodz.android.pandora.R
import com.lodz.android.pandora.picker.contract.OnImgLoader
import com.lodz.android.pandora.picker.contract.picker.OnFilePickerListener
import com.lodz.android.pandora.picker.file.PickerBean
import com.lodz.android.pandora.picker.file.PickerManager
import com.lodz.android.pandora.picker.file.PickerUIConfig
import com.lodz.android.pandora.picker.file.pick.any.AnyPickerActivity
import com.lodz.android.pandora.picker.preview.AbsImageView

/**
 * 文件选择构建类
 * Created by zhouL on 2018/12/19.
 */
open class PickerBuilder<V : View, T : Any> constructor(private val pickerBean: PickerBean<V, T>) {

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

    /** 打开选择器，上下文[context]，Intent启动标记[flags] */
    open fun open(context: Context, flags: List<Int>? = null): PickerManager {
        val manager = PickerManager()

        if (pickerBean.imgLoader == null) {// 校验图片加载器
            context.toastShort(R.string.pandora_picker_loader_unset)
            return manager
        }
        if (pickerBean.imgView == null) {// 校验图片预览控件
            ToastUtils.showShort(context, R.string.pandora_preview_img_unset)
            return manager
        }
        if (pickerBean.filePickerListener == null) {// 校验文件选中回调监听
            context.toastShort(R.string.pandora_picker_selected_listener_unset)
            return manager
        }
        val type = pickerBean.pickType // 挑选类型
        if (type == PickerManager.PICK_PHONE_SUFFIX && pickerBean.suffixArray.isEmpty()) {//校验后缀是否设置
            context.toastShort(R.string.pandora_picker_suffix_unset)
            return manager
        }

        val list = pickerBean.sourceList
        if (type == PickerManager.PICK_ANY || type == PickerManager.PICK_FILE || type == PickerManager.PICK_URI
            || type == PickerManager.PICK_RES_ID || type == PickerManager.PICK_IMAGE_URL) {//挑选指定文件
            if (list.isNullOrEmpty()) {
                context.toastShort(R.string.pandora_picker_source_list_empty)
                return manager
            }
            pickerBean.sourceList = list.deduplication().toList()// 对指定的文件列表去重
            pickerBean.isNeedCamera = false// 不允许使用拍照模式
        }

        if (pickerBean.maxCount < 1) {// 修正最大选择数
            pickerBean.maxCount = 1
        }

        if (pickerBean.isNeedCamera && pickerBean.publicDirectoryName.isEmpty()) {// 开启拍照功能要校验公共目录路径
            val path = Environment.getExternalStoragePublicDirectory(pickerBean.publicDirectoryName)?.absolutePath ?: ""
            if (path.isEmpty()) {// 校验公共目录路径
                context.toastShort(R.string.pandora_picker_public_directory_empty)
                return manager
            }
        }

        //开启拍照功能 && 当前系统是7.0以上且没有配置FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && pickerBean.isNeedCamera && pickerBean.authority.isEmpty()) {
            context.toastShort(R.string.pandora_picker_authority_empty)
            return manager
        }
        startActivity(context, flags)
        return manager
    }

    /** 跳转指定的Activity */
    protected open fun startActivity(context: Context, flags: List<Int>? = null) {
        AnyPickerActivity.start(context, pickerBean, flags)
    }
}