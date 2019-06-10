package com.lodz.android.pandora.rx.converter

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.parser.Feature
import com.alibaba.fastjson.parser.ParserConfig
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

/**
 * Created by zhouL on 2018/7/4.
 */
class FastJsonResponseBodyConverter<T>(
    private val type: Type?,
    private val config: ParserConfig?,
    private val featureValues: Int,
    vararg features: Feature?
) : Converter<ResponseBody, T> {

    private var mFeatures: Array<out Feature?> = features

    override fun convert(value: ResponseBody?): T {
        value.use { rb ->
            return JSON.parseObject(rb?.string() ?: "{}", type, config, featureValues, *mFeatures)
        }
    }
}