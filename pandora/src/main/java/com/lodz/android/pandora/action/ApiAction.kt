package com.lodz.android.pandora.action

/**
 * 接口请求的DSL构造器
 * @author zhouL
 * @date 2022/2/11
 */
open class ApiAction<T> {

    var mStartAction: (() -> Unit)? = null

    var mSuccessAction: ((T) -> Unit)? = null

    var mTokenUnauthAction: ((T) -> Unit)? = null

    var mErrorAction: ((e: Throwable, isNetwork: Boolean) -> Unit)? = null

    var mCompleteAction: (() -> Unit)? = null

    /** 订阅开始 */
    fun onStart(action: () -> Unit){
        mStartAction = action
    }

    /** 订阅成功 */
    fun onSuccess(action: (T) -> Unit) {
        mSuccessAction = action
    }

    /** 鉴权未通过 */
    fun onTokenUnauth(action: (T) -> Unit) {
        mTokenUnauthAction = action
    }

    /** 订阅异常 */
    fun onError(action: (e: Throwable, isNetwork: Boolean) -> Unit) {
        mErrorAction = action
    }

    /** 订阅完成 */
    fun onComplete(action: () -> Unit){
        mCompleteAction = action
    }

}