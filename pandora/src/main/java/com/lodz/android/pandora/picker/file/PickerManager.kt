package com.lodz.android.pandora.picker.file

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import androidx.documentfile.provider.DocumentFile
import com.lodz.android.corekt.anko.deduplication
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.ToastUtils
import com.lodz.android.pandora.R
import com.lodz.android.pandora.event.PhotoPickerFinishEvent
import com.lodz.android.pandora.event.PicturePreviewFinishEvent
import com.lodz.android.pandora.picker.file.pick.FilePickerActivity
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
 * 照片选择器
 * Created by zhouL on 2018/12/19.
 */
class PickerManager<V : View, T : Any> internal constructor(private val pickerBean: PickerBean<V, T>) {

    companion object {

        /** 选取手机内文件 */
        @JvmStatic
        fun <V : View> pickPhoneDocument(): PickerBuilder<V, DocumentFile> {
            val pickerBean = PickerBean<V, DocumentFile>()
            pickerBean.isPickPhoneDocument = true
            return PickerBuilder(pickerBean)
        }

        /** 选取指定的文件列表[list] */
        @JvmStatic
        fun <V : View> pickFile(list: List<File>): PickerBuilder<V, File> = pickAny(list)

        /** 选取指定的文件数组[array] */
        @JvmStatic
        fun <V : View> pickFile(array: Array<File>): PickerBuilder<V, File> = pickAny(array)

        /** 选取指定的资源id列表[list] */
        @JvmStatic
        fun <V : View> pickResId(list: List<Int>): PickerBuilder<V, Int> = pickAny(list)

        /** 选取指定的资源id数组[array] */
        @JvmStatic
        fun <V : View> pickResId(array: Array<Int>): PickerBuilder<V, Int> = pickAny(array)

        /** 选取指定的url列表[list] */
        @JvmStatic
        fun <V : View> pickUrl(list: List<String>): PickerBuilder<V, String> = pickAny(list)

        /** 选取指定的url数组[array] */
        @JvmStatic
        fun <V : View> pickUrl(array: Array<String>): PickerBuilder<V, String> = pickAny(array)

        /** 选取指定的Uri列表[list] */
        @JvmStatic
        fun <V : View> pickUri(list: List<Uri>): PickerBuilder<V, Uri> = pickAny(list)

        /** 选取指定的Uri数组[array] */
        @JvmStatic
        fun <V : View> pickUri(array: Array<Uri>): PickerBuilder<V, Uri> = pickAny(array)

        /** 选取指定的任意类型数组[array] */
        @JvmStatic
        fun <V : View, T : Any> pickAny(array: Array<T>): PickerBuilder<V, T> = pickAny(array.toList())

        /** 选取指定的任意类型列表[list] */
        @JvmStatic
        fun <V : View, T : Any> pickAny(list: List<T>): PickerBuilder<V, T> {
            val pickerBean = PickerBean<V, T>()
            pickerBean.isPickPhoneDocument = false
            pickerBean.sourceList = list
            return PickerBuilder(pickerBean)
        }
    }

    /** 打开选择器，上下文[context]，Intent启动标记[flags] */
    @JvmOverloads
    fun open(context: Context, flags: List<Int>? = null): PickerManager<V, T> {
        if (pickerBean.imgLoader == null) {// 校验图片加载器
            context.toastShort(R.string.pandora_picker_loader_unset)
            return this
        }
        if (pickerBean.imgView == null) {// 校验图片预览控件
            ToastUtils.showShort(context, R.string.pandora_preview_img_unset)
            return this
        }
        if (pickerBean.filePickerListener == null) {// 校验文件选中回调监听
            context.toastShort(R.string.pandora_picker_selected_listener_unset)
            return this
        }
        val list = pickerBean.sourceList
        if (!pickerBean.isPickPhoneDocument && list.isNullOrEmpty()) {// 校验指定文件数据列表
            context.toastShort(R.string.pandora_picker_source_list_empty)
            return this
        }
        if (pickerBean.isPickPhoneDocument && pickerBean.mimeTypeArray.isEmpty()) {//校验挑选手机文件时mimeType是否设置
            context.toastShort(R.string.pandora_picker_mime_type_empty)
            return this
        }

        if (pickerBean.maxCount < 1) {// 修正最大选择数
            pickerBean.maxCount = 1
        }

        if (!pickerBean.isPickPhoneDocument && list != null) {//挑选指定文件
            pickerBean.sourceList = list.deduplication().toList()// 对指定的文件列表去重
            pickerBean.isNeedCamera = false// 不允许使用拍照模式
        }

        if (pickerBean.isNeedCamera && pickerBean.publicDirectoryName.isEmpty()) {// 开启拍照功能要校验公共目录路径
            val path = Environment.getExternalStoragePublicDirectory(pickerBean.publicDirectoryName)?.absolutePath ?: ""
            if (path.isEmpty()) {// 校验公共目录路径
                context.toastShort(R.string.pandora_picker_public_directory_empty)
                return this
            }
        }

        //开启拍照功能 && 当前系统是7.0以上且没有配置FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && pickerBean.isNeedCamera && pickerBean.authority.isEmpty()) {
            context.toastShort(R.string.pandora_picker_authority_empty)
            return this
        }
        FilePickerActivity.start(context, pickerBean, flags)
        return this
    }

    /** 手动关闭选择器 */
    fun finishPickActivity(){
        EventBus.getDefault().post(PicturePreviewFinishEvent())
        EventBus.getDefault().post(PhotoPickerFinishEvent())
    }
}