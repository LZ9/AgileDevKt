package com.lodz.android.corekt.media

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.documentfile.provider.DocumentFile

/**
 * 媒体文件工具类
 * @author zhouL
 * @date 2022/4/12
 */

/**  */
@SuppressLint("Range")
fun Context.getMediaInfo(uri: Uri): MediaInfoBean? {
    val isDocumentUri = DocumentsContract.isDocumentUri(this, uri)
    if (!isDocumentUri) {
        return null
    }
    val bean = MediaInfoBean()
    bean.file = DocumentFile.fromSingleUri(this, uri)
    contentResolver.query(uri, null, null, null, null).use {
        it?.let {
            while (it.moveToNext()) {
                bean.documentId = it.getString(it.getColumnIndex("document_id")) ?: ""
                bean.mimeType = it.getString(it.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)) ?: ""
                bean.displayName = it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)) ?: ""
                bean.summary = it.getString(it.getColumnIndex("summary")) ?: ""
                bean.lastModified = it.getLong(it.getColumnIndex("last_modified"))
                bean.flags = it.getString(it.getColumnIndex("flags")) ?: ""
                bean.size = it.getLong(it.getColumnIndex(MediaStore.MediaColumns.SIZE))
            }
        }
    }
    return bean
}