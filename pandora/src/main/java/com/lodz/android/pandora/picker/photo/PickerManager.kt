package com.lodz.android.pandora.picker.photo

import android.content.Context
import android.os.Build
import android.os.Environment
import android.view.View
import com.lodz.android.corekt.album.AlbumUtils
import com.lodz.android.corekt.anko.deduplication
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.ToastUtils
import com.lodz.android.pandora.R
import com.lodz.android.pandora.event.PhotoPickerFinishEvent
import com.lodz.android.pandora.event.PicturePreviewFinishEvent
import com.lodz.android.pandora.picker.photo.pick.PhotoPickerActivity
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
 * 照片选择器
 * Created by zhouL on 2018/12/19.
 */
class PickerManager<V : View> internal constructor(private val pickerBean: PickerBean<V>) {

    companion object {
        @JvmStatic
        fun <V : View> create(): PickerBuilder<V> = PickerBuilder()
    }

    /** 打开选择器，上下文[context]，Intent启动标记[flags] */
    @JvmOverloads
    fun open(context: Context, flags: List<Int>? = null): PickerManager<V> {
        if (pickerBean.imgLoader == null) {// 校验图片加载器
            context.toastShort(R.string.pandora_photo_loader_unset)
            return this
        }
        if (pickerBean.imgView == null) {// 校验图片加载器
            ToastUtils.showShort(context, R.string.pandora_preview_img_unset)
            return this
        }
        if (pickerBean.photoPickerListener == null) {// 校验图片选中回调监听
            context.toastShort(R.string.pandora_photo_selected_listener_unset)
            return this
        }
        if (!pickerBean.isPickAllPhoto && pickerBean.sourceList.isNullOrEmpty()) {// 校验指定图片数据列表
            context.toastShort(R.string.pandora_photo_source_list_empty)
            return this
        }
        if (pickerBean.isPickAllPhoto && !pickerBean.isNeedCamera && AlbumUtils.getAllImages(context).isEmpty()) {//未开启拍照时校验手机内是否有图片
            context.toastShort(R.string.pandora_photo_source_list_empty)
            return this
        }
        if (pickerBean.maxCount < 1) {// 修正最大选择数
            pickerBean.maxCount = 1
        }
        if (!pickerBean.isPickAllPhoto) {//挑选指定图片
            pickerBean.sourceList = pickerBean.sourceList!!.deduplication().toList()// 对指定的图片列表去重
            pickerBean.isNeedCamera = false// 不允许使用拍照模式
        }
        if (pickerBean.isNeedCamera && pickerBean.cameraSavePath.isEmpty()) {// 开启拍照功能要校验拍照保存地址
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                pickerBean.cameraSavePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath ?: ""
            }
            if (pickerBean.cameraSavePath.isEmpty()){
                pickerBean.cameraSavePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
            }
        }
        if (pickerBean.isNeedCamera && !pickerBean.cameraSavePath.endsWith(File.separator)) {//补全地址
            pickerBean.cameraSavePath += File.separator
        }
        //开启拍照功能 && 当前系统是7.0以上且没有配置FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && pickerBean.isNeedCamera && pickerBean.authority.isEmpty()) {
            context.toastShort(R.string.pandora_photo_authority_empty)
            return this
        }
        PhotoPickerActivity.start(context, pickerBean, flags)
        return this
    }

    /** 手动关闭选择器 */
    fun finishPickActivity(){
        EventBus.getDefault().post(PicturePreviewFinishEvent())
        EventBus.getDefault().post(PhotoPickerFinishEvent())
    }
}