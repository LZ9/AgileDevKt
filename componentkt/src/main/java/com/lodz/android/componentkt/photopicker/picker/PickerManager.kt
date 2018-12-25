package com.lodz.android.componentkt.photopicker.picker

import android.content.Context
import android.os.Build
import android.os.Environment
import com.lodz.android.componentkt.R
import com.lodz.android.componentkt.photopicker.picker.pick.PhotoPickerActivity
import com.lodz.android.componentkt.photopicker.picker.take.TakePhotoActivity
import com.lodz.android.corekt.album.AlbumUtils
import com.lodz.android.corekt.anko.deduplication
import com.lodz.android.corekt.utils.toastShort
import java.io.File

/**
 * 照片选择器
 * Created by zhouL on 2018/12/19.
 */
class PickerManager internal constructor(private val pickerBean: PickerBean) {

    companion object {
        fun create(): PickerBuilder = PickerBuilder()
    }

    /** 打开选择器，上下文[context] */
    fun open(context: Context) {
        if (pickerBean.photoLoader == null) {// 校验图片加载器
            context.toastShort(R.string.componentkt_photo_loader_unset)
            return
        }
        if (pickerBean.previewLoader == null) {
            pickerBean.previewLoader = pickerBean.photoLoader
        }
        if (pickerBean.photoPickerListener == null) {// 校验图片选中回调监听
            context.toastShort(R.string.componentkt_photo_selected_listener_unset)
            return
        }
        if (!pickerBean.isPickAllPhoto && pickerBean.sourceList.isNullOrEmpty()) {// 校验指定图片数据列表
            context.toastShort(R.string.componentkt_photo_source_list_empty)
            return
        }
        if (pickerBean.isPickAllPhoto && !pickerBean.isNeedCamera && AlbumUtils.getAllImages(context).isEmpty()) {//未开启拍照时校验手机内是否有图片
            context.toastShort(R.string.componentkt_photo_source_list_empty)
            return
        }
        if (pickerBean.maxCount < 1) {// 修正最大选择数
            pickerBean.maxCount = 1
        }
        if (!pickerBean.isPickAllPhoto) {//挑选指定图片
            pickerBean.sourceList = pickerBean.sourceList!!.deduplication().toList()// 对指定的图片列表去重
            pickerBean.isNeedCamera = false// 不允许使用拍照模式
        }
        if (pickerBean.isNeedCamera && pickerBean.cameraSavePath.isEmpty()) {// 开启拍照功能要校验拍照保存地址
            pickerBean.cameraSavePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
        }
        if (pickerBean.isNeedCamera && !pickerBean.cameraSavePath.endsWith(File.separator)) {//补全地址
            pickerBean.cameraSavePath += File.separator
        }
        //开启拍照功能 && 当前系统是7.0以上且没有配置FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && pickerBean.isNeedCamera && pickerBean.authority.isEmpty()) {
            context.toastShort(R.string.componentkt_photo_authority_empty)
            return
        }
        PhotoPickerActivity.start(context, pickerBean)
    }

    /** 拍照，上下文[context]，是否立即返回结果[isImmediately]（默认true） */
    fun takePhoto(context: Context, isImmediately: Boolean = true) {
        if (pickerBean.previewLoader == null && !isImmediately) {//没有立即返回需要校验预览图片加载器
            context.toastShort(R.string.componentkt_preview_loader_unset)
            return
        }
        if (pickerBean.photoPickerListener == null) {// 校验图片选中回调监听
            context.toastShort(R.string.componentkt_photo_selected_listener_unset)
            return
        }
        if (pickerBean.cameraSavePath.isEmpty()) {// 校验拍照保存地址
            pickerBean.cameraSavePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
        }
        if (!pickerBean.cameraSavePath.endsWith(File.separator)) {//补全地址
            pickerBean.cameraSavePath += File.separator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && pickerBean.authority.isEmpty()) {//当前系统是7.0以上且没有配置FileProvider
            context.toastShort(R.string.componentkt_photo_authority_empty)
            return
        }
        pickerBean.isImmediately = isImmediately// 设置是否立即返回结果
        TakePhotoActivity.start(context, pickerBean)
    }
}