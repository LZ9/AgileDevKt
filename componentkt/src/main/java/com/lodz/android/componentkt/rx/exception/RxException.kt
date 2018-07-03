package com.lodz.android.componentkt.rx.exception

/**
 * RX异常基类
 * Created by zhouL on 2018/7/3.
 */
open class RxException : Exception {
    /** 自定义异常信息 */
    private var mErrorMsg = ""

    constructor(errorMsg: String) : super() {
        mErrorMsg = errorMsg
    }

    constructor(message: String?, errorMsg: String) : super(message) {
        mErrorMsg = errorMsg
    }

    constructor(message: String?, cause: Throwable?, errorMsg: String) : super(message, cause) {
        mErrorMsg = errorMsg
    }

    constructor(cause: Throwable?, errorMsg: String) : super(cause) {
        mErrorMsg = errorMsg
    }

    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean, errorMsg: String)
            : super(message, cause, enableSuppression, writableStackTrace) {
        mErrorMsg = errorMsg
    }

    /** 获取自定义异常信息 */
    fun getErrorMsg() = mErrorMsg
}