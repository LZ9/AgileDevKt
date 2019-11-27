package com.lodz.android.pandora.rx.subscribe.completable

import com.lodz.android.corekt.anko.getMetaData
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.application.BaseApplication
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable

/**
 * Completable基类订阅者
 * 不发送事件，以完成或失败结束
 * Created by zhouL on 2019/1/22.
 */
abstract class BaseCompletableObserver : CompletableObserver {

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
        val app = BaseApplication.get() ?: return
        val tag = app.getMetaData(BaseApplication.ERROR_TAG)
        if (tag != null && tag is String) {
            if (tag.isNotEmpty()) {
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
        @JvmStatic
        fun empty(): CompletableObserver = object : CompletableObserver {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {}
        }

        /** 创建lambda调用 */
        @JvmStatic
        @JvmOverloads
        fun action(
            complete: () -> Unit,
            error: (e: Throwable) -> Unit = {},
            subscribe: (d: Disposable) -> Unit = {},
            dispose: () -> Unit = {}
        ): BaseCompletableObserver = object : BaseCompletableObserver() {

            override fun onBaseSubscribe(d: Disposable) {
                super.onBaseSubscribe(d)
                subscribe(d)
            }

            override fun onBaseComplete() {
                complete()
            }

            override fun onBaseError(e: Throwable) {
                error(e)
            }

            override fun onDispose() {
                super.onDispose()
                dispose()
            }
        }
    }
}