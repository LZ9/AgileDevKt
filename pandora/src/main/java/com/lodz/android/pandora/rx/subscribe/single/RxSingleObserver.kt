package com.lodz.android.pandora.rx.subscribe.single

import com.lodz.android.pandora.rx.exception.DataException
import com.lodz.android.pandora.rx.exception.NetworkException
import com.lodz.android.pandora.rx.exception.RxExceptionFactory
import com.lodz.android.pandora.rx.status.ResponseStatus
import io.reactivex.rxjava3.disposables.Disposable

/**
 * 网络接口使用的订阅者，主要对接口进行判断处理
 * Created by zhouL on 2019/1/18.
 */
abstract class RxSingleObserver<T> : BaseSingleObserver<T>() {

    final override fun onBaseSubscribe(d: Disposable) {
        super.onBaseSubscribe(d)
        onRxSubscribe(d)
    }

    final override fun onBaseSuccess(any: T) {
        try {
            checkError(any)
            if (any is ResponseStatus){
                if (any.isTokenUnauth()){
                    onTokenUnauth(any)
                    return
                }
            }
            onRxSuccess(any)
        } catch (e: Exception) {
            onError(e)
        }
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

    open fun onTokenUnauth(any: T) {}

    open fun onRxSubscribe(d: Disposable) {}

    abstract fun onRxSuccess(any: T)

    abstract fun onRxError(e: Throwable, isNetwork: Boolean)

    /** onError执行完后会调用该方法 */
    open fun onErrorEnd() {}

    companion object {
        /** 创建lambda调用 */
        @JvmStatic
        @JvmOverloads
        fun <T> action(
            success: (any: T) -> Unit,
            error: (e: Throwable, isNetwork: Boolean) -> Unit = { _, _ -> },
            subscribe: (d: Disposable) -> Unit = {},
            errorEnd: () -> Unit = {},
            dispose: () -> Unit = {}
        ): RxSingleObserver<T> = object : RxSingleObserver<T>() {
            override fun onRxSubscribe(d: Disposable) {
                super.onRxSubscribe(d)
                subscribe(d)
            }

            override fun onRxSuccess(any: T) {
                success(any)
            }

            override fun onRxError(e: Throwable, isNetwork: Boolean) {
                error(e, isNetwork)
            }

            override fun onErrorEnd() {
                super.onErrorEnd()
                errorEnd()
            }

            override fun onDispose() {
                super.onDispose()
                dispose()
            }
        }
    }
}