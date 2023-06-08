package com.lodz.android.agiledevkt.modules.api.coroutines

import com.lodz.android.agiledevkt.apiservice.ApiCoroutinesService
import com.lodz.android.agiledevkt.bean.MockBean
import com.lodz.android.agiledevkt.bean.SpotBean
import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import com.lodz.android.corekt.utils.ReflectUtils
import com.lodz.android.pandora.utils.jackson.parseJsonObject
import kotlinx.coroutines.*
import okhttp3.RequestBody
import java.net.SocketTimeoutException

/**
 * 数据
 * @author zhouL
 * @date 2019/12/3
 */
object ApiModuleSuspend :ApiCoroutinesService{

    /** 网络失败 */
    const val NETWORK_FAIL = 1
    /** 接口失败 */
    const val API_FAIL = 2
    /** 接口成功 */
    const val API_SUCCESS = 3

    override suspend fun login(account: String, password: String): ResponseBean<MockBean<SpotBean>> {
        delay(2000)
        val responseBean = ResponseBean.createSuccess<MockBean<SpotBean>>()
        responseBean.msg = "success"
        responseBean.data = "{\"id\":\"1\",\"loginName\":\"admin\"}".parseJsonObject()
        return responseBean
    }

    override suspend fun postSpot(id: Int): ResponseBean<SpotBean> {
        delay(2000)
        if (id == NETWORK_FAIL) {
            throw SocketTimeoutException()
        }
        if (id == API_FAIL) {
            val responseBean = ResponseBean.createFail<SpotBean>()
            responseBean.msg = "fail"
            return responseBean
        }
        val responseBean = ResponseBean.createSuccess<SpotBean>()
        responseBean.msg = "success"
        val spotBean = SpotBean()
        spotBean.name = "环岛路"
        spotBean.score = "10分"
        responseBean.data = spotBean
        return responseBean
    }

    override suspend fun getSpot(id: Int): ResponseBean<SpotBean> {
        delay(2000)
        if (id == NETWORK_FAIL) {
            throw SocketTimeoutException()
        }
        if (id == API_FAIL) {
            val responseBean = ResponseBean.createFail<SpotBean>()
            responseBean.msg = "fail"
            return responseBean
        }
        val responseBean = ResponseBean.createSuccess<SpotBean>()
        responseBean.msg = "success"
        val spotBean = SpotBean()
        spotBean.name = "鼓浪屿"
        spotBean.score = "10分"
        responseBean.data = spotBean
        return responseBean
    }

    override suspend fun querySpot(requestBody: RequestBody): ResponseBean<List<SpotBean>> {
        delay(2000)
        val json = "{\"code\":200,\"msg\":\"success\",\"data\":[]}"
        val responseBean = json.parseJsonObject<ResponseBean<List<SpotBean>>>() ?: ResponseBean.createFail("parse fail")
        val list = ArrayList<SpotBean>()
        val spotBean = SpotBean()
        spotBean.name = getJsonByRequestBody(requestBody)
        spotBean.score = "10分"
        list.add(spotBean)
        responseBean.data = list
        return responseBean
    }

    /** 从RequestBody中获取请求参数 */
    private fun getJsonByRequestBody(requestBody: RequestBody): String {
        try {
            val c = requestBody.javaClass
            val list = ReflectUtils.getFieldName(c)
            for (name in list) {
                val any = ReflectUtils.getFieldValue(c, requestBody, name)
                if (any is ByteArray) {
                    return String(any)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    override suspend fun getResult(isSuccess: Boolean): ResponseBean<String> {
        delay(2000)
        val responseBean = ResponseBean.createSuccess<String>()
        responseBean.code = if (isSuccess) ResponseBean.SUCCESS else ResponseBean.FAIL
        responseBean.msg = if (isSuccess) "success" else "faile"
        responseBean.data = if (isSuccess) System.currentTimeMillis().toString() else ""
        return responseBean
    }
}