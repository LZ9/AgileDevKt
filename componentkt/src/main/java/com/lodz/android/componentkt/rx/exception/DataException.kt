package com.lodz.android.componentkt.rx.exception

import com.lodz.android.componentkt.rx.status.ResponseStatus

/**
 * 服务端数据异常
 * Created by zhouL on 2018/7/3.
 */
class DataException : RxException {

    /** 数据信息 */
    private var mData: ResponseStatus? = null

    constructor(errorMsg: String) : super(errorMsg)
    constructor(message: String?, errorMsg: String) : super(message, errorMsg)
    constructor(message: String?, cause: Throwable?, errorMsg: String) : super(message, cause, errorMsg)
    constructor(cause: Throwable?, errorMsg: String) : super(cause, errorMsg)
    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean, errorMsg: String) : super(message, cause, enableSuppression, writableStackTrace, errorMsg)

    /** 获取数据 */
    fun getData() = mData

    /** 设置数据[data] */
    fun setData(data: ResponseStatus?) {
        mData = data
    }

}