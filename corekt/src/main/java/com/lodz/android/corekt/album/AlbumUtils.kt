package com.lodz.android.corekt.album

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.media.MediaScannerConnection
import android.provider.MediaStore
import com.lodz.android.corekt.anko.getSize
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * 系统相册工具类
 * Created by zhouL on 2018/11/6.
 */
object AlbumUtils {

    /** 获取相册中所有图片路径列表 */
    @JvmStatic
    fun getAllImages(context: Context): List<String> {
        val imageList = LinkedList<String>()

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Images.Media.MIME_TYPE + "=? or " +
                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                MediaStore.Images.Media.MIME_TYPE + "=?"
        val selectionArgs = arrayOf("image/jpeg", "image/png", "image/gif", "image/jpg")
        val sortOrder = MediaStore.Images.Media.DATE_MODIFIED

        val cursor: Cursor? = context.contentResolver.query(uri, null, selection, selectionArgs, sortOrder)

        if (cursor == null) {
            return imageList
        }
        if (cursor.count == 0) {
            cursor.close()
            return imageList
        }

        cursor.moveToFirst()
        do {
            val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            if (path.isNullOrEmpty()) {
                continue
            }

            val file = File(path)
            if (file.exists() && file.length() > 0) {//文件存在且大小不为0
                imageList.add(path)
            }
        } while (cursor.moveToNext())
        cursor.close()
        imageList.reverse()// 按时间降序
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
    fun getImageFolders(pictures: List<String>): List<ImageFolder> {
        val list = LinkedList<ImageFolder>()
        if (pictures.isEmpty()) {
            return list
        }

        val directoryList = LinkedList<String>()
        for (path in pictures) {
            val file = File(path)
            if (!file.exists()) {
                continue
            }

            val parentFile = file.parentFile
            val parentPath = parentFile.absolutePath
            if (directoryList.contains(parentPath)) {// 已经添加
                continue
            }

            directoryList.add(parentPath)
            val imageFolder = getImageFolder(parentFile, path)//创建图片文件夹信息
            if (imageFolder != null) {
                list.add(imageFolder)
            }
        }
        return list
    }

    /** 获取总图片的文件夹信息 */
    @JvmStatic
    fun getTotalImageFolder(context: Context): ImageFolder {
        val list = getAllImages(context)

        val imageFolder = ImageFolder()
        imageFolder.name = "所有图片"
        imageFolder.count = list.getSize()
        imageFolder.coverImgPath = if (list.isEmpty()) "" else list[0]
        return imageFolder
    }

    /** 获取指定文件目录[file]下的图片文件夹信息，[coverImgPath]为封面图片路径 */
    @JvmStatic
    fun getImageFolder(file: File, coverImgPath: String): ImageFolder? {
        val fileList = file.list { dir, name ->
            name != null && (name.endsWith(".jpg")
                    || name.endsWith(".gif")
                    || name.endsWith(".png")
                    || name.endsWith(".jpeg"))
        }

        if (fileList == null || fileList.isEmpty()) {
            return null
        }
        var rootPath = file.absolutePath// 获取目录路径
        if (!rootPath.endsWith(File.separator)) {
            rootPath += File.separator
        }
        val imageList = ArrayList<String>()
        for (path in fileList) {
            val tempFile = File(rootPath + path)
            if (tempFile.exists() && tempFile.length() > 0) {//文件存在且大小不为0
                imageList.add(path)
            }
        }
        val imageFolder = ImageFolder()
        imageFolder.count = imageList.size
        imageFolder.coverImgPath = coverImgPath
        imageFolder.dir = file.absolutePath
        return imageFolder
    }

    /** 获取指定图片目录[imageFolder]下的图片数据列表 */
    @JvmStatic
    fun getImageListOfFolder(context: Context, imageFolder: ImageFolder): List<String> {
        val imageList = LinkedList<String>()

        if (imageFolder.isAllPicture()) {
            return getAllImages(context)
        }

        val directoryFile = File(imageFolder.dir)
        val files = directoryFile.listFiles { dir, name ->
            name != null && (name.endsWith(".jpg")
                    || name.endsWith(".gif")
                    || name.endsWith(".png")
                    || name.endsWith(".jpeg"))
        }
        if (files == null || files.isEmpty()) {
            return imageList
        }

        for (file in files) {
            if (file.exists() && file.length() > 0) {
                imageList.add(file.absolutePath)
            }
        }
        return imageList
    }

    /** 通知刷新相册，[imagePath]图片路径，[mimeTypes]图片格式默认jpeg/jpg/png/gif，[callback]回调默认为null */
    @JvmStatic
    @JvmOverloads
    fun notifyScanImage(context: Context, imagePath: String, mimeTypes: Array<String> = arrayOf("image/jpeg", "image/jpg", "image/png", "image/gif"),
                        callback: MediaScannerConnection.OnScanCompletedListener? = null) {
        MediaScannerConnection.scanFile(context.applicationContext, arrayOf(imagePath), mimeTypes, callback)
    }

    /** 删除图片，[path]图片路径 */
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