package com.lodz.android.pandora.picker.file

import android.net.Uri
import android.view.View
import com.lodz.android.corekt.file.DocumentWrapper
import com.lodz.android.pandora.event.PhotoPickerFinishEvent
import com.lodz.android.pandora.event.PicturePreviewFinishEvent
import com.lodz.android.pandora.picker.file.builder.*
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
 * 照片选择器
 * Created by zhouL on 2018/12/19.
 */
class PickerManager {
    companion object {

        /** 选取手机组合数据 */
        const val PICK_PHONE_ASSEMBLE = 0
        /** 选取手机相册图片 */
        const val PICK_PHONE_ALBUM = 1
        /** 选取手机视频 */
        const val PICK_PHONE_VIDEO = 2
        /** 选取手机音频 */
        const val PICK_PHONE_AUDIO = 3
        /** 选取指定后缀文件 */
        const val PICK_PHONE_SUFFIX = 4
        /** 选取指定的任意类型 */
        const val PICK_ANY = 5
        /** 选取指定的文件 */
        const val PICK_FILE = 6
        /** 选取指定的Uri */
        const val PICK_URI = 7
        /** 选取指定的资源id */
        const val PICK_RES_ID = 8
        /** 选取指定的url */
        const val PICK_IMAGE_URL = 9

        /** 选取手机组合文件（图片、视频、音频） */
        @JvmStatic
        fun <V : View> pickPhoneAssemble(vararg types: Int = intArrayOf(PICK_PHONE_ALBUM, PICK_PHONE_VIDEO, PICK_PHONE_AUDIO)): DocumentPickerBuilder<V> {
            var checkParam = true
            for (t in types) {
                if (t != PICK_PHONE_ALBUM && t != PICK_PHONE_VIDEO && t != PICK_PHONE_AUDIO) {
                    checkParam = false
                    break
                }
            }
            if (!checkParam) {
                throw IllegalArgumentException("types does not belong PickerManager.PICK_PHONE_ALBUM or PickerManager.PICK_PHONE_VIDEO or PickerManager.PICK_PHONE_AUDIO")
            }
            val bean = PickerBean<V, DocumentWrapper>(PICK_PHONE_ASSEMBLE)
            bean.phoneAssemble = types
            return DocumentPickerBuilder(bean)
        }

        /** 选取手机相册图片 */
        @JvmStatic
        fun <V : View> pickPhoneAlbum(): DocumentPickerBuilder<V> = DocumentPickerBuilder(PickerBean(PICK_PHONE_ALBUM))

        /** 选取手机视频 */
        @JvmStatic
        fun <V : View> pickPhoneVideo(): DocumentPickerBuilder<V> = DocumentPickerBuilder(PickerBean(PICK_PHONE_VIDEO))

        /** 选取手机音频 */
        @JvmStatic
        fun <V : View> pickPhoneAudio(): DocumentPickerBuilder<V> = DocumentPickerBuilder(PickerBean(PICK_PHONE_AUDIO))

        /** 选取指定后缀[suffix]文件，例如.jpg */
        @JvmStatic
        fun <V : View> pickPhoneBySuffix(vararg suffix: String): DocumentPickerBuilder<V> {
            val pickerBean = PickerBean<V, DocumentWrapper>(PICK_PHONE_SUFFIX)
            pickerBean.suffixArray = suffix
            return DocumentPickerBuilder(pickerBean)
        }

        /** 选取指定的任意类型列表[list] */
        @JvmStatic
        fun <V : View, T : Any> pickAny(list: List<T>): PickerBuilder<V, T> {
            val pickerBean = PickerBean<V, T>(PICK_ANY)
            pickerBean.sourceList = list
            return PickerBuilder(pickerBean)
        }

        /** 选取指定的任意类型数组[array] */
        @JvmStatic
        fun <V : View, T : Any> pickAny(array: Array<T>): PickerBuilder<V, T> = pickAny(array.toList())

        /** 选取指定的文件列表[list] */
        @JvmStatic
        fun <V : View> pickFile(list: List<File>): FilePickerBuilder<V> {
            val pickerBean = PickerBean<V, File>(PICK_FILE)
            pickerBean.sourceList = list
            return FilePickerBuilder(pickerBean)
        }

        /** 选取指定的文件数组[array] */
        @JvmStatic
        fun <V : View> pickFile(array: Array<File>): FilePickerBuilder<V> = pickFile(array.toList())

        /** 选取指定的Uri列表[list] */
        @JvmStatic
        fun <V : View> pickUri(list: List<Uri>): UriPickerBuilder<V> {
            val pickerBean = PickerBean<V, Uri>(PICK_URI)
            pickerBean.sourceList = list
            return UriPickerBuilder(pickerBean)
        }

        /** 选取指定的Uri数组[array] */
        @JvmStatic
        fun <V : View> pickUri(array: Array<Uri>): UriPickerBuilder<V> = pickUri(array.toList())

        /** 选取指定的资源id列表[list] */
        @JvmStatic
        fun <V : View> pickResId(list: List<Int>): ResIdPickerBuilder<V> {
            val pickerBean = PickerBean<V, Int>(PICK_RES_ID)
            pickerBean.sourceList = list
            return ResIdPickerBuilder(pickerBean)
        }

        /** 选取指定的资源id数组[array] */
        @JvmStatic
        fun <V : View> pickResId(array: Array<Int>): ResIdPickerBuilder<V> = pickResId(array.toList())

        /** 选取指定的url列表[list] */
        @JvmStatic
        fun <V : View> pickImageUrl(list: List<String>): ImageUrlPickerBuilder<V> {
            val pickerBean = PickerBean<V, String>(PICK_IMAGE_URL)
            pickerBean.sourceList = list
            return ImageUrlPickerBuilder(pickerBean)
        }

        /** 选取指定的url数组[array] */
        @JvmStatic
        fun <V : View> pickImageUrl(array: Array<String>): ImageUrlPickerBuilder<V> = pickImageUrl(array.toList())
    }


    /** 手动关闭选择器 */
    fun finishPickActivity() {
        EventBus.getDefault().post(PicturePreviewFinishEvent())
        EventBus.getDefault().post(PhotoPickerFinishEvent())
    }
}