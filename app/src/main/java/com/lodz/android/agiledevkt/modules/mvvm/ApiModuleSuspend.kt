package com.lodz.android.agiledevkt.modules.mvvm

import com.lodz.android.pandora.rx.exception.DataException
import kotlinx.coroutines.*

/**
 * 数据
 * @author zhouL
 * @date 2019/12/3
 */
object ApiModuleSuspend {

    suspend fun requestResult(isSuccess: Boolean): String = withContext(Dispatchers.IO) {
        getResultText(isSuccess).await()
    }

    fun getResultText(isSuccess: Boolean): Deferred<String> = GlobalScope.async {
        delay(2000)
        if (!isSuccess) {
            throw DataException("request fail")
        }
        return@async "result is ${System.currentTimeMillis()}"
    }
}