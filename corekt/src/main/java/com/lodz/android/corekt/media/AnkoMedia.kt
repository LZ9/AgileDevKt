package com.lodz.android.corekt.media

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.deduplication
import com.lodz.android.corekt.file.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * 媒体文件工具类
 * @author zhouL
 * @date 2022/4/12
 */

object AnkoMedia {
    /** 所有图片 */
    const val MIME_TYPE_IMAGE_START = "image/"
    /** gif图片 */
    const val MIME_TYPE_IMAGE_GIF = MIME_TYPE_IMAGE_START + "gif"
    /** jpeg/jpg图片 */
    const val MIME_TYPE_IMAGE_JPEG = MIME_TYPE_IMAGE_START + "jpeg"
    /** png图片 */
    const val MIME_TYPE_IMAGE_PNG = MIME_TYPE_IMAGE_START + "png"
    /** webp图片 */
    const val MIME_TYPE_IMAGE_WEBP = MIME_TYPE_IMAGE_START + "webp"
    /** bmp图片 */
    const val MIME_TYPE_IMAGE_BMP = MIME_TYPE_IMAGE_START + "bmp"

    /** 所有音频 */
    const val MIME_TYPE_AUDIO_START = "audio/"
    /** mpeg音频（包含mp3） */
    const val MIME_TYPE_AUDIO_MPEG = MIME_TYPE_AUDIO_START + "mpeg"
    /** wav音频 */
    const val MIME_TYPE_AUDIO_WAV = MIME_TYPE_AUDIO_START + "x-wav"

    /** 所有视频 */
    const val MIME_TYPE_VIDEO_START = "video/"
    /** avi视频 */
    const val MIME_TYPE_VIDEO_AVI = MIME_TYPE_VIDEO_START + "x-msvideo"
    /** flv视频 */
    const val MIME_TYPE_VIDEO_FLV = MIME_TYPE_VIDEO_START + "x-flv"
    /** mp4视频 */
    const val MIME_TYPE_VIDEO_MP4 = MIME_TYPE_VIDEO_START + "mp4"

    /** 所有应用文件 */
    const val MIME_TYPE_APPLICATION_START = "application/"
    /** xls文件 */
    const val MIME_TYPE_APPLICATION_XLS = MIME_TYPE_APPLICATION_START + "vnd.ms-excel"
    /** xlsx文件 */
    const val MIME_TYPE_APPLICATION_XLSX = MIME_TYPE_APPLICATION_START + "vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    /** doc文件 */
    const val MIME_TYPE_APPLICATION_DOC = MIME_TYPE_APPLICATION_START + "msword"
    /** docx文件 */
    const val MIME_TYPE_APPLICATION_DOCX = MIME_TYPE_APPLICATION_START + "vnd.openxmlformats-officedocument.wordprocessingml.document"
    /** ppt文件 */
    const val MIME_TYPE_APPLICATION_PPT = MIME_TYPE_APPLICATION_START + "vnd.ms-powerpoint"
    /** pptx文件 */
    const val MIME_TYPE_APPLICATION_PPTX = MIME_TYPE_APPLICATION_START + "vnd.openxmlformats-officedocument.presentationml.presentation"
    /** pdf文件 */
    const val MIME_TYPE_APPLICATION_PDF = MIME_TYPE_APPLICATION_START + "pdf"
    /** apk文件 */
    const val MIME_TYPE_APPLICATION_APK = MIME_TYPE_APPLICATION_START + "vnd.android.package-archive"
    /** zip文件 */
    const val MIME_TYPE_APPLICATION_ZIP = MIME_TYPE_APPLICATION_START + "zip"
    /** rar文件 */
    const val MIME_TYPE_APPLICATION_RAR = MIME_TYPE_APPLICATION_START + "x-rar-compressed"
    /** 7z文件 */
    const val MIME_TYPE_APPLICATION_7Z = MIME_TYPE_APPLICATION_START + "x-7z-compressed"

    /** 所有文本文件 */
    const val MIME_TYPE_TEXT_START = "text/"
    /** txt文件 */
    const val MIME_TYPE_TEXT_TXT = MIME_TYPE_TEXT_START + "plain"

    /** 自定义图片后缀 */
    var sCustomImageSuffixList = arrayListOf<String>()
    /** 自定义视频后缀 */
    var sCustomVideoSuffixList = arrayListOf<String>()
    /** 自定义音频后缀 */
    var sCustomAudioSuffixList = arrayListOf<String>()
    /** 自定义文本后缀 */
    var sCustomTextSuffixList = arrayListOf<String>()
    /** 自定义Excel后缀 */
    var sCustomExcelSuffixList = arrayListOf<String>()
    /** 自定义Word后缀 */
    var sCustomWordSuffixList = arrayListOf<String>()
    /** 自定义PPT后缀 */
    var sCustomPPTSuffixList = arrayListOf<String>()
    /** 自定义pdf后缀 */
    var sCustomPdfSuffixList = arrayListOf<String>()
    /** 自定义apk后缀 */
    var sCustomApkSuffixList = arrayListOf<String>()
    /** 自定义zip后缀 */
    var sCustomZipSuffixList = arrayListOf<String>()
}

/** 判断mimeType是不是图片 */
fun String?.isImageMimeType(): Boolean = (this != null && this.startsWith(AnkoMedia.MIME_TYPE_IMAGE_START))

/** 判断后缀是不是图片 */
fun String?.isImageSuffix(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    val suffixLower = this.lowercase()
    val list = assembleSuffix(
        AnkoMedia.sCustomImageSuffixList,
        arrayListOf(".gif", ".jpg", ".png", ".webp", ".bmp")
    )
    for (s in list) {
        if (suffixLower == s) {
            return true
        }
    }
    return false
}

/** 判断mimeType是不是视频 */
fun String?.isVideoMimeType(): Boolean = (this != null && this.startsWith(AnkoMedia.MIME_TYPE_VIDEO_START))

/** 判断后缀是不是视频 */
fun String?.isVideoSuffix(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    val suffixLower = this.lowercase()
    val list = assembleSuffix(
        AnkoMedia.sCustomVideoSuffixList,
        arrayListOf(".mp4", ".avi", ".flv", ".swf", ".rm", ".ram", ".rmvb", ".wmv", ".mpg", ".mpeg")
    )
    for (s in list) {
        if (suffixLower == s) {
            return true
        }
    }
    return false
}

/** 判断mimeType是不是音频 */
fun String?.isAudioMimeType(): Boolean = (this != null && this.startsWith(AnkoMedia.MIME_TYPE_AUDIO_START))

/** 判断后缀是不是音频 */
fun String?.isAudioSuffix(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    val suffixLower = this.lowercase()
    val list = assembleSuffix(
        AnkoMedia.sCustomAudioSuffixList,
        arrayListOf(".mp3", ".wav", ".flac", ".wma", ".pcm", ".aac", ".ra")
    )
    for (s in list) {
        if (suffixLower == s) {
            return true
        }
    }
    return false
}

/** 判断mimeType是不是文本文件 */
fun String?.isTextMimeType(): Boolean = (this != null && this.startsWith(AnkoMedia.MIME_TYPE_TEXT_START))

/** 判断后缀是不是文本文件 */
fun String?.isTextSuffix(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    val suffixLower = this.lowercase()
    val list = assembleSuffix(AnkoMedia.sCustomTextSuffixList, arrayListOf(".txt", ".log"))
    for (s in list) {
        if (suffixLower == s) {
            return true
        }
    }
    return false
}

/** 判断mimeType是不是Excel文件 */
fun String?.isExcelMimeType(): Boolean = (this != null && (this == AnkoMedia.MIME_TYPE_APPLICATION_XLS || this == AnkoMedia.MIME_TYPE_APPLICATION_XLSX))

/** 判断后缀是不是Excel文件 */
fun String?.isExcelSuffix(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    val suffixLower = this.lowercase()
    val list = assembleSuffix(AnkoMedia.sCustomExcelSuffixList, arrayListOf(".xls", ".xlsx"))
    for (s in list) {
        if (suffixLower == s) {
            return true
        }
    }
    return false
}

/** 判断mimeType是不是Word文件 */
fun String?.isWordMimeType(): Boolean = (this != null && (this == AnkoMedia.MIME_TYPE_APPLICATION_DOC || this == AnkoMedia.MIME_TYPE_APPLICATION_DOCX))

/** 判断后缀是不是Word文件 */
fun String?.isWordSuffix(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    val suffixLower = this.lowercase()
    val list = assembleSuffix(AnkoMedia.sCustomWordSuffixList, arrayListOf(".doc", ".docx"))
    for (s in list) {
        if (suffixLower == s) {
            return true
        }
    }
    return false
}

/** 判断mimeType是不是PPT文件 */
fun String?.isPptMimeType(): Boolean = (this != null && (this == AnkoMedia.MIME_TYPE_APPLICATION_PPT || this == AnkoMedia.MIME_TYPE_APPLICATION_PPTX))

/** 判断后缀是不是PPT文件 */
fun String?.isPptSuffix(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    val suffixLower = this.lowercase()
    val list = assembleSuffix(AnkoMedia.sCustomPPTSuffixList, arrayListOf(".ppt", ".pptx"))
    for (s in list) {
        if (suffixLower == s) {
            return true
        }
    }
    return false
}

/** 判断mimeType是不是pdf文件 */
fun String?.isPdfMimeType(): Boolean = (this != null && this == AnkoMedia.MIME_TYPE_APPLICATION_PDF)

/** 判断后缀是不是pdf文件 */
fun String?.isPdfSuffix(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    val suffixLower = this.lowercase()
    val list = assembleSuffix(AnkoMedia.sCustomPdfSuffixList, arrayListOf(".pdf"))
    for (s in list) {
        if (suffixLower == s) {
            return true
        }
    }
    return false
}

/** 判断mimeType是不是apk文件 */
fun String?.isApkMimeType(): Boolean = (this != null && this == AnkoMedia.MIME_TYPE_APPLICATION_APK)

/** 判断后缀是不是apk文件 */
fun String?.isApkSuffix(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    val suffixLower = this.lowercase()
    val list = assembleSuffix(AnkoMedia.sCustomApkSuffixList, arrayListOf(".apk"))
    for (s in list) {
        if (suffixLower == s) {
            return true
        }
    }
    return false
}

/** 判断mimeType是不是zip文件 */
fun String?.isZipMimeType(): Boolean = (this != null &&
        (this == AnkoMedia.MIME_TYPE_APPLICATION_ZIP || this == AnkoMedia.MIME_TYPE_APPLICATION_RAR || this == AnkoMedia.MIME_TYPE_APPLICATION_7Z))

/** 判断后缀是不是zip文件 */
fun String?.isZipSuffix(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    val suffixLower = this.lowercase()
    val list = assembleSuffix(AnkoMedia.sCustomZipSuffixList, arrayListOf(".zip", ".rar", ".7z"))
    for (s in list) {
        if (suffixLower == s) {
            return true
        }
    }
    return false
}

/** 组装自定义后缀和默认后缀 */
private fun assembleSuffix(customSuffixList: ArrayList<String>, defaultSuffixList: ArrayList<String>): List<String> {
    if (customSuffixList.isEmpty()) {
        return defaultSuffixList
    }
    for (suffix in customSuffixList) {
        defaultSuffixList.add(suffix.lowercase())
    }
    return defaultSuffixList.deduplication().toList()
}

/**
 * 获取所有文件，传入文件后缀[suffix]，例如：.jpg、.doc等等。
 * 考虑到
 */
fun Context.getAllFiles(vararg suffix: String): List<DocumentWrapper> {
    val uri = MediaStore.Files.getContentUri("external")
    val list = ArrayList<DocumentWrapper>()
    val array: Array<out String> = suffix
    val map = HashMap<String, ArrayList<DocumentWrapper>>()
    for (s in array) {
        map[s] = ArrayList()
    }

    contentResolver.query(
        uri,
        null,
        "${MediaStore.Files.FileColumns.MEDIA_TYPE} != 'null' AND ${MediaStore.MediaColumns.SIZE} > 0",
        null,
        MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC"
    )?.use {
        while (it.moveToNext()){
            var duration = 0L
            val idIndex = it.getColumnIndex(MediaStore.Files.FileColumns._ID)
            val id = it.getLong(idIndex)
            val dataIndex = it.getColumnIndex(MediaStore.Files.FileColumns.DATA)
            val path = it.getString(dataIndex)
            val nameIndex = it.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val name = it.getString(nameIndex)
            val mimeTypeIndex = it.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)
            val mimeType = it.getString(mimeTypeIndex)
            val durationIndex = it.getColumnIndex("duration")
            if (durationIndex >= 0) {
                duration = it.getLong(durationIndex)
            }

            val file = File(path)
            if (!file.isFile) {
                continue
            }
            val document = DocumentFile.fromSingleUri(this, ContentUris.withAppendedId(uri, id)) ?: continue
            val durationFormat = if (duration > 0) duration.formatVideoDuration() else ""
            val wrapper = DocumentWrapper(file, document, name, duration, durationFormat, file.length().formatFileSize())
            if (array.isEmpty()) {//后缀数组为空查询全部
                list.add(wrapper)
                continue
            }
            map[path.getFileSuffix()]?.add(wrapper)
        }
        for (entry in map) {
            list.addAll(entry.value)
        }
    }
    return list
}

/** 获取媒体库里所有图片 */
fun Context.getMediaImages(): List<DocumentWrapper> = getMediaData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

/** 获取媒体库里所有音频 */
fun Context.getMediaAudios(): List<DocumentWrapper> = getMediaData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)

/** 获取媒体库里所有视频 */
fun Context.getMediaVideos(): List<DocumentWrapper> = getMediaData(MediaStore.Video.Media.EXTERNAL_CONTENT_URI)

/** 获取媒体库数据 */
fun Context.getMediaData(uri: Uri): List<DocumentWrapper> {
    val list = ArrayList<DocumentWrapper>()
    contentResolver.query(
        uri,
        null,
        "${MediaStore.MediaColumns.SIZE} > 0",
        null,
        MediaStore.MediaColumns.DATE_MODIFIED + " DESC"
    )?.use {
        while (it.moveToNext()) {
            var duration = 0L
            val idIndex = it.getColumnIndex(MediaStore.MediaColumns._ID)
            val id = it.getLong(idIndex)
            val dataIndex = it.getColumnIndex(MediaStore.MediaColumns.DATA)
            val path = it.getString(dataIndex)
            val nameIndex = it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
            val name = it.getString(nameIndex)
            val mimeTypeIndex = it.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)
            val mimeType = it.getString(mimeTypeIndex)
            val durationIndex = it.getColumnIndex("duration")
            if (durationIndex >= 0) {
                duration = it.getLong(durationIndex)
            }

            val file = File(path)
            if (!file.isFile) {
                continue
            }
            val document = DocumentFile.fromSingleUri(this, ContentUris.withAppendedId(uri, id)) ?: continue
            val durationFormat = if (duration > 0) duration.formatVideoDuration() else ""
            val wrapper = DocumentWrapper(file, document, name, duration, durationFormat, file.length().formatFileSize())
            list.add(wrapper)
        }
    }
    return list
}

/** 根据[uri]删除文件 */
fun Context.deleteFile(uri: Uri) = contentResolver.delete(uri, null, null)

/** 往图片数据库插入数据来获取路径 */
fun Context.insertImageForPair(
    fileName: String,
    authority: String,
    directoryName: String = Environment.DIRECTORY_PICTURES,
    mimeType: String = AnkoMedia.MIME_TYPE_IMAGE_JPEG
): Pair<Uri, File>? = insertMediaForPair(
    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    mimeType,
    fileName,
    authority,
    directoryName
)

/** 往视频数据库插入数据来获取路径 */
fun Context.insertVideoForPair(
    fileName: String,
    authority: String,
    directoryName: String = Environment.DIRECTORY_DCIM,
    mimeType: String = AnkoMedia.MIME_TYPE_VIDEO_MP4
): Pair<Uri, File>? = insertMediaForPair(
    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
    mimeType,
    fileName,
    authority,
    directoryName
)

/** 往音频数据库插入数据来获取路径 */
fun Context.insertAudioForPair(
    fileName: String,
    authority: String,
    directoryName: String = Environment.DIRECTORY_MUSIC,
    mimeType: String = AnkoMedia.MIME_TYPE_AUDIO_WAV
): Pair<Uri, File>? = insertMediaForPair(
    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
    mimeType,
    fileName,
    authority,
    directoryName
)

/** 往多媒体数据库插入数据来获取路径 */
fun Context.insertMediaForPair(
    uri: Uri,
    mimeType: String,
    fileName: String,
    authority: String,
    directoryName: String
): Pair<Uri, File>? {
    var rootPath = Environment.getExternalStoragePublicDirectory(directoryName)?.absolutePath ?: ""
    if (rootPath.isEmpty()) {
        return null
    }
    if (!rootPath.endsWith(File.separator)) {//补全地址
        rootPath += File.separator
    }
    val file = File(rootPath.append(fileName))
    val resultUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        values.put(MediaStore.Images.Media.DESCRIPTION, "This is an media")
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        values.put(MediaStore.Images.Media.TITLE, fileName)
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, directoryName)
        contentResolver.insert(uri, values)
    } else {
        FileProvider.getUriForFile(this, authority, file)
    }
    if (resultUri == null) {
        return null
    }
    return Pair(resultUri, file)
}

/** 将图片路径插入数据库 */
fun Context.insertImageByPath(
    fileName: String,
    directoryName: String = Environment.DIRECTORY_PICTURES,
    mimeType: String = AnkoMedia.MIME_TYPE_IMAGE_JPEG
) = insertMediaByPath(
    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    mimeType,
    fileName,
    directoryName
)

/** 将视频路径插入数据库 */
fun Context.insertVideoByPath(
    fileName: String,
    directoryName: String = Environment.DIRECTORY_DCIM,
    mimeType: String = AnkoMedia.MIME_TYPE_VIDEO_MP4
) = insertMediaByPath(
    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
    mimeType,
    fileName,
    directoryName
)

/** 将音频路径插入数据库 */
fun Context.insertAudioByPath(
    fileName: String,
    directoryName: String = Environment.DIRECTORY_MUSIC,
    mimeType: String = AnkoMedia.MIME_TYPE_AUDIO_WAV
) = insertMediaByPath(
    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
    mimeType,
    fileName,
    directoryName
)

/** 将多媒体路径插入数据库 */
fun Context.insertMediaByPath(
    uri: Uri,
    mimeType: String,
    fileName: String,
    directoryName: String
) {
    var rootPath = Environment.getExternalStoragePublicDirectory(directoryName)?.absolutePath ?: ""
    if (rootPath.isEmpty()) {
        return
    }
    if (!rootPath.endsWith(File.separator)) {//补全地址
        rootPath += File.separator
    }
    val file = File(rootPath.append(fileName))
    val values = ContentValues()
    values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
    values.put(MediaStore.Images.Media.DESCRIPTION, "This is an media")
    values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
    values.put(MediaStore.Images.Media.TITLE, file.name)
    values.put(MediaStore.Images.Media.DATA, file.absolutePath)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, directoryName)
    }
    contentResolver.insert(uri, values)
}

/** 通知刷新图片数据库 */
fun File.notifyScanImage(
    context: Context,
    mimeTypes: Array<String> = arrayOf(AnkoMedia.MIME_TYPE_IMAGE_JPEG),
    callback: MediaScannerConnection.OnScanCompletedListener? = null
) = notifyScanMedia(context, mimeTypes, callback)

/** 通知刷新视频数据库 */
fun File.notifyScanVideo(
    context: Context,
    mimeTypes: Array<String> = arrayOf(AnkoMedia.MIME_TYPE_VIDEO_MP4),
    callback: MediaScannerConnection.OnScanCompletedListener? = null
) = notifyScanMedia(context, mimeTypes, callback)

/** 通知刷新音频数据库 */
fun File.notifyScanAudio(
    context: Context,
    mimeTypes: Array<String> = arrayOf(AnkoMedia.MIME_TYPE_AUDIO_WAV),
    callback: MediaScannerConnection.OnScanCompletedListener? = null
) = notifyScanMedia(context, mimeTypes, callback)

/** 通知刷新多媒体数据库，[mimeTypes]数据类型，[callback]回调默认为null */
fun File.notifyScanMedia(
    context: Context,
    mimeTypes: Array<String>,
    callback: MediaScannerConnection.OnScanCompletedListener? = null
) = MediaScannerConnection.scanFile(context, arrayOf(absolutePath), mimeTypes, callback)

/** 获取手机所有文件的目录 */
fun Context.getAllFileFolder(vararg suffix: String): List<DocumentFolder> {
    val folders = LinkedList<DocumentFolder>()
    val totalFolder = getTotalFolder(*suffix)
    if (totalFolder.fileList.isEmpty()){
        return folders
    }
    folders.add(totalFolder)
    folders.addAll(totalFolder.fileList.groupByFolder())
    return folders
}

/** 获取总文件夹 */
fun Context.getTotalFolder(vararg suffix: String): DocumentFolder {
    val folder = DocumentFolder()
    folder.dirName = "全部"
    folder.addDocument(getAllFiles(*suffix))
    return folder
}

/** 获取手机所有文件的目录 */
fun List<DocumentWrapper>.getAllFileFolder(): List<DocumentFolder> {
    val folders = LinkedList<DocumentFolder>()
    val totalFolder = getTotalFolder()
    if (totalFolder.fileList.isEmpty()) {
        return folders
    }
    folders.add(totalFolder)
    folders.addAll(totalFolder.fileList.groupByFolder())
    return folders
}

/** 获取总文件夹 */
fun List<DocumentWrapper>.getTotalFolder(): DocumentFolder {
    val folder = DocumentFolder()
    folder.dirName = "全部"
    folder.addDocument(this)
    return folder
}

/** 将文件分组到对应的文件夹目录里 */
fun List<DocumentWrapper>.groupByFolder(): List<DocumentFolder> {
    val list = LinkedList<DocumentFolder>()
    if (this.isEmpty()) {
        return list
    }
    for (wrapper in this) {
        if (!wrapper.file.exists()) {
            continue
        }

        val dirPath = wrapper.file.absolutePath.getDirPath()
        if (dirPath.isEmpty()){
            continue
        }
        var hasFolder = false
        for (folder in list) {
            if (folder.dirPath == dirPath) {
                // 已经创建了目录
                folder.addDocument(wrapper)
                if (folder.coverDocument == null) {
                    folder.coverDocument = getCoverDocument(wrapper)
                }
                hasFolder = true
                break
            }
        }
        if (!hasFolder) {
            val folder = DocumentFolder()
            folder.dirPath = dirPath
            folder.dirName = wrapper.file.absolutePath.getDirName()
            folder.addDocument(wrapper)
            folder.coverDocument = getCoverDocument(wrapper)
            list.add(folder)
        }
    }
    return list
}

/** 获取封面文件 */
private fun getCoverDocument(wrapper: DocumentWrapper): DocumentWrapper? {
    var coverDocument: DocumentWrapper? = null
    val mimeType = wrapper.documentFile.type
    if (mimeType.isImageMimeType() || mimeType.isVideoMimeType()) {
        coverDocument = wrapper
    }
    if (coverDocument == null) {
        val suffix = wrapper.file.absolutePath.getFileSuffix()
        if (suffix.isImageSuffix() || suffix.isVideoSuffix()) {
            coverDocument = wrapper
        }
    }
    return coverDocument
}



