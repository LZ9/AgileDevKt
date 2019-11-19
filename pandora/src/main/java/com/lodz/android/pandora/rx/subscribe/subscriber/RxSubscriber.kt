package com.lodz.android.pandora.rx.subscribe.subscriber

import com.lodz.android.pandora.rx.exception.DataException
import com.lodz.android.pandora.rx.exception.NetworkException
import com.lodz.android.pandora.rx.exception.RxExceptionFactory
import com.lodz.android.pandora.rx.status.ResponseStatus
import org.reactivestreams.Subscription

/**
 * 网络接口使用的订阅者（带背压），主要对接口进行判断处理
 * Created by zhouL on 2018/7/6.
 */
abstract class RxSubscriber<T> : BaseSubscriber<T>() {

    final override fun onBaseSubscribe(s: Subscription?) {
        super.onBaseSubscribe(s)
        onRxSubscribe(s)
    }

    final override fun onBaseComplete() {
        super.onBaseComplete()
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

    open fun onRxSubscribe(s: Subscription?) {}

    abstract fun onRxNext(any: T)

    abstract fun onRxError(e: Throwable, isNetwork: Boolean)

    open fun onRxComplete() {}

    /** onError执行完后会调用该方法 */
    open fun onErrorEnd() {}

    companion object {
        /** 创建lambda调用 */
        @JvmStatic
        fun <T> action(next: (any: T) -> Unit, error: (e: Throwable, isNetwork: Boolean) -> Unit): RxSubscriber<T> = object : RxSubscriber<T>() {
            override fun onRxNext(any: T) {
                next(any)
            }

            override fun onRxError(e: Throwable, isNetwork: Boolean) {
                error(e, isNetwork)
            }
        }
    }
}