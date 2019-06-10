package com.lodz.android.corekt.utils

import android.graphics.Bitmap
import androidx.annotation.IntRange
import java.io.*
import java.nio.channels.FileChannel
import java.text.DecimalFormat

/**
 * 文件操作帮助类
 * Created by zhouL on 2018/6/26.
 */
object FileUtils {

    /** 设置当前路径[filePath]拥有最高权限 */
    @JvmStatic
    fun setFileRoot(filePath: String): Boolean {
        try {
            val permission = "777"
            val command = "chmod $permission $filePath"
            val runtime = Runtime.getRuntime()
            runtime.exec(command)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /** 根据路径[filePath]创建文件对象 */
    @JvmStatic
    fun create(filePath: String): File? {
        try {
            return File(filePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 路径[filePath]下的文件是否存在 */
    @JvmStatic
    fun isFileExists(filePath: String): Boolean = isFileExists(create(filePath))

    /** 判断文件[file]是否存在 */
    @JvmStatic
    fun isFileExists(file: File?): Boolean = file != null && file.exists()

    /** 根据路径[filePath]对文件重命名[newName] */
    @JvmStatic
    fun renameFile(filePath: String, newName: String): Boolean = renameFile(create(filePath), newName)

    /** 对文件[file]重命名[newName] */
    @JvmStatic
    fun renameFile(file: File?, newName: String): Boolean {
        val replaceName = newName.replace(" ", "", true)//去掉新名称中的空格
        if (file == null || !isFileExists(file)) {//文件为空或者文件不存在
            return false
        }
        if (newName.equals(file.name)) {// 新名称与旧名称一致
            return true
        }

        val newFile = create(file.parent + File.separator + replaceName)
        return file.renameTo(newFile)
    }

    /** 删除指定路径[filePath]下以后缀[suffix]结尾的文件，例如：.apk、.png等等 */
    @JvmStatic
    fun deleteFileWithSuffix(filePath: String, suffix: String) {
        val file = create(filePath)
        if (file == null || !isFileExists(file)) {// 文件为空或不存在
            return
        }
        val files: Array<File>? = file.listFiles()
        if (files == null || files.isEmpty()) {// 该路径下没有其他文件
            return
        }

        var replaceSuffix = suffix
        if (!suffix.startsWith(".")) {//校验后缀符号
            replaceSuffix = ".$suffix"
        }

        for (childFile in files) {
            if (childFile.name.toLowerCase().endsWith(replaceSuffix.toLowerCase())) {
                childFile.delete()
            }
        }
    }

    /** 获取文件名[fileName]的后缀 */
    @JvmStatic
    fun getSuffix(fileName: String): String {
        var subffix = ""
        val startCharindex = fileName.lastIndexOf('.')
        if (startCharindex != -1) {//存在后缀
            subffix = fileName.substring(startCharindex)
        }
        return subffix
    }

    /** 在路径[filePath]下创建文件夹 */
    @JvmStatic
    fun createFolder(filePath: String): Boolean {
        val folder: File? = create(filePath)
        return folder != null && !folder.exists() && folder.mkdirs()
    }

    /** 在路径[filePath]下创建一个文件 */
    @JvmStatic
    fun createNewFile(filePath: String): Boolean {
        try {
            val file: File? = create(filePath)
            return file != null && !file.exists() && file.createNewFile()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /** 移动文件[fileName]从[fromPath]到[toPath] */
    @JvmStatic
    fun moveFile(fromPath: String, toPath: String, fileName: String): Boolean {
        if (fromPath.isEmpty() || toPath.isEmpty() || fileName.isEmpty()) {
            return false
        }
        var newFromPath = fromPath
        if (!fromPath.endsWith(File.separator)) {
            newFromPath = fromPath + File.separator
        }
        val fromFile: File? = create(newFromPath + fileName)
        if (fromFile == null || !isFileExists(fromFile)) {
            return false
        }
        var newToPath = toPath
        if (!toPath.endsWith(File.separator)) {
            newToPath = toPath + File.separator
        }
        val toDirectoryFile: File = create(newToPath) ?: return false
        if (!toDirectoryFile.exists()) {
            toDirectoryFile.mkdirs()
        }
        if (!toDirectoryFile.isDirectory) {
            return false
        }
        val toFile: File = create(newToPath + fileName) ?: return false
        return fromFile.renameTo(toFile)
    }

    /** 复制文件[fileName]从[fromPath]到[toPath] */
    @JvmStatic
    fun copyFile(fromPath: String, toPath: String, fileName: String): Boolean {
        if (fromPath.isEmpty() || toPath.isEmpty() || fileName.isEmpty()) {
            return false
        }
        var newFromPath = fromPath
        if (!fromPath.endsWith(File.separator)) {
            newFromPath = fromPath + File.separator
        }
        val fromFile: File? = create(newFromPath + fileName)
        if (fromFile == null || !isFileExists(fromFile)) {
            return false
        }
        var newToPath = toPath
        if (!toPath.endsWith(File.separator)) {
            newToPath = toPath + File.separator
        }
        val toDirectoryFile: File = create(newToPath) ?: return false
        if (!toDirectoryFile.exists()) {
            toDirectoryFile.mkdirs()
        }
        if (!toDirectoryFile.isDirectory) {
            return false
        }
        val toFile: File = create(newToPath + fileName) ?: return false

        FileInputStream(fromFile).use { fis: FileInputStream ->
            FileOutputStream(toFile).use { fos: FileOutputStream ->
                fis.channel.use { inChannel: FileChannel? ->
                    fos.channel.use { outChannel: FileChannel? ->
                        if (inChannel != null && outChannel != null) {
                            inChannel.transferTo(0, inChannel.size(), outChannel)
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    /** 获取路径[filePath]下的文件总长度（返回结果携带单位） */
    @JvmStatic
    fun getFileTotalLengthUnit(filePath: String): String = formetFileSize(getFileTotalLength(filePath))

    /** 获取路径[filePath]下的文件总长度 */
    @JvmStatic
    fun getFileTotalLength(filePath: String): Long {
        val file: File? = create(filePath)

        if (file == null || !isFileExists(file)) {
            return 0
        }

        try {
            return if (file.isDirectory) getFileSizes(file) else getFileSize(file)
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
        if (files == null || files.isEmpty()) {
            return 0
        }
        var size: Long = 0
        for (f in files) {
            size += if (f.isDirectory) {
                getFileSizes(f)
            } else {
                getFileSize(f)
            }
        }
        return size
    }

    /** 将文件大小[fileSize]格式化单位：B、KB、MB、GB、TB */
    @JvmStatic
    fun formetFileSize(fileSize: Long): String {
        if (fileSize <= 0) {
            return "0KB"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups: Int = (Math.log10(fileSize.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("##0.0").format(fileSize / Math.pow(1024.0, digitGroups.toDouble())) + units[digitGroups]
    }

    /** 删除路径[filePath]的文件 */
    @JvmStatic
    fun delFile(filePath: String) {
        val file: File? = create(filePath)
        if (file == null || !isFileExists(file)) {
            return
        }

        try {
            if (file.isDirectory) {
                val files: Array<File>? = file.listFiles()
                if (files == null || files.isEmpty()) {
                    file.delete()
                    return
                }

                for (f in files) {
                    if (f.isFile) {
                        f.delete()
                    } else if (f.isDirectory) {
                        delFile(f.absolutePath)
                    }
                }
                file.delete()
            } else if (file.isFile) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** 文件路径[filePath]转byte数组 */
    @JvmStatic
    fun fileToByte(filePath: String): ByteArray? {
        if (filePath.isEmpty()) {
            return null
        }
        val file: File = create(filePath) ?: return null
        FileInputStream(file).use { fis: FileInputStream ->
            ByteArrayOutputStream().use { baos: ByteArrayOutputStream ->
                val b = ByteArray(1024)
                var n: Int
                while (true) {
                    n = fis.read(b)
                    if (n != -1) {
                        baos.write(b, 0, n)
                    } else {
                        break
                    }
                }
                return baos.toByteArray()
            }
        }
    }

    /** 将[bytes]数组保存为文件，文件保存路径[savePath]，文件名称[fileName] */
    @JvmStatic
    fun byteToFile(bytes: ByteArray, savePath: String, fileName: String) {
        if (bytes.isEmpty() || savePath.isEmpty() || fileName.isEmpty()) {
            return
        }
        var newFilePath = savePath
        if (!savePath.endsWith(File.separator)) {
            newFilePath += File.separator
        }
        val directory: File = create(savePath) ?: return
        if (!directory.exists()) {
            directory.mkdirs()
        }
        if (!directory.isDirectory) {
            return
        }
        val file: File = create(newFilePath + fileName) ?: return
        FileOutputStream(file).use { fos: FileOutputStream ->
            BufferedOutputStream(fos).use { bos: BufferedOutputStream ->
                bos.write(bytes)
            }
        }
    }

    /** 将[bitmap]保存为图片文件，保存路径[savePath]，文件名[fileName]，后缀[suffix]例如png或者.jpg，保存质量[quality] */
    @JvmStatic
    fun bitmapToPath(bitmap: Bitmap, savePath: String, fileName: String, suffix: String, @IntRange(from = 0, to = 100) quality: Int) {
        if (savePath.isEmpty() || fileName.isEmpty() || suffix.isEmpty()) {
            return
        }
        var newFilePath = savePath
        if (!savePath.endsWith(File.separator)) {
            newFilePath += File.separator
        }
        val directory: File = create(savePath) ?: return
        if (!directory.exists()) {
            directory.mkdirs()
        }
        if (!directory.isDirectory) {
            return
        }
        var newSuffix = suffix
        if (!suffix.startsWith(".")) {
            newSuffix = ".$suffix"
        }
        val file: File = create(newFilePath + fileName + newSuffix) ?: return
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        FileOutputStream(file).use { fos: FileOutputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos)
        }
        if (!bitmap.isRecycled) {
            bitmap.recycle()
        }
    }
}