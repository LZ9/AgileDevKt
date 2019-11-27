package com.lodz.android.pandora.rx.subscribe.observer

import com.lodz.android.corekt.anko.getMetaData
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.application.BaseApplication
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * 基类订阅者（无背压）
 * 能够发射0或n个数据，并以完成或错误事件终止。
 * Created by zhouL on 2018/7/5.
 */
abstract class BaseObserver<T> : Observer<T> {

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
        val app = BaseApplication.get() ?: return
        val tag = app.getMetaData(BaseApplication.ERROR_TAG)
        if (tag != null && tag is String) {
            if (tag.isNotEmpty()) {
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
            mDisposable?.dispose()
            onDispose()
        }
    }

    open fun onBaseSubscribe(d: Disposable) {}

    abstract fun onBaseNext(any: T)

    abstract fun onBaseError(e: Throwable)

    open fun onBaseComplete() {}
    /** 取消订阅时回调 */
    open fun onDispose() {}

    companion object {
        /** 创建空调用 */
        @JvmStatic
        fun <T> empty(): Observer<T> = object : Observer<T> {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: T) {}
            override fun onError(e: Throwable) {}
        }

        /** 创建lambda调用 */
        @JvmStatic
        @JvmOverloads
        fun <T> action(
            next: (any: T) -> Unit,
            error: (e: Throwable) -> Unit = {},
            subscribe: (d: Disposable) -> Unit = {},
            complete: () -> Unit = {},
            dispose: () -> Unit = {}
        ): BaseObserver<T> = object : BaseObserver<T>() {

            override fun onBaseSubscribe(d: Disposable) {
                super.onBaseSubscribe(d)
                subscribe(d)
            }

            override fun onBaseNext(any: T) {
                next(any)
            }

            override fun onBaseError(e: Throwable) {
                error(e)
            }

            override fun onBaseComplete() {
                super.onBaseComplete()
                complete()
            }

            override fun onDispose() {
                super.onDispose()
                dispose()
            }
        }
    }
}