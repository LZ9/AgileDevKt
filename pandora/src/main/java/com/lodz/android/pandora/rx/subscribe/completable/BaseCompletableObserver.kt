package com.lodz.android.pandora.rx.subscribe.completable

import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.utils.getMetaData
import com.lodz.android.pandora.base.application.BaseApplication
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable

/**
 * 基类订阅者
 * Created by zhouL on 2019/1/22.
 */
abstract class BaseCompletableObserver : CompletableObserver {

    private val ERROR_TAG = "error_tag"

    private var mDisposable: Disposable? = null

    final override fun onSubscribe(d: Disposable) {
        mDisposable = d
        onBaseSubscribe(d)
    }

    final override fun onComplete() {
        onBaseComplete()
    }

    final override fun onError(e: Throwable) {
        e.printStackTrace()
        printTagLog(e)
        onBaseError(e)
    }

    /** 打印标签日志 */
    private fun printTagLog(t: Throwable) {
        val app = BaseApplication.get()
        if (app == null) {
            return
        }
        val tag = app.getMetaData(ERROR_TAG)
        if (tag != null && tag is String) {
            if (!tag.isEmpty()) {
                PrintLog.e(tag, t.toString(), t)
            }
        }
    }

    fun getDisposable(): Disposable? {
        return mDisposable
    }

    fun clearDisposable() {
        mDisposable = null
    }

    /** 停止订阅 */
    fun dispose() {
        if (mDisposable != null) {
            mDisposable?.dispose()
            onDispose()
        }
    }

    open fun onBaseSubscribe(d: Disposable) {}

    abstract fun onBaseComplete()

    abstract fun onBaseError(e: Throwable)

    /** 取消订阅时回调 */
    open fun onDispose() {}

    companion object {
        /** 创建空调用 */
        fun empty(): CompletableObserver = object : CompletableObserver {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {}
        }
    }
}