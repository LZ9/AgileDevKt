package com.lodz.android.pandora.picker.file

import android.os.Environment
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import com.lodz.android.pandora.picker.contract.OnImgLoader
import com.lodz.android.pandora.picker.contract.picker.OnFilePickerListener
import com.lodz.android.pandora.picker.preview.AbsImageView

/**
 * 选择数据
 * Created by zhouL on 2018/12/18.
 */
class PickerBean<V : View, T : Any> {

    /** 资源列表 */
    var sourceList: List<T>? = null
    /** 图片加载接口 */
    var imgLoader: OnImgLoader<T>? = null
    /** 文件回调接口 */
    var filePickerListener: OnFilePickerListener<T>? = null
    /** 可选最大数量 */
    var maxCount = 9
    /** 是否需要相机功能 */
    var isNeedCamera = false
    /** 是否需要预览功能 */
    var isNeedPreview = true
    /** 公共目录名称 */
    var publicDirectoryName: String = Environment.DIRECTORY_PICTURES
    /** UI配置 */
    var pickerUIConfig = PickerUIConfig.createDefault()
    /** 7.0的FileProvider名字 */
    var authority = ""
    /** 是否挑选手机的文件，false挑选指定文件 */
    var isPickPhoneDocument= true
    /** 图片控件 */
    var imgView: AbsImageView<V, T>? = null
    /** 生命周期接口 */
    var lifecycleObserver: DefaultLifecycleObserver? = null
    /** 文件类型 */
    var mimeTypeArray: Array<out String> = arrayOf()

    fun clear() {
        sourceList = null
        imgLoader = null
        imgView = null
        filePickerListener = null
        lifecycleObserver = null
    }
}