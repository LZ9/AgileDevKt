package com.lodz.android.corekt.file

import androidx.documentfile.provider.DocumentFile
import java.io.File

/**
 * 文档包装类
 * @author zhouL
 * @date 2022/4/24
 */
class DocumentWrapper(val file: File, val documentFile: DocumentFile, val duration: Long)