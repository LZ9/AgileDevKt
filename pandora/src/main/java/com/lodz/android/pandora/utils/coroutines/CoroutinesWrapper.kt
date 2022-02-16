package com.lodz.android.pandora.utils.coroutines

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

/**
 * 协程请求包装类
 * @author zhouL
 * @date 2022/2/16
 */
class CoroutinesWrapper private constructor(private val mViewModel: ViewModel? = null) {

    companion object {
        /** 创建包装类对象，如果在ViewModel内创建，请传入[vm]对象 */
        @JvmOverloads
        fun create(vm: ViewModel? = null): CoroutinesWrapper {
            return CoroutinesWrapper(vm)
        }
    }

    /** 使用挂起函数请求 */
    fun <T> request(request: suspend () -> T): CoroutinesAction<T> {
        return CoroutinesAction(mViewModel, mRequestFun = request)
    }

    /** 使用延迟函数请求 */
    fun <T> request(request: Deferred<T>): CoroutinesAction<T> {
        return CoroutinesAction(mViewModel, mRequestDeferred = request)
    }
}