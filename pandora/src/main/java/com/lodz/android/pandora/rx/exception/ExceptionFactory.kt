package com.lodz.android.pandora.rx.exception

import com.lodz.android.corekt.network.NetworkManager
import com.lodz.android.pandora.rx.status.ResponseStatus
import retrofit2.HttpException
import java.net.*
import javax.net.ssl.SSLException

/**
 * RX异常工厂
 * Created by zhouL on 2018/7/3.
 */
object ExceptionFactory {

    /** 创建一个数据异常 */
    @JvmStatic
    fun createDataException(res: ResponseStatus?, msg: String = "response fail"): DataException {
        val exception = DataException(msg)
        exception.setData(res)
        return exception
    }

    /** 根据异常[t]类型判断是否是网络异常 */
    @JvmStatic
    fun isNetworkError(t: Throwable): Boolean {
        if (!NetworkManager.get().isNetworkAvailable()) {
            return true
        }
        return t is SocketTimeoutException || t is SocketException || t is HttpException || t is UnknownHostException || t is HttpRetryException || t is SSLException
    }

}