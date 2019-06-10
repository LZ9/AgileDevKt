package com.lodz.android.pandora.rx.converter

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import com.alibaba.fastjson.serializer.SerializerFeature
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter

/**
 * Created by zhouL on 2018/7/4.
 */
class FastJsonRequestBodyConverter<T>(private val config: SerializeConfig?, vararg features: SerializerFeature?) : Converter<T, RequestBody> {

    private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")

    private var mFeatures: Array<out SerializerFeature?> = features

    override fun convert(value: T): RequestBody {
        return if (config != null) {
            if (mFeatures.isNotEmpty()) {
                RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value, config, *mFeatures))
            } else {
                RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value, config))
            }
        } else {
            if (mFeatures.isNotEmpty()) {
                RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value, *mFeatures))
            } else {
                RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value))
            }
        }
    }
}