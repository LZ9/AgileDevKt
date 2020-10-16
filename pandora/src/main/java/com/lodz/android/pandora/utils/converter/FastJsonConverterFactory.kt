package com.lodz.android.pandora.utils.converter

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.parser.Feature
import com.alibaba.fastjson.parser.ParserConfig
import com.alibaba.fastjson.serializer.SerializeConfig
import com.alibaba.fastjson.serializer.SerializerFeature
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


/**
 * @author zhouL
 * @date 2020/10/16
 */
class FastJsonConverterFactory private constructor() : Converter.Factory() {

    private var mParserConfig = ParserConfig.getGlobalInstance()
    private var featureValues = JSON.DEFAULT_PARSER_FEATURE
    private var features: Array<Feature>? = null

    private var serializeConfig: SerializeConfig? = null
    private var serializerFeatures: Array<SerializerFeature>? = null

    companion object {
        fun create(): FastJsonConverterFactory = FastJsonConverterFactory()
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? =
        FastJsonResponseBodyConverter<Any>(type, mParserConfig, featureValues, features)

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? =
        FastJsonRequestBodyConverter<Any>(serializeConfig, serializerFeatures)

    fun getParserConfig(): ParserConfig = mParserConfig


    fun setParserConfig(config: ParserConfig?): FastJsonConverterFactory {
        mParserConfig = config
        return this
    }

    fun getParserFeatureValues(): Int = featureValues

    fun setParserFeatureValues(featureValues: Int): FastJsonConverterFactory {
        this.featureValues = featureValues
        return this
    }

    fun getParserFeatures(): Array<Feature>? = features

    fun setParserFeatures(features: Array<Feature>?): FastJsonConverterFactory {
        this.features = features
        return this
    }

    fun getSerializeConfig(): SerializeConfig? = serializeConfig

    fun setSerializeConfig(serializeConfig: SerializeConfig?): FastJsonConverterFactory {
        this.serializeConfig = serializeConfig
        return this
    }

    fun getSerializerFeatures(): Array<SerializerFeature>? = serializerFeatures

    fun setSerializerFeatures(features: Array<SerializerFeature>?): FastJsonConverterFactory? {
        serializerFeatures = features
        return this
    }

}