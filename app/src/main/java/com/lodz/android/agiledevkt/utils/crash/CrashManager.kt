package com.lodz.android.agiledevkt.utils.crash

import android.content.Intent
import android.os.Looper
import android.widget.Toast
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.corekt.anko.getAppName
import com.lodz.android.corekt.anko.getVersionCode
import com.lodz.android.corekt.anko.getVersionName
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.corekt.utils.DeviceUtils
import com.lodz.android.corekt.utils.FileUtils
import com.lodz.android.pandora.base.application.BaseApplication
import java.io.*
import java.util.*

/**
 * 崩溃管理类
 * Created by zhouL on 2018/9/17.
 */
class CrashManager private constructor() : Thread.UncaughtExceptionHandler {

    companion object {
        private val sInstance = CrashManager()
        @JvmStatic
        fun get(): CrashManager = sInstance
    }

    /** 日志标签 */
    private var mTag = "CrashTag"
    /** 系统默认的UncaughtException处理类 */
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    /** 是否拦截 */
    private var isInterceptor = true
    /** 提示语 */
    private var mToastTips = "很抱歉，程序出现异常即将退出"
    /** 保存的文件夹路径 */
    private var mSaveFolderPath = ""
    /** 日志文件名及后缀 */
    private var mFileName = ""
    /** 启动页的Class */
    private var mClass: Class<*>? = null

    /** 设置是否对异常进行拦截[interceptor] */
    fun setInterceptor(interceptor: Boolean): CrashManager {
        isInterceptor = interceptor
        return this
    }

    /** 设置异常提示语[tips]（不设置使用默认提示语） */
    fun setToastTips(tips: String): CrashManager {
        if (tips.isNotEmpty()) {
            mToastTips = tips
        }
        return this
    }

    /** 设置保存的文件夹路径[path] */
    fun setSaveFolderPath(path: String): CrashManager {
        mSaveFolderPath = path
        return this
    }

    /** 设置日志文件名及后缀[fileName]（不设置使用默认文件名） */
    fun setFileName(fileName: String): CrashManager {
        mFileName = fileName
        return this
    }

    /** 设置启动页[cls]，app崩溃后会重启该类 */
    fun setLauncherClass(cls: Class<*>): CrashManager {
        mClass = cls
        return this
    }

    /** 设置打印标签[tag] */
    fun setLogTag(tag: String): CrashManager {
        if (tag.isNotEmpty()) {
            mTag = tag
        }
        return this
    }

    /** 构建代码代码 */
    fun build() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread?, t: Throwable?) {
        if (isInterceptor) {// 用户处理
            PrintLog.d(mTag, "user handle")
            handleException(t)// 具体处理类
            try {
                Thread.sleep(2500)
            } catch (e: Exception) {
            }
            if (t != null) {
                PrintLog.e(mTag, "error: ", t)
            }
            exceptionExit()
            return
        }

        if (mDefaultHandler != null) {// 系统处理
            PrintLog.d(mTag, "system handle")
            mDefaultHandler?.uncaughtException(thread, t)
            return
        }
        PrintLog.d(mTag, "unhandle")
        exceptionExit()// 没有处理的情况下直接退出
    }

    /** 异常退出 */
    private fun exceptionExit() {
        val app = BaseApplication.get()
        if (mClass != null && app != null) {
            // 闪退后重新打开启动页而不是当前页
            val intent = Intent(app, mClass)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            app.startActivity(intent)
        }
        app?.exit()
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(1)// 非0表示异常退出
    }

    /** 处理异常[t] */
    private fun handleException(t: Throwable?) {
        showToast()// 显示提示
        val deviceInfos = getDeviceInfos()// 获取设备信息
        val content = getLogContent(deviceInfos, t)// 获取日志内容
        PrintLog.d(mTag, "保存崩溃日志 ： " + saveCrashLogInFile(content))// 将日志内容保存到文件夹
//        customHandle(deviceInfos, content, t)// 自定义扩展操作
    }

    /** 显示提示语 */
    private fun showToast() {
        Thread(Runnable {
            Looper.prepare()
            val app = BaseApplication.get()
            if (app != null) {
                Toast.makeText(app.applicationContext, mToastTips, Toast.LENGTH_LONG).show()
            }
            Looper.loop()
        }).start()
    }

    /** 获取设备信息 */
    private fun getDeviceInfos(): Map<String, String> {
        val app = BaseApplication.get() ?: return HashMap()
        val infos = DeviceUtils.getDeviceInfo().toMutableMap()
        infos["appName"] = app.getAppName()
        infos["packageName"] = app.packageName
        infos["versionName"] = app.getVersionName()
        infos["versionCode"] = app.getVersionCode().toString()
        for (info in infos) {
            PrintLog.i(mTag, info.key + " : " + info.value)
        }
        return infos
    }

    /** 获取设备信息[deviceInfos]和异常内容[t]日志 */
    private fun getLogContent(deviceInfos: Map<String, String>, t: Throwable?): String {
        val stringBuilder = StringBuilder()
        for (info in deviceInfos) {
            stringBuilder.append(info.key + "=" + info.value + "\n")
        }
        if (t == null) {
            return stringBuilder.toString()
        }

        StringWriter().use { writer: Writer ->
            PrintWriter(writer).use { printWriter: PrintWriter ->
                t.printStackTrace(printWriter)
                var cause = t.cause
                while (cause != null) {
                    cause.printStackTrace(printWriter)
                    cause = cause.cause
                }
                return stringBuilder.append(writer.toString()).toString()
            }
        }
    }

    /** 保存崩溃信息[content]到本地文件 */
    private fun saveCrashLogInFile(content: String): Boolean {
        if (mSaveFolderPath.isEmpty()) {
            mSaveFolderPath = FileManager.getCrashFolderPath()// 存到默认的崩溃文件夹底下
        }
        if (mSaveFolderPath.isEmpty()) {//保存路径为空时不保存数据
            return false
        }
        if (!mSaveFolderPath.endsWith(File.separator)) {// 判断路径结尾符
            mSaveFolderPath += File.separator
        }
        FileUtils.createFolder(mSaveFolderPath)// 创建文件夹
        if (mFileName.isEmpty()) {// 文件名为空时使用默认名称
            val timestamp = System.currentTimeMillis()
            val time = DateUtils.getFormatString(DateUtils.TYPE_4, Date(timestamp))
            mFileName = "crash-$time-$timestamp.log"
        }
        FileOutputStream(mSaveFolderPath + mFileName).use { fos: FileOutputStream ->
            try {
                fos.write(content.toByteArray())
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
    }

}
