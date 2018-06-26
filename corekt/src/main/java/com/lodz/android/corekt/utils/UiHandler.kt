package com.lodz.android.corekt.utils

import android.os.Handler
import android.os.Looper
import android.os.Message

/**
 * 把runnable post到UI线程执行的工具类
 * Created by zhouL on 2018/6/26.
 */
object UiHandler {

    private var sHandler: Handler

    init {
        // 获取主线程的Handler
        sHandler = Handler(Looper.getMainLooper())
    }

    /** 在UI线程执行[runnable] */
    fun post(runnable: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run()
        } else {
            sHandler.post(runnable)
        }
    }

    /** 在UI线程执行[runnable]，并指定[token] */
    fun post(runnable: Runnable, token: Any) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run()
        } else {
            val message = Message.obtain(sHandler, runnable)
            message.obj = token
            sHandler.sendMessage(message)
        }
    }

    /** 延迟[delay]毫秒执行[runnable] */
    fun postDelayed(runnable: Runnable, delay: Long) {
        sHandler.postDelayed(runnable, delay)
    }

    /** 延迟[delay]毫秒执行[runnable]，并指定[token] */
    fun postDelayed(runnable: Runnable, delay: Long, token: Any) {
        val message = Message.obtain(sHandler, runnable)
        message.obj = token
        sHandler.sendMessageDelayed(message, delay)
    }

    /** 移除指定[token]的Runnable（token传null则移除所有的Runnable） */
    fun remove(token: Any?) {
        sHandler.removeCallbacksAndMessages(token)
    }

    /** 销毁Handler */
    fun destroy(){
        remove(null)
    }

}