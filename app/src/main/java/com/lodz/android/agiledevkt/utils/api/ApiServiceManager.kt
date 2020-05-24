package com.lodz.android.agiledevkt.utils.api

import com.lodz.android.agiledevkt.config.UrlConfig
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.rx.converter.FastJsonConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.util.concurrent.TimeUnit

/**
 * 接口管理器
 * @author zhouL
 * @date 2019/3/22
 */
class ApiServiceManager private constructor() {

    companion object {
        /** 接口日志标签 */
        private const val TAG = "resultValue"

        private val sInstance = ApiServiceManager()
        @JvmStatic
        fun get(): ApiServiceManager = sInstance
    }

    private var mRetrofit: Retrofit


    init {
        mRetrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .baseUrl(UrlConfig.BASE_URL)
                .client(getOkHttpClient())
                .build()
    }

    /** 获取一个OkHttpClient */
    private fun getOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .addInterceptor(RequestInterceptor())
                    .build()

    /** 请求拦截器 */
    private class RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val oldRequest = chain.request()
            val request = oldRequest.newBuilder().build()
            logRequest(request)
            return logResponse(chain.proceed(request))
        }

        /** 获取请求字符串，[request]请求 */
        private fun logRequest(request: Request) {
            val list = request.url().pathSegments()
            PrintLog.iS(TAG, "[" + list[list.size - 1] + "] ----> " + request.url().toString())
            PrintLog.iS(TAG, "[" + list[list.size - 1] + "] <--- " + getRequestString(request))
        }

        /** 获取请求字符串，[request]请求 */
        private fun getRequestString(request: Request): String =
                Buffer().use { buffer ->
                    val copy = request.newBuilder().build()
                    val body = copy.body() ?: return ""
                    body.writeTo(buffer)
                    return buffer.readUtf8()
                }

        /** 打印返回数据日志，[response]返回数据 */
        private fun logResponse(response: okhttp3.Response): okhttp3.Response {
            val body = response.body() ?: return response
            var log = ""
            try {
                log = body.string()
                val list = response.request().url().pathSegments()
                PrintLog.dS(TAG, "[" + list[list.size - 1] + "] <--- " + log)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            // 打印完需要将数据重新写入，因为response.body().string()执行一次以后会将数据清空
            return response.newBuilder().body(ResponseBody.create(body.contentType(), log)).build()
        }
    }

    /** 创建对应的接口类[service]实体 */
    fun <T> create(service: Class<T>): T = mRetrofit.create(service)

    /** 得到Retrofit对象 */
    fun getRetrofit(): Retrofit = mRetrofit
}