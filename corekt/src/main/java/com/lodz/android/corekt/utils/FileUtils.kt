package com.lodz.android.corekt.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.text.DecimalFormat

/**
 * 文件操作帮助类
 * Created by zhouL on 2018/6/26.
 */
object FileUtils {

    /** 设置当前路径[filePath]拥有最高权限 */
    fun setFileRoot(filePath: String): Boolean {
        try {
            val permission = "777"
            val command = "chmod " + permission + " " + filePath
            val runtime = Runtime.getRuntime()
            runtime.exec(command)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /** 根据路径[filePath]创建文件对象 */
    fun create(filePath: String): File? {
        try {
            return File(filePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 路径[filePath]下的文件是否存在 */
    fun isFileExists(filePath: String): Boolean = isFileExists(create(filePath))

    /** 判断文件[file]是否存在 */
    fun isFileExists(file: File?): Boolean = file != null && file.exists()

    /** 根据路径[filePath]对文件重命名[newName] */
    fun renameFile(filePath: String, newName: String) = renameFile(create(filePath), newName)

    /** 对文件[file]重命名[newName] */
    fun renameFile(file: File?, newName: String): Boolean {
        val replaceName = newName.replace(" ", "", true)//去掉新名称中的空格
        if (file == null || !isFileExists(file)) {//文件为空或者文件不存在
            return false
        }
        if (newName.equals(file.name)) {// 新名称与旧名称一致
            return true
        }

        val newFile = create(file.getParent() + File.separator + replaceName)
        return file.renameTo(newFile)
    }

    /** 删除指定路径[filePath]下以后缀[suffix]结尾的文件，例如：.apk、.png等等 */
    fun deleteFileWithSuffix(filePath: String, suffix: String) {
        val file = create(filePath)
        if (file == null || !isFileExists(file)) {// 文件为空或不存在
            return
        }
        val files: Array<File>? = file.listFiles()
        if (files == null || files.size == 0) {// 该路径下没有其他文件
            return
        }

        var replaceSuffix = suffix
        if (!suffix.startsWith(".")) {//校验后缀符号
            replaceSuffix = "." + suffix
        }

        for (childFile in files) {
            if (childFile.name.toLowerCase().endsWith(replaceSuffix.toLowerCase())) {
                childFile.delete()
            }
        }
    }

    /** 获取文件名[fileName]的后缀 */
    fun getSuffix(fileName: String): String {
        var subffix = ""
        val startCharindex = fileName.lastIndexOf('.')
        if (startCharindex != -1) {//存在后缀
            subffix = fileName.substring(startCharindex)
        }
        return subffix
    }

    /** 在路径[filePath]下创建文件夹 */
    fun createFolder(filePath: String): Boolean {
        val folder: File? = create(filePath)
        return folder != null && !folder.exists() && folder.mkdirs()
    }

    /** 在路径[filePath]下创建一个文件 */
    fun createNewFile(filePath: String): Boolean {
        try {
            val file: File? = create(filePath)
            return file != null && !file.exists() && file.createNewFile()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /** 移动指定文件[fromPath]到指定的路径[toPath] */
    fun moveFile(fromPath: String, toPath: String): Boolean {
        val fromFile: File? = create(fromPath)
        if (fromFile == null || !isFileExists(fromFile)) {
            return false
        }
        val toFile: File? = create(fromPath)
        return toFile != null && !toFile.exists() && fromFile.renameTo(toFile)
    }

    /** 复制文件[fromPath]到指定路径[toPath] */
    fun copyFile(fromPath: String, toPath: String): Boolean {
        val fromFile: File? = create(fromPath)
        val toFile: File? = create(toPath)
        if (fromFile == null || toFile == null) {
            return false;
        }

        var inStream: FileInputStream? = null
        var outStream: FileOutputStream? = null
        var inChannel: FileChannel? = null
        var outChannel: FileChannel? = null

        try {
            inStream = FileInputStream(fromFile)
            outStream = FileOutputStream(toFile)
            inChannel = inStream.channel
            outChannel = outStream.channel
            inChannel.transferTo(0, inChannel.size(), outChannel)

            inStream.close()
            outStream.close()
            inChannel?.close()
            outChannel?.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            inStream?.close()
            outStream?.close()
            inChannel?.close()
            outChannel?.close()
        }
        return false
    }

    /** 获取路径[filePath]下的文件总长度（返回结果携带单位） */
    fun getFileTotalLengthUnit(filePath: String): String = formetFileSize(getFileTotalLength(filePath))

    /** 获取路径[filePath]下的文件总长度 */
    fun getFileTotalLength(filePath: String): Long {
        val file: File? = create(filePath)

        if (file == null || !isFileExists(file)) {
            return 0
        }

        try {
            if (file.isDirectory) {
                return getFileSizes(file)
            } else {
                return getFileSize(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    /** 获取文件[File]大小 */
    private fun getFileSize(file: File): Long {
        if (isFileExists(file)) {
            return file.length()
        }
        return 0
    }

    /** 计算文件夹[file]大小 */
    private fun getFileSizes(file: File): Long {
        val files: Array<File>? = file.listFiles()
        if (files == null || files.size == 0) {
            return 0
        }
        var size: Long = 0
        for (f in files) {
            if (f.isDirectory) {
                size = size + getFileSizes(f)
            } else {
                size = size + getFileSize(f)
            }
        }
        return size
    }

    /** 将文件大小[fileSize]格式化单位：B、KB、MB、GB、TB */
    fun formetFileSize(fileSize: Long): String {
        if (fileSize <= 0) {
            return "0KB"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups: Int = (Math.log10(fileSize.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("##0.0").format(fileSize / Math.pow(1024.0, digitGroups.toDouble())) + units[digitGroups]
    }

    /** 删除路径[filePath]的文件 */
    fun delFile(filePath: String) {
        val file: File? = create(filePath)
        if (file == null || !isFileExists(file)) {
            return
        }

        try {
            if (file.isDirectory){
                val files: Array<File>? = file.listFiles()
                if (files == null || files.size == 0){
                    file.delete()
                    return
                }

                for (f in files){
                    if (f.isFile()) {
                        f.delete()
                    } else if (f.isDirectory) {
                        delFile(f.absolutePath)
                    }
                }
            }else if (file.isFile){
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // todo 待完善


























}