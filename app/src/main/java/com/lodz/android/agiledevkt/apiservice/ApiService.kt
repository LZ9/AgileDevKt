package com.lodz.android.agiledevkt.apiservice

import com.lodz.android.agiledevkt.bean.SpotBean
import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * 接口
 * @author zhouL
 * @date 2019/3/22
 */
interface ApiService {

    /** 登录接口 */
    @FormUrlEncoded
    @POST("systemApi/login")
    fun login(@Field("account") account: String, @Field("password") password: String): Observable<ResponseBean<String>>

    /** post方式获取景点数据 */
    @FormUrlEncoded
    @POST("spot")
    fun postSpot(@Field("id") id: Int): Observable<ResponseBean<SpotBean>>

    /** get方式获取景点数据 */
    @GET("spot")
    fun getSpot(@Query("id") id: Int): Observable<ResponseBean<SpotBean>>

    /** 自定义方式获取景点数据 */
    @POST("spot")
    fun querySpot(@Body requestBody: RequestBody): Observable<ResponseBean<List<SpotBean>>>

    /** 获取测试数据 */
    @GET("spot")
    suspend fun getResult(@Field("isSuccess") isSuccess: Boolean): ResponseBean<String>

}