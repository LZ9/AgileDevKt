package com.lodz.android.pandora.utils.media

import com.lodz.android.corekt.media.AnkoMedia
import com.lodz.android.pandora.R

/**
 * 多媒体帮助类
 * @author zhouL
 * @date 2022/4/25
 */
object MediaUtils {

    /** 根据[mimeType]获取默认图标的资源id */
    fun getIconByMimeType(mimeType: String?): Int {
        if (AnkoMedia.isImage(mimeType)){
            return R.drawable.pandora_ic_img
        }
        if (AnkoMedia.isVideo(mimeType)){
            return R.drawable.pandora_ic_video
        }
        if (AnkoMedia.isAudio(mimeType)){
            return R.drawable.pandora_ic_audio
        }
        if (AnkoMedia.isText(mimeType)){
            return R.drawable.pandora_ic_text
        }
        if (AnkoMedia.isExcel(mimeType)){
            return R.drawable.pandora_ic_excel
        }
        if (AnkoMedia.isWord(mimeType)){
            return R.drawable.pandora_ic_word
        }
        if (AnkoMedia.isPPT(mimeType)){
            return R.drawable.pandora_ic_ppt
        }
        if (AnkoMedia.isPdf(mimeType)){
            return R.drawable.pandora_ic_pdf
        }
        if (AnkoMedia.isApk(mimeType)){
            return R.drawable.pandora_ic_apk
        }
        if (AnkoMedia.isZip(mimeType)) {
            return R.drawable.pandora_ic_zip
        }
        return R.drawable.pandora_ic_file
    }

    /** 根据文件名后缀[suffix]获取默认图标的资源id */
    fun getIconBySuffix(suffix: String): Int {
        if (AnkoMedia.isImageSuffix(suffix)){
            return R.drawable.pandora_ic_img
        }
        if (AnkoMedia.isVideoSuffix(suffix)) {
            return R.drawable.pandora_ic_video
        }
        if (AnkoMedia.isAudioSuffix(suffix)) {
            return R.drawable.pandora_ic_audio
        }
        if (AnkoMedia.isTextSuffix(suffix)) {
            return R.drawable.pandora_ic_text
        }
        if (AnkoMedia.isExcelSuffix(suffix)) {
            return R.drawable.pandora_ic_excel
        }
        if (AnkoMedia.isWordSuffix(suffix)) {
            return R.drawable.pandora_ic_word
        }
        if (AnkoMedia.isPPTSuffix(suffix)) {
            return R.drawable.pandora_ic_ppt
        }
        if (AnkoMedia.isPdfSuffix(suffix)) {
            return R.drawable.pandora_ic_pdf
        }
        if (AnkoMedia.isApkSuffix(suffix)) {
            return R.drawable.pandora_ic_apk
        }
        if (AnkoMedia.isZipSuffix(suffix)) {
            return R.drawable.pandora_ic_zip
        }
        return R.drawable.pandora_ic_file
    }
}