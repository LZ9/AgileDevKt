package com.lodz.android.agiledevkt.bean.base.request

import com.lodz.android.pandora.utils.jackson.toJsonString
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * 基础请求类
 * @author zhouL
 * @date 2019/4/12
 */
object BaseRequestBean {

    /** 创建一个[data]请求对象 */
    fun <T> createRequestBody(data: T): RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data.toJsonString())

    /** 创建一个请求对象（无需请求体数据） */
    fun createRequestBody(): RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{}")

}