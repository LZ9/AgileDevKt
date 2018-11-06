package com.lodz.android.corekt.album

import android.content.Context
import android.database.Cursor
import android.media.MediaScannerConnection
import android.provider.MediaStore
import com.lodz.android.corekt.anko.getSize
import java.io.File
import java.io.FilenameFilter
import java.util.*

/**
 * 系统相册工具类
 * Created by zhouL on 2018/11/6.
 */
object AlbumUtils {

    /** 获取相册中所有图片列表 */
    fun getAllImages(context: Context): List<String> {
        val imageList = LinkedList<String>()

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Images.Media.MIME_TYPE + "=? or " +
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
        Collections.reverse(imageList)// 按时间降序
        return imageList
    }

    /** 获取所有图片的文件夹（包括总图片的文件夹） */
    fun getAllImageFolders(context: Context): List<ImageFolder> {
        val folders = LinkedList<ImageFolder>()
        val list = getAllImages(context)
        if (list.isEmpty()) {
            return folders
        }
        folders.add(getAllImageFolder(context))
        folders.addAll(getImageFolders(list))
        return folders
    }

    /** 获取图片列表[pictures]的文件夹信息 */
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
    fun getAllImageFolder(context: Context): ImageFolder {
        val list = getAllImages(context)

        val imageFolder = ImageFolder()
        imageFolder.name = "所有图片"
        imageFolder.isAllPicture = true
        imageFolder.count = list.getSize()
        imageFolder.firstImagePath = if (list.isEmpty()) "" else list.get(0)
        return imageFolder
    }

    /** 获取指定文件目录[file]下的图片文件夹信息，[coverImgPath]为封面图片路径 */
    fun getImageFolder(file: File, coverImgPath: String): ImageFolder? {
        val fileList = file.list(object : FilenameFilter {
            override fun accept(dir: File?, name: String?): Boolean =
                    name != null && (name.endsWith(".jpg")
                            || name.endsWith(".gif")
                            || name.endsWith(".png")
                            || name.endsWith(".jpeg"))

        })

        if (fileList == null || fileList.size == 0) {
            return null
        }
        val imageFolder = ImageFolder()
        imageFolder.count = fileList.size
        imageFolder.firstImagePath = coverImgPath
        imageFolder.dir = file.absolutePath
        return imageFolder
    }

    /** 获取指定图片目录[imageFolder]下的图片数据列表 */
    fun getImageListOfFolder(context: Context, imageFolder: ImageFolder):List<String>{
        val imageList = LinkedList<String>()

        if (imageFolder.isAllPicture){
            return getAllImages(context)
        }

        if (!imageFolder.isDirectory()){
            return imageList
        }

        val directoryFile = File(imageFolder.dir)
        val files = directoryFile.listFiles(object : FilenameFilter {
            override fun accept(dir: File?, name: String?): Boolean =
                    name != null && (name.endsWith(".jpg")
                            || name.endsWith(".gif")
                            || name.endsWith(".png")
                            || name.endsWith(".jpeg"))
        })
        if (files == null || files.size == 0) {
            return imageList
        }

        for (file in files) {
            if (file.exists() && file.length() > 0){
                imageList.add(file.absolutePath)
            }
        }
        return imageList
    }

    /** 通知刷新相册，[imagePath]图片路径，[callback]回调默认为null */
    fun notifyScanImage(context: Context, imagePath: String, callback: MediaScannerConnection.OnScanCompletedListener? = null) {
        MediaScannerConnection.scanFile(context, arrayOf(imagePath), arrayOf("image/jpeg"), callback)
    }

}