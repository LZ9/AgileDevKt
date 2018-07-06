package com.lodz.android.componentkt.rx.subscribe.observer

import com.lodz.android.componentkt.rx.exception.DataException
import com.lodz.android.componentkt.rx.exception.NetworkException
import com.lodz.android.componentkt.rx.exception.RxExceptionFactory
import com.lodz.android.componentkt.rx.status.ResponseStatus
import io.reactivex.disposables.Disposable

/**
 * 网络接口使用的订阅者（无背压），主要对接口进行判断处理
 * Created by zhouL on 2018/7/5.
 */
abstract class RxObserver<T> : BaseObserver<T>() {

    final override fun onBaseSubscribe(d: Disposable) {
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

    final override fun onBaseNext(any: T) {
        try {
            checkError(any)
            onRxNext(any)
        } catch (e: Exception) {
            onError(e)
        }
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

    abstract fun onRxNext(any: T)

    abstract fun onRxError(e: Throwable, isNetwork: Boolean)

    open fun onRxComplete() {}

    /** onError执行完后会调用该方法 */
    open fun onErrorEnd() {}
}