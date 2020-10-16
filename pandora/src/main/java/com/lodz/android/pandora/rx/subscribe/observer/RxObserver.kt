package com.lodz.android.pandora.rx.subscribe.observer

import com.lodz.android.pandora.rx.exception.DataException
import com.lodz.android.pandora.rx.exception.NetworkException
import com.lodz.android.pandora.rx.exception.RxExceptionFactory
import com.lodz.android.pandora.rx.status.ResponseStatus
import io.reactivex.rxjava3.disposables.Disposable

/**
 * 网络接口使用的订阅者（无背压），主要对接口进行判断处理
 * Created by zhouL on 2018/7/5.
 */
abstract class RxObserver<T> : BaseObserver<T>() {

    final override fun onBaseSubscribe(d: Disposable) {
        super.onBaseSubscribe(d)
        onRxSubscribe(d)
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
            if (any is ResponseStatus){
                if (any.isTokenUnauth()){
                    onTokenUnauth(any)
                    return
                }
            }
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

    open fun onTokenUnauth(any: T) {}

    open fun onRxSubscribe(d: Disposable) {}

    abstract fun onRxNext(any: T)

    abstract fun onRxError(e: Throwable, isNetwork: Boolean)

    open fun onRxComplete() {}

    /** onError执行完后会调用该方法 */
    open fun onErrorEnd() {}

    companion object {
        /** 创建lambda调用 */
        @JvmStatic
        @JvmOverloads
        fun <T> action(
            next: (any: T) -> Unit,
            error: (e: Throwable, isNetwork: Boolean) -> Unit = { _, _ -> },
            subscribe: (d: Disposable) -> Unit = {},
            errorEnd: () -> Unit = {},
            complete: () -> Unit = {},
            dispose: () -> Unit = {}
        ): RxObserver<T> = object : RxObserver<T>() {

            override fun onRxSubscribe(d: Disposable) {
                super.onRxSubscribe(d)
                subscribe(d)
            }

            override fun onRxNext(any: T) {
                next(any)
            }

            override fun onRxError(e: Throwable, isNetwork: Boolean) {
                error(e, isNetwork)
            }

            override fun onErrorEnd() {
                super.onErrorEnd()
                errorEnd()
            }

            override fun onRxComplete() {
                super.onRxComplete()
                complete()
            }

            override fun onDispose() {
                super.onDispose()
                dispose()
            }
        }
    }
}