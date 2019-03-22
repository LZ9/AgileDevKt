package com.lodz.android.agiledevkt.apiservice

import com.lodz.android.agiledevkt.bean.base.ResponseBean
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 接口
 * @author zhouL
 * @date 2019/3/22
 */
interface ApiService {
    /**
     * 登录接口
     * @param requestBody 请求体
     * @return 订阅器
     */
    @FormUrlEncoded
    @POST("systemApi/login")
    fun login(@Field("account") account: String, @Field("password") password: String): Observable<ResponseBean<String>>

}