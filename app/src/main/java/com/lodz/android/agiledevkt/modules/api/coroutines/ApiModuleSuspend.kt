package com.lodz.android.agiledevkt.modules.api.coroutines

import com.lodz.android.agiledevkt.apiservice.ApiCoroutinesService
import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import kotlinx.coroutines.*

/**
 * 数据
 * @author zhouL
 * @date 2019/12/3
 */
object ApiModuleSuspend :ApiCoroutinesService{


    override suspend fun getResult(isSuccess: Boolean): ResponseBean<String> =
        withContext(Dispatchers.IO) {
            delay(2000)
            val responseBean = ResponseBean.createSuccess<String>()
            responseBean.code = if (isSuccess) ResponseBean.SUCCESS else ResponseBean.FAIL
            responseBean.msg = if (isSuccess) "success" else "faile"
            responseBean.data = if (isSuccess) System.currentTimeMillis().toString() else ""
            responseBean
        }
    //    override suspend fun getResult(isSuccess: Boolean): ResponseBean<String> =
    //        withContext(Dispatchers.IO) {
    //            delay(2000)
    //            val responseBean = ResponseBean.createSuccess<String>()
    //            responseBean.code = if (isSuccess) ResponseBean.SUCCESS else ResponseBean.FAIL
    //            responseBean.msg = if (isSuccess) "success" else "faile"
    //            responseBean.data = if (isSuccess) System.currentTimeMillis().toString() else ""
    //            responseBean
    //        }

//    suspend fun requestResult(isSuccess: Boolean): String = withContext(Dispatchers.IO) {
//        getResultText(isSuccess).await()
//    }
//
//    fun getResultText(isSuccess: Boolean): Deferred<String> = GlobalScope.async {
//        delay(2000)
//        if (!isSuccess) {
//            throw DataException("request fail")
//        }
//        return@async "result is ${System.currentTimeMillis()}"
//    }
}