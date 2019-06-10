package com.lodz.android.corekt.utils

import android.os.Handler
import android.os.Looper
import android.os.Message

/**
 * 把runnable post到UI线程执行的工具类
 * Created by zhouL on 2018/6/26.
 */
object UiHandler {

    // 获取主线程的Handler
    private var sHandler: Handler = Handler(Looper.getMainLooper())

    /** 在UI线程执行[runnable] */
    @JvmStatic
    fun post(runnable: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run()
        } else {
            sHandler.post(runnable)
        }
    }

    /** 在UI线程执行[block] */
    @JvmStatic
    fun post(block: () -> Unit) {
        val runnable = Runnable {
            block()
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run()
        } else {
            sHandler.post(runnable)
        }
    }

    /** 在UI线程执行[runnable]，并指定[token] */
    @JvmStatic
    fun post(runnable: Runnable, token: Any) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run()
        } else {
            val message = Message.obtain(sHandler, runnable)
            message.obj = token
            sHandler.sendMessage(message)
        }
    }

    /** 在UI线程执行[block]，并指定[token] */
    @JvmStatic
    fun post(token: Any, block: () -> Unit) {
        val runnable = Runnable {
            block()
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run()
        } else {
            val message = Message.obtain(sHandler, runnable)
            message.obj = token
            sHandler.sendMessage(message)
        }
    }

    /** 延迟[delay]毫秒执行[runnable] */
    @JvmStatic
    fun postDelayed(runnable: Runnable, delay: Long) {
        sHandler.postDelayed(runnable, delay)
    }

    /** 延迟[delay]毫秒执行[block] */
    @JvmStatic
    fun postDelayed(delay: Long, block: () -> Unit) {
        sHandler.postDelayed({ block() }, delay)
    }

    /** 延迟[delay]毫秒执行[runnable]，并指定[token] */
    @JvmStatic
    fun postDelayed(runnable: Runnable, delay: Long, token: Any) {
        val message = Message.obtain(sHandler, runnable)
        message.obj = token
        sHandler.sendMessageDelayed(message, delay)
    }

    /** 延迟[delay]毫秒执行[block]，并指定[token] */
    @JvmStatic
    fun postDelayed(delay: Long, token: Any, block: () -> Unit) {
        val message = Message.obtain(sHandler) { block() }
        message.obj = token
        sHandler.sendMessageDelayed(message, delay)
    }

    /** 移除指定[token]的Runnable（token传null则移除所有的Runnable） */
    @JvmStatic
    fun remove(token: Any?) {
        sHandler.removeCallbacksAndMessages(token)
    }

    /** 销毁Handler */
    @JvmStatic
    fun destroy() {
        remove(null)
    }

}