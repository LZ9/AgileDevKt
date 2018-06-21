package com.lodz.android.corekt.log

import android.annotation.SuppressLint
import android.support.annotation.IntDef
import android.text.TextUtils
import android.util.Log

/**
 * 日志打印
 * Created by zhouL on 2018/6/20.
 */
object PrintLog {

    private const val LOG_I = 0
    private const val LOG_V = 1
    private const val LOG_D = 2
    private const val LOG_W = 3
    private const val LOG_E = 4

    @IntDef(LOG_I, LOG_V, LOG_D, LOG_W, LOG_E)
    @Retention(AnnotationRetention.SOURCE)
    annotation class LogType

    /** 是否打印日志  */
    private var isPrint = true

    /** 通过[isPrint]来控制是否打开日志 */
    fun setPrint(isPrint: Boolean) {
        this.isPrint = isPrint
    }

    /** 打印INFO类型日志。[tag]为日志标签，[log]为日志内容 */
    fun i(tag: String, log: String?) {
        if (isPrint) {
            logByType(LOG_I, tag, log)
        }
    }

    /** 打印INFO类型分段日志。[tag]为日志标签，[log]为日志内容 */
    fun iS(tag: String, log: String?) {
        if (!isPrint) {
            return
        }
        if (TextUtils.isEmpty(log)) {
            logByType(LOG_I, tag, log)
        } else {
            logSegmented(LOG_I, tag, log!!)
        }
    }

    /** 打印VERBOSE类型日志。[tag]为日志标签，[log]为日志内容 */
    fun v(tag: String, log: String?) {
        if (isPrint) {
            logByType(LOG_V, tag, log)
        }
    }

    /** 打印VERBOSE类型分段日志。[tag]为日志标签，[log]为日志内容 */
    fun vS(tag: String, log: String?) {
        if (!isPrint) {
            return
        }
        if (TextUtils.isEmpty(log)) {
            logByType(LOG_V, tag, log)
        } else {
            logSegmented(LOG_V, tag, log!!)
        }
    }

     /** 打印DEBUG类型日志。[tag]为日志标签，[log]为日志内容 */
    fun d(tag: String, log: String?) {
        if (isPrint) {
            logByType(LOG_D, tag, log)
        }
    }

    /** 打印DEBUG类型分段日志。[tag]为日志标签，[log]为日志内容 */
    fun dS(tag: String, log: String?) {
        if (!isPrint) {
            return
        }
        if (TextUtils.isEmpty(log)) {
            logByType(LOG_D, tag, log)
        } else {
            logSegmented(LOG_D, tag, log!!)
        }
    }

    /** 打印WARN类型日志。[tag]为日志标签，[log]为日志内容 */
    fun w(tag: String, log: String?) {
        if (isPrint) {
            logByType(LOG_W, tag, log)
        }
    }

    /** 打印WARN类型分段日志。[tag]为日志标签，[log]为日志内容 */
    fun wS(tag: String, log: String?) {
        if (!isPrint) {
            return
        }
        if (TextUtils.isEmpty(log)) {
            logByType(LOG_W, tag, log)
        } else {
            logSegmented(LOG_W, tag, log!!)
        }
    }

    /** 打印ERROR类型日志。[tag]为日志标签，[log]为日志内容 */
    fun e(tag: String, log: String?) {
        if (isPrint) {
            logByType(LOG_E, tag, log)
        }
    }

    /** 打印ERROR类型分段日志。[tag]为日志标签，[log]为日志内容 */
    fun eS(tag: String, log: String?) {
        if (!isPrint) {
            return
        }
        if (TextUtils.isEmpty(log)) {
            logByType(LOG_E, tag, log)
        } else {
            logSegmented(LOG_E, tag, log!!)
        }
    }

    /** 打印ERROR类型日志。[tag]为日志标签，[log]为日志内容 */
    fun e(tag: String, log: String?, t : Throwable) {
        if (!isPrint) {
            return
        }
        if (TextUtils.isEmpty(log)) {
            logByType(LOG_E, tag, log)
        }else{
            Log.e(tag, log, t)
        }
    }

    /** 打印分段日志。[type]为日志类型，[tag]为日志标签，[log]为日志内容 */
    private fun logSegmented(@LogType type: Int, tag: String, log: String) {
        if (log.length < 3000) {
            logByType(type, tag, log)
            return
        }
        val index = Math.ceil(log.length / 3000.0).toInt()
        for (i in 0..(index - 1)){
            val start = i * 3000
            var end = 3000 + i * 3000
            if (end >= log.length){
                end = log.length
            }
            logByType(type, tag, log.substring(start, end))
            if (end == log.length){
                return
            }
        }
    }

    /** 根据日志类型打印，[type]为日志类型，[tag]为日志标签，[log]为日志内容 */
    @SuppressLint("SwitchIntDef")
    private fun logByType(@LogType type: Int, tag: String, log: String?) {
        when (type) {
            LOG_I -> Log.i(tag, log ?: "NULL")
            LOG_V -> Log.v(tag, log ?: "NULL")
            LOG_D -> Log.d(tag, log ?: "NULL")
            LOG_W -> Log.w(tag, log ?: "NULL")
            LOG_E -> Log.e(tag, log ?: "NULL")
            else -> Log.i(tag, log ?: "NULL")
        }
    }
}