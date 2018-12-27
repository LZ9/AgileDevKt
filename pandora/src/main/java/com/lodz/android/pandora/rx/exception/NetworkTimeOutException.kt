package com.lodz.android.pandora.rx.exception

/**
 * 网络超时异常
 * Created by zhouL on 2018/7/3.
 */
class NetworkTimeOutException : NetworkException {
    constructor(errorMsg: String) : super(errorMsg)
    constructor(message: String?, errorMsg: String) : super(message, errorMsg)
    constructor(message: String?, cause: Throwable?, errorMsg: String) : super(message, cause, errorMsg)
    constructor(cause: Throwable?, errorMsg: String) : super(cause, errorMsg)
    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean, errorMsg: String) : super(message, cause, enableSuppression, writableStackTrace, errorMsg)
}