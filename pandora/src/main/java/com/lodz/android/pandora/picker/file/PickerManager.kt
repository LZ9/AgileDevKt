package com.lodz.android.pandora.picker.file

import android.net.Uri
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
        fun pickPhoneAssemble(vararg types: Int = intArrayOf(PICK_PHONE_ALBUM, PICK_PHONE_VIDEO, PICK_PHONE_AUDIO)): DocumentPickerBuilder {
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
            val bean = PickerBean<DocumentWrapper>(PICK_PHONE_ASSEMBLE)
            bean.phoneAssemble = types
            return DocumentPickerBuilder(bean)
        }

        /** 选取手机相册图片 */
        @JvmStatic
        fun pickPhoneAlbum(): DocumentPickerBuilder = DocumentPickerBuilder(PickerBean(PICK_PHONE_ALBUM))

        /** 选取手机视频 */
        @JvmStatic
        fun pickPhoneVideo(): DocumentPickerBuilder = DocumentPickerBuilder(PickerBean(PICK_PHONE_VIDEO))

        /** 选取手机音频 */
        @JvmStatic
        fun pickPhoneAudio(): DocumentPickerBuilder = DocumentPickerBuilder(PickerBean(PICK_PHONE_AUDIO))

        /** 选取指定后缀[suffix]文件，例如.jpg */
        @JvmStatic
        fun pickPhoneBySuffix(vararg suffix: String): DocumentPickerBuilder {
            val pickerBean = PickerBean<DocumentWrapper>(PICK_PHONE_SUFFIX)
            pickerBean.suffixArray = suffix
            return DocumentPickerBuilder(pickerBean)
        }

        /** 选取指定的任意类型列表[list] */
        @JvmStatic
        fun <T> pickAny(list: List<T>): PickerBuilder<T> {
            val pickerBean = PickerBean<T>(PICK_ANY)
            pickerBean.sourceList = list
            return PickerBuilder(pickerBean)
        }

        /** 选取指定的任意类型数组[array] */
        @JvmStatic
        fun <T> pickAny(array: Array<T>): PickerBuilder<T> = pickAny(array.toList())

        /** 选取指定的文件列表[list] */
        @JvmStatic
        fun pickFile(list: List<File>): FilePickerBuilder {
            val pickerBean = PickerBean<File>(PICK_FILE)
            pickerBean.sourceList = list
            return FilePickerBuilder(pickerBean)
        }

        /** 选取指定的文件数组[array] */
        @JvmStatic
        fun pickFile(array: Array<File>): FilePickerBuilder = pickFile(array.toList())

        /** 选取指定的Uri列表[list] */
        @JvmStatic
        fun pickUri(list: List<Uri>): UriPickerBuilder {
            val pickerBean = PickerBean<Uri>(PICK_URI)
            pickerBean.sourceList = list
            return UriPickerBuilder(pickerBean)
        }

        /** 选取指定的Uri数组[array] */
        @JvmStatic
        fun pickUri(array: Array<Uri>): UriPickerBuilder = pickUri(array.toList())

        /** 选取指定的资源id列表[list] */
        @JvmStatic
        fun pickResId(list: List<Int>): ResIdPickerBuilder {
            val pickerBean = PickerBean<Int>(PICK_RES_ID)
            pickerBean.sourceList = list
            return ResIdPickerBuilder(pickerBean)
        }

        /** 选取指定的资源id数组[array] */
        @JvmStatic
        fun pickResId(array: Array<Int>): ResIdPickerBuilder = pickResId(array.toList())

        /** 选取指定的url列表[list] */
        @JvmStatic
        fun pickImageUrl(list: List<String>): ImageUrlPickerBuilder {
            val pickerBean = PickerBean<String>(PICK_IMAGE_URL)
            pickerBean.sourceList = list
            return ImageUrlPickerBuilder(pickerBean)
        }

        /** 选取指定的url数组[array] */
        @JvmStatic
        fun pickImageUrl(array: Array<String>): ImageUrlPickerBuilder = pickImageUrl(array.toList())
    }


    /** 手动关闭选择器 */
    fun finishPickActivity() {
        EventBus.getDefault().post(PicturePreviewFinishEvent())
        EventBus.getDefault().post(PhotoPickerFinishEvent())
    }
}