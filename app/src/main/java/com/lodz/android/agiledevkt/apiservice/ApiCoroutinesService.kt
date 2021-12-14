package com.lodz.android.agiledevkt.apiservice

import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import retrofit2.http.*

/**
 * 协程接口
 * @author zhouL
 * @date 2019/3/22
 */
interface ApiCoroutinesService {

    /** 获取测试数据 */
    @GET("spot")
    suspend fun getResult(@Field("isSuccess") isSuccess: Boolean): ResponseBean<String>

}