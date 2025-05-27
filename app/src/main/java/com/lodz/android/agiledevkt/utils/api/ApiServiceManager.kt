package com.lodz.android.agiledevkt.utils.api

import com.lodz.android.agiledevkt.App
import com.lodz.android.agiledevkt.config.UrlConfig
import com.lodz.android.corekt.log.PrintLog
import okhttp3.Interceptor
import okhttp3.Headers
import okhttp3.Headers.Companion.toHeaders
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
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
            .addConverterFactory(JacksonConverterFactory.create(App.get().getJacksonObjectMapper()))
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
            val request = oldRequest.newBuilder()
                .headers(getHeaders(oldRequest.headers).toHeaders())//注入头信息
                .url(getCommonUrl(oldRequest))// 注入通用入参
                .build()
            logRequest(request)
            return logResponse(chain.proceed(request))
        }

        /** 在原始头信息[headers]中注入新的头信息 */
        private fun getHeaders(headers: Headers?): Map<String, String> {
            val map = HashMap<String, String>()
            map["appKey"] = "00001"
            map["format"] = "json"
            map["locale"] = "zh_CN"
            if (headers == null) {
                return map
            }
            val oldHeadersMap = headers.toMultimap()
            for (entry in oldHeadersMap) {
                if (entry.value.isNotEmpty()) {
                    map[entry.key] = entry.value[0]
                }
            }
            return map
        }

        /** 在原始请求[request]中以get的方式拼接公共参数 */
        private fun getCommonUrl(request: Request): HttpUrl =
            request.url
                .newBuilder()
                .scheme(request.url.scheme)
                .host(request.url.host)
                .addQueryParameter("appKey", "00001")
                .addQueryParameter("format", "json")
                .addQueryParameter("locale", "zh_CN")
                .build()


        /** 获取请求字符串，[request]请求 */
        private fun logRequest(request: Request) {
            val list = request.url.pathSegments
            PrintLog.iS(TAG, "[" + getApiName(list) + "] ----> " + request.url.toString())
            PrintLog.iS(TAG, "[" + getApiName(list) + "] <--- " + getRequestString(request))
        }

        /** 获取请求字符串，[request]请求 */
        private fun getRequestString(request: Request): String =
            Buffer().use { buffer ->
                val copy = request.newBuilder().build()
                val body = copy.body ?: return ""
                body.writeTo(buffer)
                return buffer.readUtf8()
            }

        /** 打印返回数据日志，[response]返回数据 */
        private fun logResponse(response: okhttp3.Response): okhttp3.Response {
            val body = response.body ?: return response
            var log = ""
            try {
                log = body.string()
                val list = response.request.url.pathSegments
                PrintLog.dS(TAG, "[" + getApiName(list) + "] <--- " + log)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            // 打印完需要将数据重新写入，因为response.body().string()执行一次以后会将数据清空
            return response.newBuilder().body(ResponseBody.create(body.contentType(), log)).build()
        }

        private fun getApiName(list: List<String>): String {
            var api = ""
            for (name in list) {
                api = "$api$name/"
            }
            return api
        }
    }

    /** 创建对应的接口类[service]实体 */
    fun <T> create(service: Class<T>): T = mRetrofit.create(service)

    /** 得到Retrofit对象 */
    fun getRetrofit(): Retrofit = mRetrofit
}