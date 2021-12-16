package com.lodz.android.agiledevkt.apiservice

import com.lodz.android.agiledevkt.bean.SpotBean
import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * 协程接口
 * @author zhouL
 * @date 2019/3/22
 */
interface ApiCoroutinesService {

    /** 登录接口 */
    @FormUrlEncoded
    @POST("systemApi/login")
    suspend fun login(@Field("account") account: String, @Field("password") password: String): ResponseBean<String>

    /** post方式获取景点数据 */
    @FormUrlEncoded
    @POST("spot")
    suspend fun postSpot(@Field("id") id: Int): ResponseBean<SpotBean>

    /** get方式获取景点数据 */
    @GET("spot")
    suspend fun getSpot(@Query("id") id: Int): ResponseBean<SpotBean>

    /** 自定义方式获取景点数据 */
    @POST("spot")
    suspend fun querySpot(@Body requestBody: RequestBody): ResponseBean<List<SpotBean>>

    /** 获取测试数据 */
    @GET("spot")
    suspend fun getResult(@Field("isSuccess") isSuccess: Boolean): ResponseBean<String>

}