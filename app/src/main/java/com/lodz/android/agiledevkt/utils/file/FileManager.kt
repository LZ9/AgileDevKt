package com.lodz.android.agiledevkt.utils.file

import android.text.TextUtils
import com.lodz.android.agiledevkt.App
import com.lodz.android.corekt.anko.getStoragePath
import com.lodz.android.corekt.utils.FileUtils
import java.io.File

/**
 * 文件管理
 * Created by zhouL on 2018/6/28.
 */
object FileManager {

    /** 存储卡是否可用 */
    private var isStorageCanUse = false
    /** app主文件夹路径 */
    private var mAppFolderPath = ""
    /** 缓存路径 */
    private var mCacheFolderPath = ""
    /** 下载路径 */
    private var mDownloadFolderPath = ""
    /** 内容路径 */
    private var mContentFolderPath = ""
    /** 崩溃日志路径 */
    private var mCrashFolderPath = ""

    @JvmStatic
    fun init() {
        initPath()
        if (isStorageCanUse) {
            initFolder()
        }
    }

    /** 初始化路径 */
    private fun initPath() {
        val storagePathPair = App.get().getStoragePath()
        var rootPath: String? = storagePathPair.first // 先获取内置存储路径
        if (rootPath.isNullOrEmpty()) {// 内置为空再获取外置
            rootPath = storagePathPair.second
        }
        if (rootPath.isNullOrEmpty()) {// 没有可用的存储路径
            isStorageCanUse = false
            return
        }
        // 成功获取到存储路径
        isStorageCanUse = true
        if (!rootPath.endsWith(File.separator)) {
            rootPath += File.separator
        }
        mAppFolderPath = rootPath + "AgileDevKt" + File.separator// 主文件夹路径
        mCacheFolderPath = mAppFolderPath + "Cache" + File.separator// 缓存路径
        mDownloadFolderPath = mAppFolderPath + "Download" + File.separator// 下载路径
        mContentFolderPath = mAppFolderPath + "Content" + File.separator// 内容路径
        mCrashFolderPath = mAppFolderPath + "Crash" + File.separator// 崩溃日志路径
    }

    /** 初始化文件夹 */
    private fun initFolder() {
        try {
            FileUtils.createFolder(mAppFolderPath)// 主文件夹路径
            FileUtils.createFolder(mCacheFolderPath)// 缓存路径
            FileUtils.createFolder(mDownloadFolderPath)// 下载路径
            FileUtils.createFolder(mContentFolderPath)// 内容路径
            FileUtils.createFolder(mCrashFolderPath)// 崩溃日志路径
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** 存储是否可用 */
    @JvmStatic
    fun isStorageCanUse(): Boolean = isStorageCanUse

    /** 获取app主文件夹路径 */
    @JvmStatic
    fun getAppFolderPath(): String = fixPath(mAppFolderPath)

    /** 获取缓存路径 */
    @JvmStatic
    fun getCacheFolderPath(): String = fixPath(mCacheFolderPath)

    /** 获取下载路径 */
    @JvmStatic
    fun getDownloadFolderPath(): String = fixPath(mDownloadFolderPath)

    /** 获取内容路径 */
    @JvmStatic
    fun getContentFolderPath(): String = fixPath(mContentFolderPath)

    /** 获取崩溃日志路径 */
    @JvmStatic
    fun getCrashFolderPath(): String = fixPath(mCrashFolderPath)

    /** 修复文件夹路径[path] */
    private fun fixPath(path: String): String {
        if (TextUtils.isEmpty(path)) {
            // 路径为空说明未初始化
            init()
        }
        if (isStorageCanUse && !FileUtils.isFileExists(path)) {
            //存储可用 && 路径下的文件夹不存在 说明文件夹被删除
            initFolder()
        }
        return path
    }
}