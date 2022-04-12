package com.lodz.android.corekt.media

import androidx.documentfile.provider.DocumentFile
import com.lodz.android.corekt.utils.FileUtils
import java.util.*

/**
 * 媒体信息数据（音频/视频/图片/文件等等）
 * @author zhouL
 * @date 2022/4/12
 */
class MediaInfoBean {

    /** 文件编号 */
    var documentId = ""

    /** 文件类型 */
    var mimeType = ""

    /** 文件名称 */
    var displayName = ""

    /** 概括 */
    var summary = ""

    /** 最后修改时间 */
    var lastModified: Long = 0L

    /** 标志位 */
    var flags = ""

    /** 文件大小 */
    var size: Long = 0L

    /** 文件对象 */
    var file: DocumentFile? = null

    /** 获取最后修改时间的Date对象 */
    fun getLastModifiedDate(): Date = Date(lastModified)

    /** 获取文件后缀 */
    fun getSuffix(): String = FileUtils.getSuffix(displayName)

}