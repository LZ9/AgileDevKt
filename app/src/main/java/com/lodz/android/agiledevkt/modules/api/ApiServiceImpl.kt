package com.lodz.android.agiledevkt.modules.api

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.lodz.android.agiledevkt.apiservice.ApiService
import com.lodz.android.agiledevkt.bean.SpotBean
import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import com.lodz.android.corekt.utils.ReflectUtils
import com.lodz.android.corekt.utils.UiHandler
import com.lodz.android.pandora.rx.utils.RxObservableOnSubscribe
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
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
            Observable.create(object : RxObservableOnSubscribe<ResponseBean<String>>() {
                override fun subscribe(emitter: ObservableEmitter<ResponseBean<String>>) {
                    UiHandler.postDelayed(2000){
                        val responseBean = ResponseBean.createSuccess<String>()
                        responseBean.msg = "success"
                        responseBean.data = "{\"id\":\"1\",\"loginName\":\"admin\"}"
                        doNext(emitter, responseBean)
                        doComplete(emitter)
                    }
                }
            })

    override fun postSpot(id: Int): Observable<ResponseBean<SpotBean>> =
            Observable.create(object : RxObservableOnSubscribe<ResponseBean<SpotBean>>(id) {
                override fun subscribe(emitter: ObservableEmitter<ResponseBean<SpotBean>>) {
                    UiHandler.postDelayed(2000){
                        try {
                            if (id == NETWORK_FAIL) {
                                throw SocketTimeoutException()
                            }
                            if (id == API_FAIL) {
                                val responseBean = ResponseBean.createFail<SpotBean>()
                                responseBean.msg = "fail"
                                doNext(emitter, responseBean)
                                doComplete(emitter)
                                return@postDelayed
                            }
                            val responseBean = ResponseBean.createSuccess<SpotBean>()
                            responseBean.msg = "success"
                            val spotBean = SpotBean()
                            spotBean.spotName = "环岛路"
                            spotBean.score = "10分"
                            responseBean.data = spotBean
                            doNext(emitter, responseBean)
                            doComplete(emitter)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            doError(emitter, e)
                        }
                    }
                }
            })

    override fun getSpot(id: Int): Observable<ResponseBean<SpotBean>> =
            Observable.create(object : RxObservableOnSubscribe<ResponseBean<SpotBean>>(id) {
                override fun subscribe(emitter: ObservableEmitter<ResponseBean<SpotBean>>) {
                    UiHandler.postDelayed(2000){
                        try {
                            if (id == NETWORK_FAIL) {
                                throw SocketTimeoutException()
                            }
                            if (id == API_FAIL) {
                                val responseBean = ResponseBean.createFail<SpotBean>()
                                responseBean.msg = "fail"
                                doNext(emitter, responseBean)
                                doComplete(emitter)
                                return@postDelayed
                            }
                            val responseBean = ResponseBean.createSuccess<SpotBean>()
                            responseBean.msg = "success"
                            val spotBean = SpotBean()
                            spotBean.spotName = "鼓浪屿"
                            spotBean.score = "10分"
                            responseBean.data = spotBean
                            doNext(emitter, responseBean)
                            doComplete(emitter)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            doError(emitter, e)
                        }
                    }
                }
            })

    override fun querySpot(requestBody: RequestBody): Observable<ResponseBean<List<SpotBean>>> =
            Observable.create(object : RxObservableOnSubscribe<ResponseBean<List<SpotBean>>>(requestBody) {
                override fun subscribe(emitter: ObservableEmitter<ResponseBean<List<SpotBean>>>) {
                    UiHandler.postDelayed(2000){
                        try {
                            val json = "{\"code\":200,\"msg\":\"success\",\"data\":[]}"
                            val responseBean = JSON.parseObject(json, object : TypeReference<ResponseBean<List<SpotBean>>>() {})
                            val list = ArrayList<SpotBean>()
                            val spotBean = SpotBean()
                            spotBean.spotName = getJsonByRequestBody(requestBody)
                            spotBean.score = "10分"
                            list.add(spotBean)
                            responseBean.data = list
                            doNext(emitter, responseBean)
                            doComplete(emitter)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            doError(emitter, e)
                        }
                    }
                }
            })

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