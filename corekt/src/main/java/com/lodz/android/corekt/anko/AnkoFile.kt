package com.lodz.android.corekt.anko

import android.content.Context
import android.net.Uri
import android.util.Base64
import java.io.FileInputStream

/**
 * Uri工具类
 * @author zhouL
 * @date 2022/4/19
 */

/** 将uri路径转base64，上下文[context]，base64类型[flags]，uri打开模式[mode] */
fun Uri.toBase64(context: Context, flags: Int = Base64.NO_WRAP, mode: String = "r"): String =
    context.contentResolver.openFileDescriptor(this, mode)?.use { pfd ->
        FileInputStream(pfd.fileDescriptor).use { fs ->
            val bytes = ByteArray(fs.available())
            val length = fs.read(bytes)
            Base64.encodeToString(bytes, 0, length, flags)
        }
    } ?: ""