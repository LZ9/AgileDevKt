package com.lodz.android.pandora.utils.media

import com.lodz.android.corekt.media.*
import com.lodz.android.pandora.R

/**
 * 多媒体帮助类
 * @author zhouL
 * @date 2022/4/25
 */
object MediaUtils {

    /** 根据[mimeType]获取默认图标的资源id */
    fun getIconByMimeType(mimeType: String?): Int {
        if (mimeType.isImageMimeType()){
            return R.drawable.pandora_ic_img
        }
        if (mimeType.isVideoMimeType()){
            return R.drawable.pandora_ic_video
        }
        if (mimeType.isAudioMimeType()){
            return R.drawable.pandora_ic_audio
        }
        if (mimeType.isTextMimeType()){
            return R.drawable.pandora_ic_text
        }
        if (mimeType.isExcelMimeType()){
            return R.drawable.pandora_ic_excel
        }
        if (mimeType.isWordMimeType()){
            return R.drawable.pandora_ic_word
        }
        if (mimeType.isPptMimeType()){
            return R.drawable.pandora_ic_ppt
        }
        if (mimeType.isPdfMimeType()){
            return R.drawable.pandora_ic_pdf
        }
        if (mimeType.isApkMimeType()){
            return R.drawable.pandora_ic_apk
        }
        if (mimeType.isZipMimeType()) {
            return R.drawable.pandora_ic_zip
        }
        return R.drawable.pandora_ic_file
    }

    /** 根据文件名后缀[suffix]获取默认图标的资源id */
    fun getIconBySuffix(suffix: String): Int {
        if (suffix.isImageSuffix()){
            return R.drawable.pandora_ic_img
        }
        if (suffix.isVideoSuffix()) {
            return R.drawable.pandora_ic_video
        }
        if (suffix.isAudioSuffix()) {
            return R.drawable.pandora_ic_audio
        }
        if (suffix.isTextSuffix()) {
            return R.drawable.pandora_ic_text
        }
        if (suffix.isExcelSuffix()) {
            return R.drawable.pandora_ic_excel
        }
        if (suffix.isWordSuffix()) {
            return R.drawable.pandora_ic_word
        }
        if (suffix.isPptSuffix()) {
            return R.drawable.pandora_ic_ppt
        }
        if (suffix.isPdfSuffix()) {
            return R.drawable.pandora_ic_pdf
        }
        if (suffix.isApkSuffix()) {
            return R.drawable.pandora_ic_apk
        }
        if (suffix.isZipSuffix()) {
            return R.drawable.pandora_ic_zip
        }
        return R.drawable.pandora_ic_file
    }
}