package com.lodz.android.pandora.utils.converter

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.parser.Feature
import com.alibaba.fastjson.parser.ParserConfig
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

/**
 * @author zhouL
 * @date 2020/10/16
 */
class FastJsonResponseBodyConverter<T>(
    private var mType: Type?,
    private var config: ParserConfig?,
    private var featureValues: Int,
    private var features: Array<Feature>?
) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T? {
        try {
            val f = features ?: arrayOfNulls<Feature>(0)
            return JSON.parseObject(value.string(), mType, config, featureValues, *f)
        } finally {
            value.close()
        }
    }
}