package com.lodz.android.corekt.file

import androidx.annotation.DrawableRes
import androidx.documentfile.provider.DocumentFile
import java.io.File

/**
 * 文档包装类
 * @author zhouL
 * @date 2022/4/24
 */
class DocumentWrapper(
    val file: File,
    val documentFile: DocumentFile,
    val fileName: String, //文件名
    val duration: Long, //音视频时长
    val durationFormat: String, //格式化音视频时长
    val sizeFormat: String, //格式化文件大小
    @DrawableRes var iconId: Int = 0, //音视频时长
):PickerInfo {

    override fun getNameStr(): String = fileName

    override fun getDurationFormatStr(): String = durationFormat

    override fun getSizeFormatStr(): String = sizeFormat
}