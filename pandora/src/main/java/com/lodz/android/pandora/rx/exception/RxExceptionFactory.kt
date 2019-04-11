package com.lodz.android.pandora.rx.exception

import com.lodz.android.corekt.network.NetworkManager
import java.net.SocketException
import java.net.SocketTimeoutException

/**
 * RX异常工厂
 * Created by zhouL on 2018/7/3.
 */
object RxExceptionFactory {

    /** 根据异常[t]生成对应的RxException */
    @JvmStatic
    fun create(t: Throwable): RxException {
        if (t is RxException) {
            return t
        }

        if (!NetworkManager.get().isNetworkAvailable()) {
            return NetworkNoConnException(t.message, t.cause, "网络尚未连接，请检查您的网络状态")
        }

        if (t is SocketTimeoutException || t is SocketException) {
            return NetworkTimeOutException(t.message, t.cause, "网络连接超时，请稍后再试")
        }

        return RxException(t.message, t.cause, "数据接口异常，请稍后再试")
    }
}