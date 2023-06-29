package com.lodz.android.pandora.rx.exception

import android.os.Build
import androidx.annotation.RequiresApi
import com.lodz.android.pandora.rx.status.ResponseStatus

/**
 * 服务端数据异常
 * Created by zhouL on 2018/7/3.
 */
class DataException : Exception {

    /** 数据信息 */
    private var mData: ResponseStatus? = null

    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
    @RequiresApi(Build.VERSION_CODES.N)
    constructor(
        message: String?,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
    ) : super(message, cause, enableSuppression, writableStackTrace)


    /** 获取数据 */
    fun getData(): ResponseStatus? = mData

    /** 设置数据[data] */
    fun setData(data: ResponseStatus?): DataException {
        mData = data
        return this
    }

}