package com.lodz.android.pandora.picker.file

import android.os.Environment
import androidx.lifecycle.DefaultLifecycleObserver
import com.lodz.android.pandora.picker.contract.OnImgLoader
import com.lodz.android.pandora.picker.contract.picker.OnFileClickListener
import com.lodz.android.pandora.picker.contract.picker.OnFilePickerListener
import com.lodz.android.pandora.picker.contract.picker.OnFilePreviewListener
import com.lodz.android.pandora.picker.preview.vh.DataPreviewAgent

/**
 * 选择数据
 * Created by zhouL on 2018/12/18.
 */
class PickerBean<T>(val pickType: Int) {

    /** 资源列表 */
    var sourceList: List<T>? = null
    /** 图片加载接口 */
    var imgLoader: OnImgLoader<T>? = null
    /** 文件回调接口 */
    var filePickerListener: OnFilePickerListener<T>? = null
    /** 文件预览接口 */
    var filePreviewListener: OnFilePreviewListener<T>? = null
    /** 文件点击接口 */
    var fileClickListener: OnFileClickListener<T>? = null
    /** 可选最大数量 */
    var maxCount = 9
    /** 是否需要相机功能 */
    var isNeedCamera = false
    /** 是否需要预览功能 */
    var isNeedPreview = true
    /** 是否需要底部信息栏 */
    var isNeedBottomInfo = true
    /** 公共目录名称 */
    var publicDirectoryName: String = Environment.DIRECTORY_PICTURES
    /** UI配置 */
    var pickerUIConfig = PickerUIConfig.createDefault()
    /** 7.0的FileProvider名字 */
    var authority = ""
    /** 图片预览view */
    var view: DataPreviewAgent<T>? = null
    /** 生命周期接口 */
    var lifecycleObserver: DefaultLifecycleObserver? = null
    /** 文件后缀类型 */
    var suffixArray: Array<out String> = arrayOf()
    /** 文件后缀类型 */
    var phoneAssemble: IntArray = intArrayOf()


    fun clear() {
        sourceList = null
        imgLoader = null
        view = null
        filePickerListener = null
        lifecycleObserver = null
        filePreviewListener = null
    }
}