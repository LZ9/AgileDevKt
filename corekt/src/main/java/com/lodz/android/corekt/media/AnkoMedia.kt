package com.lodz.android.corekt.media

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import java.io.File

/**
 * 媒体文件工具类
 * @author zhouL
 * @date 2022/4/12
 */

/**  */
fun Context.getMediaInfo(uri: Uri): MediaInfoBean {
    val bean = MediaInfoBean()
    bean.file = DocumentFile.fromSingleUri(this, uri)
    contentResolver.query(uri, null, null, null, null).use {
        it?.let {
            while (it.moveToNext()) {
                val documentIdIndex = it.getColumnIndex("document_id")
                if (documentIdIndex > -1) {
                    bean.documentId = it.getString(documentIdIndex) ?: ""
                }

                val mimeTypeIndex = it.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)
                if (mimeTypeIndex > -1) {
                    bean.mimeType = it.getString(mimeTypeIndex) ?: ""
                }

                val displayNameIndex = it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                if (displayNameIndex > -1) {
                    bean.displayName = it.getString(displayNameIndex) ?: ""
                }

                val summaryIndex = it.getColumnIndex("summary")
                if (summaryIndex > -1) {
                    bean.summary = it.getString(summaryIndex) ?: ""
                }

                val lastModifiedIndex = it.getColumnIndex("last_modified")
                if (lastModifiedIndex > -1) {
                    bean.lastModified = it.getLong(lastModifiedIndex)
                }

                val flagsIndex = it.getColumnIndex("flags")
                if (flagsIndex > -1) {
                    bean.flags = it.getString(flagsIndex) ?: ""
                }

                val sizeIndex = it.getColumnIndex(MediaStore.MediaColumns.SIZE)
                if (sizeIndex > -1) {
                    bean.size = it.getLong(sizeIndex)
                }
            }
        }
    }
    return bean
}

fun Context.getMediaInfo(file: File, authority: String): MediaInfoBean = getMediaInfo(FileProvider.getUriForFile(this, authority, file, file.name))


