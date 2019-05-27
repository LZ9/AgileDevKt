package com.lodz.android.pandora.rx.subscribe.completable

import com.lodz.android.pandora.rx.exception.NetworkException
import com.lodz.android.pandora.rx.exception.RxExceptionFactory
import io.reactivex.disposables.Disposable

/**
 * 网络接口使用的订阅者，主要对接口进行判断处理
 * Created by zhouL on 2019/1/22.
 */
abstract class RxCompletableObserver : BaseCompletableObserver() {

    final override fun onBaseSubscribe(d: Disposable) {
        super.onBaseSubscribe(d)
        onRxSubscribe(d)
    }

    final override fun onBaseComplete() {
        onRxComplete()
    }

    final override fun onBaseError(e: Throwable) {
        val exception = RxExceptionFactory.create(e)
        onRxError(exception, exception is NetworkException)
        onErrorEnd()
    }

    open fun onRxSubscribe(d: Disposable) {}

    abstract fun onRxComplete()

    abstract fun onRxError(e: Throwable, isNetwork: Boolean)

    /** onError执行完后会调用该方法 */
    open fun onErrorEnd() {}

    companion object {
        /** 创建lambda调用 */
        @JvmStatic
        fun action(complete: () -> Unit, error: (e: Throwable, isNetwork: Boolean) -> Unit): RxCompletableObserver = object : RxCompletableObserver() {
            override fun onRxComplete() {
                complete()

            }

            override fun onRxError(e: Throwable, isNetwork: Boolean) {
                error(e, isNetwork)
            }
        }
    }
}