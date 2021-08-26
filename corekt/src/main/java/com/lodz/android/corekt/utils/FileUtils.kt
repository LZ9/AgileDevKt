package com.lodz.android.corekt.utils

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.annotation.IntRange
import com.lodz.android.corekt.anko.append
import java.io.*
import java.nio.channels.FileChannel
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

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
        if (newName == file.name) {// 新名称与旧名称一致
            return true
        }
        val parentPath = file.parent ?: return false
        val newFile = create(parentPath + File.separator + replaceName)
        return if (newFile != null) file.renameTo(newFile) else  false
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
            if (childFile.name.toLowerCase(Locale.getDefault()).endsWith(replaceSuffix.toLowerCase(Locale.getDefault()))) {
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
        return if (file.isDirectory) getFileSizes(file) else getFileSize(file)
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

    /** 用上下文[context]，通过[uri]将文件复制到指定的目录[toPath]（一般是自己的沙盒目录），并指定文件名[fileName]（需要后缀指定文件类型） */
    @JvmStatic
    fun copyFileFromUri(context: Context, uri: Uri, toPath: String, fileName: String): Boolean {
        if (uri == Uri.EMPTY || toPath.isEmpty() || fileName.isEmpty()) {
            return false
        }
        val newToPath = if (toPath.endsWith(File.separator)) toPath else toPath.append(File.separator)
        val toDirectoryFile = create(newToPath) ?: return false
        if (!toDirectoryFile.exists()) {
            toDirectoryFile.mkdirs()
        }
        if (!toDirectoryFile.isDirectory) {
            return false
        }
        val toFile = File(newToPath.append(fileName))
        if (toFile.exists()) {
            toFile.delete()
        }
        if (!toFile.createNewFile()) {
            return false
        }
        // FileInputStream指要读取的数据
        // FileOutputStream指要写入的数据
        context.contentResolver.openFileDescriptor(uri, "r")?.use { fd ->
            FileInputStream(fd.fileDescriptor).use { fis ->
                FileOutputStream(toFile).use { fos ->
                    fis.channel.use { inChannel ->
                        fos.channel.use { outChannel ->
                            if (inChannel != null && outChannel != null) {
                                inChannel.transferTo(0, inChannel.size(), outChannel)
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    /** 文件路径转uri */
    fun filePathToUri(context: Context, path: String): Uri {
        val mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor = context.contentResolver.query(
            mediaUri,
            null,
            MediaStore.Images.Media.DISPLAY_NAME + "= ?",
            arrayOf(path.substring(path.lastIndexOf("/") + 1)),
            null
        ) ?: return Uri.EMPTY
        var uri: Uri? = null
        if (cursor.moveToFirst()) {
            uri = ContentUris.withAppendedId(
                mediaUri,
                cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
            )
        }
        cursor.close()
        return uri ?: Uri.EMPTY
    }

    /** 把[path]文件转Base64，转码类型[flags]默认Base64.NO_WRAP */
    @JvmStatic
    @JvmOverloads
    fun fileToBase64(context: Context, uri: Uri, toPath: String, fileName: String, flags: Int = Base64.NO_WRAP): String {
        val isSuccess = copyFileFromUri(context, uri, toPath, fileName)
        if (!isSuccess) {
            return ""
        }
        val newToPath = if (toPath.endsWith(File.separator)) toPath else toPath.append(File.separator)
        return fileToBase64(newToPath.append(fileName), flags)
    }

    /** 把[path]文件转Base64，转码类型[flags]默认Base64.NO_WRAP */
    @JvmStatic
    @JvmOverloads
    fun fileToBase64(path: String, flags: Int = Base64.NO_WRAP): String = fileToBase64(File(path), flags)

    /** 把[file]文件转Base64，转码类型[flags]默认Base64.NO_WRAP */
    @JvmStatic
    @JvmOverloads
    fun fileToBase64(file: File, flags: Int = Base64.NO_WRAP): String {
        if (!file.exists()) {
            return ""
        }
        FileInputStream(file).use {
            val bytes = ByteArray(it.available())
            val length = it.read(bytes)
            return Base64.encodeToString(bytes, 0, length, flags) ?: ""
        }
    }

    /** 把[base64]转文件，文件路径[toPath]，保存文件名[fileName]，转码类型[flags]默认Base64.NO_WRAP */
    @JvmStatic
    @JvmOverloads
    fun Base64ToFile(base64: String, toPath: String, fileName: String, flags: Int = Base64.NO_WRAP): File? {
        if (base64.isEmpty() || toPath.isEmpty() || fileName.isEmpty()) {
            return null
        }
        val newToPath = if (toPath.endsWith(File.separator)) toPath else toPath.append(File.separator)
        val toDirectoryFile = create(newToPath) ?: return null
        if (!toDirectoryFile.exists()) {
            toDirectoryFile.mkdirs()
        }
        if (!toDirectoryFile.isDirectory) {
            return null
        }
        val toFile = File(newToPath.append(fileName))
        if (toFile.exists()) {
            toFile.delete()
        }
        if (!toFile.createNewFile()) {
            return null
        }
        val bytes = Base64.decode(base64, flags)
        FileOutputStream(toFile).use {
            it.write(bytes)
        }
        return toFile
    }


    /** 获取路径[filePath]下的所有文件列表 */
    @JvmStatic
    fun getFileList(filePath: String): ArrayList<File> {
        val file: File? = create(filePath)
        if (file == null || !isFileExists(file)) {
            return ArrayList()
        }
        return if (file.isDirectory) getFiles(file) else arrayListOf(file)
    }

    /** 获取文件列表 */
    private fun getFiles(file: File): ArrayList<File> {
        val files: Array<File>? = file.listFiles()
        if (files == null || files.isEmpty()) {
            return ArrayList()
        }
        val list = ArrayList<File>()
        for (f in files) {
            list.addAll(if (f.isDirectory) getFiles(f) else arrayListOf(f))
        }
        return list
    }





    //   fun base64ToBitmap(base64Data: String, flags: Int = Base64.NO_WRAP): Bitmap? {
    //        val bytes = base64ToByte(base64Data, flags)
    //        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    //    }

    ///**往FileDescriptor中写入数据
    // * @param fileDescriptor
    // * @param content
    // */
    //private void writeData(FileDescriptor fileDescriptor, String content) {
    //    FileOutputStream fos = new FileOutputStream(fileDescriptor);
    //    try {
    //        fos.write(content.getBytes());
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    } finally {
    //        try {
    //            fos.close();
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    }
    //}
    //
    ///**从FileDescriptor中读取数据
    // * @param fileDescriptor
    // * @return
    // */
    //private String readData(FileDescriptor fileDescriptor) {
    //    FileInputStream fis = new FileInputStream(fileDescriptor);
    //    byte[] b = new byte[1024];
    //    int read;
    //    String content=null;
    //    try {
    //        while ((read = fis.read(b)) != -1) {
    //            content = new String(b, 0, read);
    //        }
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    } finally {
    //        try {
    //            fis.close();
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    }
    //    return content;
    //}

    //private void openFile(String type, Uri uri) {
    //    if (type.contains("image/")) {
    //        try {
    //            ParcelFileDescriptor descriptor = getContentResolver().openFileDescriptor(uri, "r");
    //            FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
    //            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
    //            mShowPic_iv.setVisibility(View.VISIBLE);
    //            mShowPic_iv.setImageBitmap(bitmap);
    //        } catch (FileNotFoundException e) {
    //            e.printStackTrace();
    //        }
    //    }
    //}


    //3、copy或者下载文件到公有目录，保存Bitmap同理，如Download，MIME_TYPE类型可以自行参考对应的文件类型，这里只对APK作出说明,从私有目录copy到公有目录demo如下（远程下载同理，只要拿到OutputStream即可，亦可下载到私有目录再copy到公有目录）：
    //public static void copyToDownloadAndroidQ(Context context, String sourcePath, String fileName, String saveDirName){
    //  ContentValues values = new ContentValues();
    //  values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
    //  values.put(MediaStore.Downloads.MIME_TYPE, "application/vnd.android.package-archive");
    //  values.put(MediaStore.Downloads.RELATIVE_PATH, "Download/" + saveDirName.replaceAll("/","") + "/");
    //
    //  Uri external = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
    //  ContentResolver resolver = context.getContentResolver();
    //
    //  Uri insertUri = resolver.insert(external, values);
    //  if(insertUri == null) {
    //   return;
    //  }
    //
    //  String mFilePath = insertUri.toString();
    //
    //  InputStream is = null;
    //  OutputStream os = null;
    //  try {
    //   os = resolver.openOutputStream(insertUri);
    //   if(os == null){
    //    return;
    //   }
    //   int read;
    //   File sourceFile = new File(sourcePath);
    //   if (sourceFile.exists()) { // 文件存在时
    //    is = new FileInputStream(sourceFile); // 读入原文件
    //    byte[] buffer = new byte[1444];
    //    while ((read = is.read(buffer)) != -1) {
    //     os.write(buffer, 0, read);
    //    }
    //   }
    //  } catch (Exception e) {
    //   e.printStackTrace();
    //  }finally {
    //   close(is,os);
    //  }
    //
    //}
    //4、保存图片相关
    ///**
    //  * 通过MediaStore保存，兼容AndroidQ，保存成功自动添加到相册数据库，无需再发送广播告诉系统插入相册
    //  *
    //  * @param context  context
    //  * @param sourceFile 源文件
    //  * @param saveFileName 保存的文件名
    //  * @param saveDirName picture子目录
    //  * @return 成功或者失败
    //  */
    // public static boolean saveImageWithAndroidQ(Context context,
    //             File sourceFile,
    //             String saveFileName,
    //             String saveDirName) {
    //  String extension = BitmapUtil.getExtension(sourceFile.getAbsolutePath());
    //
    //  ContentValues values = new ContentValues();
    //  values.put(MediaStore.Images.Media.DESCRIPTION, "This is an image");
    //  values.put(MediaStore.Images.Media.DISPLAY_NAME, saveFileName);
    //  values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
    //  values.put(MediaStore.Images.Media.TITLE, "Image.png");
    //  values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + saveDirName);
    //
    //  Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    //  ContentResolver resolver = context.getContentResolver();
    //
    //  Uri insertUri = resolver.insert(external, values);
    //  BufferedInputStream inputStream = null;
    //  OutputStream os = null;
    //  boolean result = false;
    //  try {
    //   inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
    //   if (insertUri != null) {
    //    os = resolver.openOutputStream(insertUri);
    //   }
    //   if (os != null) {
    //    byte[] buffer = new byte[1024 * 4];
    //    int len;
    //    while ((len = inputStream.read(buffer)) != -1) {
    //     os.write(buffer, 0, len);
    //    }
    //    os.flush();
    //   }
    //   result = true;
    //  } catch (IOException e) {
    //   result = false;
    //  } finally {
    //   close(os, inputStream);
    //  }
    //  return result;
    //}
}