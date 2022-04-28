package com.lodz.android.corekt.file

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.util.Base64
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.text.DecimalFormat
import kotlin.math.ceil
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round

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

/** 把文件转Base64，转码类型[flags]默认Base64.NO_WRAP */
fun File.toBase64(flags: Int = Base64.NO_WRAP): String {
    if (!this.exists()) {
        return ""
    }
    FileInputStream(this).use {
        val bytes = ByteArray(it.available())
        val length = it.read(bytes)
        return Base64.encodeToString(bytes, 0, length, flags) ?: ""
    }
}

/** 文件转Uri，上下文[context]，FileProvider名称[authority] */
fun File.toUri(context: Context, authority: String) = FileProvider.getUriForFile(context, authority, this, this.name)

/** 将长度大小格式化 单位：B、KB、MB、GB、TB */
fun Long.formatFileSize(): String {
    if (this <= 0) {
        return "0KB"
    }
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups: Int = (log10(this.toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("##0.0").format(this / 1024.0.pow(digitGroups.toDouble())) + units[digitGroups]
}

/** 获取路径的文件名 */
fun String.getFilePathName(): String {
    val lastSeparatorIndex = this.lastIndexOf(File.separator)
    return if (lastSeparatorIndex != -1) this.substring(lastSeparatorIndex + 1) else this
}

/** 获取文件名的后缀 */
fun String.getFileSuffix(): String {
    val fileName = getFilePathName()
    val startCharIndex = fileName.lastIndexOf('.')
    if (startCharIndex != -1) {//存在后缀
        return fileName.substring(startCharIndex)
    }
    return ""
}

/** 获取文件夹路径 */
fun String.getDirPath(): String {
    if (this.last() == File.separatorChar){
        return this
    }
    if (this.contains(File.separator)) {
        val lastIndexOf = this.lastIndexOf(File.separator)
        return this.substring(0, lastIndexOf + 1)
    }
    return this
}

/** 获取最后一级文件夹名称 */
fun String.getDirName(): String {
    val dirPath = this.getDirPath()
    val path = if (dirPath.last() == File.separatorChar) dirPath.subSequence(0, dirPath.length - 1) else dirPath
    if (this.contains(File.separator)) {
        val index = path.lastIndexOf("/")
        return path.substring(index + 1)
    }
    return this
}

/** 获取音视频的总时长 */
fun Uri.getMediaDuration(context: Context): Int {
    val player = MediaPlayer()
    try {
        player.setDataSource(context, this)
        player.prepare()
        return player.duration
    } catch (e: Exception) {
        e.printStackTrace()
        player.release()
    }
    return 0
}

/** 获取音视频的总时长（毫秒） */
fun File.getMediaDuration(): Int {
    val player = MediaPlayer()
    try {
        player.setDataSource(this.absolutePath)
        player.prepare()
        return player.duration
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        player.release()
    }
    return 0
}

/** 格式化视频时长 */
fun Uri.formatVideoDuration(context: Context): String = getMediaDuration(context).toLong().formatVideoDuration()

/** 格式化视频时长 */
fun File.formatVideoDuration(): String = getMediaDuration().toLong().formatVideoDuration()

/** 格式化视频时长（毫秒） */
fun Long.formatVideoDuration(): String {
    if (this <= 0) {
        return "00:00"
    }
    if (this <= 1000){
        return "00:01"
    }
    val second = round(this / 1000.0).toLong()
    if (second < 60) {
        return String.format("00:%02d", second % 60)
    }
    if (second < 3600) {
        return String.format("%02d:%02d", second / 60, second % 60)
    }
    return String.format("%02d:%02d:%02d", second / 3600, second % 3600 / 60, second % 60)
}