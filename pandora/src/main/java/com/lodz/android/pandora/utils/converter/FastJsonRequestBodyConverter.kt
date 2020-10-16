package com.lodz.android.pandora.utils.converter

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import com.alibaba.fastjson.serializer.SerializerFeature
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter


/**
 * @author zhouL
 * @date 2020/10/16
 */
class FastJsonRequestBodyConverter<T>(
    private var serializeConfig: SerializeConfig?,
    private var features: Array<SerializerFeature>?
) :Converter<T, RequestBody> {

    private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")

    override fun convert(value: T): RequestBody? {
        val s = features
        return if (serializeConfig != null) {
            if (s != null) {
                RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value, serializeConfig, *s))
            } else {
                RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value, serializeConfig))
            }
        } else {
            if (s != null) {
                RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value, *s))
            } else {
                RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value))
            }
        }
    }
}