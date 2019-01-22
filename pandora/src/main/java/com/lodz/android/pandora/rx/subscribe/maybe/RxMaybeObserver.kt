package com.lodz.android.pandora.rx.subscribe.maybe

import com.lodz.android.pandora.rx.exception.DataException
import com.lodz.android.pandora.rx.exception.NetworkException
import com.lodz.android.pandora.rx.exception.RxExceptionFactory
import com.lodz.android.pandora.rx.status.ResponseStatus
import io.reactivex.disposables.Disposable

/**
 * 网络接口使用的订阅者，主要对接口进行判断处理
 * Created by zhouL on 2019/1/22.
 */
abstract class RxMaybeObserver<T> : BaseMaybeObserver<T>() {

    final override fun onBaseSubscribe(d: Disposable) {
        super.onBaseSubscribe(d)
        onRxSubscribe(d)
    }

    final override fun onBaseSuccess(any: T) {
        try {
            checkError(any)
            onRxSuccess(any)
        } catch (e: Exception) {
            onError(e)
        }
    }

    final override fun onBaseComplete() {
        onRxComplete()
    }

    final override fun onBaseError(e: Throwable) {
        val exception = RxExceptionFactory.create(e)
        onRxError(exception, exception is NetworkException)
        onErrorEnd()
    }

    /** 核对数据 */
    private fun checkError(any: T) {
        if (any == null) {
            throw NullPointerException("数据是空的")
        }
        if (any is ResponseStatus) {
            if (!any.isSuccess()) {//服务端返回接口失败
                val exception = DataException("response fail")
                exception.setData(any)
                throw exception
            }
        }
    }

    open fun onRxSubscribe(d: Disposable) {}

    abstract fun onRxSuccess(any: T)

    abstract fun onRxComplete()

    abstract fun onRxError(e: Throwable, isNetwork: Boolean)

    /** onError执行完后会调用该方法 */
    open fun onErrorEnd() {}
}