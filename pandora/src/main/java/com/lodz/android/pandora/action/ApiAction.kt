package com.lodz.android.pandora.action

/**
 * 接口请求的DSL构造器
 * @author zhouL
 * @date 2022/2/11
 */
open class ApiAction<T> {

    var mSuccessAction: ((T) -> Unit)? = null

    var mTokenUnauthAction: ((T) -> Unit)? = null

    var mErrorAction: ((e: Throwable, isNetwork: Boolean) -> Unit)? = null

    fun onSuccess(action: (T) -> Unit) {
        mSuccessAction = action
    }

    fun onTokenUnauth(action: (T) -> Unit) {
        mTokenUnauthAction = action
    }

    fun onError(action: (e: Throwable, isNetwork: Boolean) -> Unit) {
        mErrorAction = action
    }

}