package com.lodz.android.pandora.utils.coroutines

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.lodz.android.corekt.anko.IoScope
import kotlinx.coroutines.*

/**
 * 协程请求包装类
 * @author zhouL
 * @date 2022/2/16
 */
class CoroutinesWrapper private constructor(private val mScope: CoroutineScope) {

    companion object {
        /** 创建包装类对象 */
        @JvmOverloads
        fun create(any: Any? = null): CoroutinesWrapper {
            val scope = when (any) {
                is ViewModel -> any.viewModelScope
                is AppCompatActivity -> any.lifecycleScope
                is Fragment -> any.lifecycleScope
                is CoroutineScope -> any
                else -> IoScope()
            }
            return CoroutinesWrapper(scope)
        }
    }

    /** 使用挂起函数请求 */
    fun <T> request(request: suspend () -> T): CoroutinesAction<T> {
        return CoroutinesAction(mScope, mRequestFun = request)
    }

    /** 使用延迟参数请求 */
    fun <T> request(request: Deferred<T>): CoroutinesAction<T> {
        return CoroutinesAction(mScope, mRequestDeferred = request)
    }
}