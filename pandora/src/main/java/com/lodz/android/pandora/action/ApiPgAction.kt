package com.lodz.android.pandora.action

/**
 * 接口加载框请求的DSL构造器
 * @author zhouL
 * @date 2022/2/11
 */
open class ApiPgAction<T> : ApiAction<T>() {

    var mPgCancelAction: (() -> Unit)? = null

    fun onPgCancel(action: () -> Unit) {
        mPgCancelAction = action
    }

}