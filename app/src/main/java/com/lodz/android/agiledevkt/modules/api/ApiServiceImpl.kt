package com.lodz.android.agiledevkt.modules.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lodz.android.agiledevkt.apiservice.ApiService
import com.lodz.android.agiledevkt.bean.SpotBean
import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import com.lodz.android.corekt.utils.ReflectUtils
import com.lodz.android.pandora.rx.utils.doComplete
import com.lodz.android.pandora.rx.utils.doError
import com.lodz.android.pandora.rx.utils.doNext
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import java.net.SocketTimeoutException

/**
 * 接口本地实现类
 * @author zhouL
 * @date 2019/4/12
 */
object ApiServiceImpl : ApiService {

    /** 网络失败 */
    const val NETWORK_FAIL = 1
    /** 接口失败 */
    const val API_FAIL = 2
    /** 接口失败 */
    const val API_SUCCESS = 3

    override fun login(account: String, password: String): Observable<ResponseBean<String>> =
        Observable.create { emitter ->
            try {
                Thread.sleep(2000)
                val responseBean = ResponseBean.createSuccess<String>()
                responseBean.msg = "success"
                responseBean.data = "{\"id\":\"1\",\"loginName\":\"admin\"}"
                emitter.doNext(responseBean)
                emitter.doComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.doError(e)
            }
        }

    override fun postSpot(id: Int): Observable<ResponseBean<SpotBean>> =
        Observable.create { emitter ->
            try {
                Thread.sleep(2000)
                if (id == NETWORK_FAIL) {
                    throw SocketTimeoutException()
                }
                if (id == API_FAIL) {
                    val responseBean = ResponseBean.createFail<SpotBean>()
                    responseBean.msg = "fail"
                    emitter.doNext(responseBean)
                    emitter.doComplete()
                    return@create
                }
                val responseBean = ResponseBean.createSuccess<SpotBean>()
                responseBean.msg = "success"
                val spotBean = SpotBean()
                spotBean.spotName = "环岛路"
                spotBean.score = "10分"
                responseBean.data = spotBean
                emitter.doNext(responseBean)
                emitter.doComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.doError(e)
            }
        }

    override fun getSpot(id: Int): Observable<ResponseBean<SpotBean>> =
        Observable.create { emitter ->
            try {
                Thread.sleep(2000)
                if (id == NETWORK_FAIL) {
                    throw SocketTimeoutException()
                }
                if (id == API_FAIL) {
                    val responseBean = ResponseBean.createFail<SpotBean>()
                    responseBean.msg = "fail"
                    emitter.doNext(responseBean)
                    emitter.doComplete()
                    return@create
                }
                val responseBean = ResponseBean.createSuccess<SpotBean>()
                responseBean.msg = "success"
                val spotBean = SpotBean()
                spotBean.spotName = "鼓浪屿"
                spotBean.score = "10分"
                responseBean.data = spotBean
                emitter.doNext(responseBean)
                emitter.doComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.doError(e)
            }
        }

    override fun querySpot(requestBody: RequestBody): Observable<ResponseBean<List<SpotBean>>> =
        Observable.create { emitter ->
            try {
                Thread.sleep(2000)
                val json = "{\"code\":200,\"msg\":\"success\",\"data\":[]}"
                val responseBean = Gson().fromJson<ResponseBean<List<SpotBean>>>(json, object : TypeToken<ResponseBean<List<SpotBean>>>(){}.type)
                val list = ArrayList<SpotBean>()
                val spotBean = SpotBean()
                spotBean.spotName = getJsonByRequestBody(requestBody)
                spotBean.score = "10分"
                list.add(spotBean)
                responseBean.data = list
                emitter.doNext(responseBean)
                emitter.doComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.doError(e)
            }
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
}