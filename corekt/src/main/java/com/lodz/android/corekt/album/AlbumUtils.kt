package com.lodz.android.corekt.album

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.lodz.android.corekt.utils.FileUtils
import java.io.*
import java.util.*

/**
 * 系统相册工具类
 * Created by zhouL on 2018/11/6.
 */
object AlbumUtils {

    /** 获取相册中所有图片路径列表 */
    @JvmStatic
    fun getAllImages(context: Context): List<PicInfo> {
        val imageList = LinkedList<PicInfo>()

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE
        )
        val selection = MediaStore.MediaColumns.SIZE + "> 0 and" +
                "(" + MediaStore.Images.Media.MIME_TYPE + "=? or " +
                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                MediaStore.Images.Media.MIME_TYPE + "=?" + ")"
        val selectionArgs = arrayOf("image/jpeg", "image/png", "image/gif", "image/jpg")
        val sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " DESC"

        val cursor: Cursor? = context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)

        if (cursor == null) {
            cursor?.close()
            return imageList
        }
        if (cursor.count == 0) {
            cursor.close()
            return imageList
        }

        cursor.moveToFirst()
        do {
            val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
            val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            val fileUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(id.toString()).build()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (fileUri.toString().isEmpty()) {
                    continue
                }
            }else{
                if (path.isNullOrEmpty()) {
                    continue
                }
            }
            val file = File(path)
            if (file.exists() && file.length() > 0) {//文件存在且大小不为0
                imageList.add(PicInfo(path, fileUri))
            }
        } while (cursor.moveToNext())
        cursor.close()
        return imageList
    }

    /** 获取所有图片的文件夹（包括总图片的文件夹） */
    @JvmStatic
    fun getAllImageFolders(context: Context): List<ImageFolder> {
        val folders = LinkedList<ImageFolder>()
        val list = getAllImages(context)
        if (list.isEmpty()) {
            return folders
        }
        folders.add(getTotalImageFolder(context))
        folders.addAll(getImageFolders(list))
        return folders
    }

    /** 获取图片列表[pictures]的文件夹信息 */
    @JvmStatic
    fun getImageFolders(pictures: List<PicInfo>): List<ImageFolder> {
        val list = LinkedList<ImageFolder>()
        if (pictures.isEmpty()) {
            return list
        }

        for (info in pictures) {
            val file = File(info.path)
            if (!file.exists()) {
                continue
            }

            val folderDir = getDir(info.path)
            if (folderDir.isEmpty()){
                continue
            }
            var hasFolder = false
            for (folder in list) {
                if (folder.dir.equals(folderDir)) {
                    // 已经创建了目录
                    folder.addPicInfo(info)
                    hasFolder = true
                    break
                }
            }
            if (!hasFolder){
                val imageFolder = ImageFolder()
                imageFolder.dir = folderDir
                imageFolder.addPicInfo(info)
                list.add(imageFolder)
            }
        }
        return list
    }

    /** 获取图片父级路径 */
    private fun getDir(path: String): String {
        if (path.isNotEmpty()) {
            val index = path.lastIndexOf(File.separator)
            return path.substring(0, index)
        }
        return ""
    }

    /** 获取总图片的文件夹信息 */
    @JvmStatic
    fun getTotalImageFolder(context: Context): ImageFolder {
        val imageFolder = ImageFolder()
        imageFolder.name = "所有图片"
        imageFolder.addPicInfo(getAllImages(context))
        return imageFolder
    }

    /** 获取指定文件路径[path]所在的图片文件夹 */
    @JvmStatic
    fun getImageFolder(context: Context, path: String): ImageFolder? = getImageFolder(context, FileUtils.create(path))

    /** 获取指定图片信息[info]下的图片文件夹 */
    @JvmStatic
    fun getImageFolder(context: Context, info: PicInfo?): ImageFolder? = getImageFolder(context, info?.path ?: "")

    /** 获取指定文件[file]所在的图片文件夹 */
    @JvmStatic
    fun getImageFolder(context: Context, file: File?): ImageFolder? {
        val path = file?.absolutePath
        if (path.isNullOrEmpty()){
            return null
        }
        val dir = getDir(path)
        val allFoldersList = getAllImageFolders(context)
        for (folder in allFoldersList) {
            if (folder.dir.equals(dir)){
                return folder
            }
        }
        return null
    }

    @JvmStatic
    fun notifyScanImageCompat(context: Context, imagePath: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            notifyScanImageQ(context, imagePath)
        } else {
            notifyScanImage(context, imagePath)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @JvmStatic
    fun notifyScanImageQ(context: Context, imagePath: String){
        val file = File(imagePath)
        if (!file.exists()){
            return
        }
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
        values.put(MediaStore.Images.Media.DESCRIPTION, "This is an image")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        values.put(MediaStore.Images.Media.TITLE, file.name)
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            val fd = context.contentResolver.openFileDescriptor(file.toUri(), "r")
            fd?.use {
                val bitmap = BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
                context.contentResolver.openOutputStream(uri)?.use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }
            }
        }
    }

    /** 通知刷新相册，[imagePath]图片路径，[mimeTypes]图片格式默认jpeg/jpg/png/gif，[callback]回调默认为null */
    @JvmStatic
    @JvmOverloads
    fun notifyScanImage(context: Context, imagePath: String, mimeTypes: Array<String> = arrayOf("image/jpeg", "image/jpg", "image/png", "image/gif"),
                        callback: MediaScannerConnection.OnScanCompletedListener? = null) {
        MediaScannerConnection.scanFile(context.applicationContext, arrayOf(imagePath), mimeTypes, callback)
    }

    /** 删除图片，图片信息[info] */
    @JvmStatic
    fun deleteImageCompat(context: Context, info: PicInfo): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deleteImage(context, info.uri)
        } else {
            deleteImage(context, info.path)
        }

    /** 删除图片，图片路径[uri] */
    @JvmStatic
    fun deleteImage(context: Context, uri: Uri): Boolean = context.contentResolver.delete(uri, null, null) > 0
    
    /** 删除图片，图片路径[path] */
    @JvmStatic
    fun deleteImage(context: Context, path: String): Boolean {
        val cursor: Cursor? = MediaStore.Images.Media.query(context.contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media._ID), MediaStore.Images.Media.DATA + "=?", arrayOf(path), null)
        var isDelete = false
        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getLong(0)
            val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            val count = context.contentResolver.delete(uri, null, null)
            isDelete = count == 1
        } else {
            val file = File(path)
            if (file.exists()) {
                isDelete = file.delete()
            }
        }
        cursor?.close()
        return isDelete
    }
}