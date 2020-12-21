package com.lodz.android.pandora.rx.subscribe.single

import com.lodz.android.corekt.anko.getMetaData
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.application.BaseApplication
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable

/**
 * 基类订阅者
 * 只发射单个数据或错误事件
 * Created by zhouL on 2019/1/18.
 */
abstract class BaseSingleObserver<T> : SingleObserver<T> {

    private var mDisposable: Disposable? = null

    final override fun onSubscribe(d: Disposable) {
        mDisposable = d
        onBaseSubscribe(d)
    }

    final override fun onSuccess(t: T) {
        onBaseSuccess(t)
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

    abstract fun onBaseSuccess(any: T)

    abstract fun onBaseError(e: Throwable)

    /** 取消订阅时回调 */
    open fun onDispose() {}

    companion object {
        /** 创建空调用 */
        @JvmStatic
        fun <T> empty(): SingleObserver<T> = object : SingleObserver<T> {
            override fun onSuccess(t: T) {}
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {}
        }

        /** 创建lambda调用 */
        @JvmStatic
        @JvmOverloads
        fun <T> action(
            success: (any: T) -> Unit,
            error: (e: Throwable) -> Unit = {},
            subscribe: (d: Disposable) -> Unit = {},
            dispose: () -> Unit = {}
        ): BaseSingleObserver<T> = object : BaseSingleObserver<T>() {

            override fun onBaseSubscribe(d: Disposable) {
                super.onBaseSubscribe(d)
                subscribe(d)
            }

            override fun onBaseSuccess(any: T) {
                success(any)
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