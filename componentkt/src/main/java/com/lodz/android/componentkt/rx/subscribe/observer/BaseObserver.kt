package com.lodz.android.componentkt.rx.subscribe.observer

import com.lodz.android.componentkt.base.application.BaseApplication
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.utils.getMetaData
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * 基类订阅者（无背压）
 * Created by zhouL on 2018/7/5.
 */
abstract class BaseObserver<T> : Observer<T> {

    private val ERROR_TAG = "error_tag"

    private var mDisposable: Disposable? = null

    final override fun onSubscribe(d: Disposable) {
        mDisposable = d
        onBaseSubscribe(d)
    }

    final override fun onNext(any: T) {
        onBaseNext(any)
    }

    final override fun onError(t: Throwable) {
        t.printStackTrace()
        printTagLog(t)
        onBaseError(t)
    }

    /** 打印标签日志 */
    private fun printTagLog(t: Throwable) {
        if (BaseApplication.get() == null) {
            return
        }
        val tag = BaseApplication.get()!!.getMetaData(ERROR_TAG)
        if (tag != null && tag is String) {
            if (!tag.isEmpty()) {
                PrintLog.e(tag, t.toString(), t)
            }
        }
    }

    final override fun onComplete() {
        onBaseComplete()
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
            mDisposable!!.dispose()
            onDispose()
        }
    }

    open fun onBaseSubscribe(d: Disposable) {}

    abstract fun onBaseNext(any: T)

    abstract fun onBaseError(e: Throwable)

    open fun onBaseComplete() {}
    /** 取消订阅时回调 */
    open fun onDispose() {}
}