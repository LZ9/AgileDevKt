package com.lodz.android.corekt.file

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
    val duration: Long, //音视频时长
    val sizeFormat: String //格式化文件大小
)