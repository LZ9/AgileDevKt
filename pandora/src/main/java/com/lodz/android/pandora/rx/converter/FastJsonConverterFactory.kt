package com.lodz.android.pandora.rx.converter

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
 * FastJson转换器
 * Created by zhouL on 2018/7/4.
 */
class FastJsonConverterFactory private constructor() : Converter.Factory() {

    companion object {
        fun create(): FastJsonConverterFactory = FastJsonConverterFactory()
    }

    private var mParserConfig = ParserConfig.getGlobalInstance()
    private var featureValues = JSON.DEFAULT_PARSER_FEATURE

    private var features: Array<Feature?> = arrayOf()
    private var serializeConfig: SerializeConfig? = null
    private var serializerFeatures: Array<SerializerFeature> = arrayOf()

    override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {

        return FastJsonResponseBodyConverter<ResponseBody>(type, mParserConfig, featureValues, *features)
    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<out Annotation>?, methodAnnotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        return FastJsonRequestBodyConverter<ResponseBody>(serializeConfig, *serializerFeatures)
    }

    fun getParserConfig(): ParserConfig = mParserConfig

    fun setParserConfig(config: ParserConfig?): FastJsonConverterFactory {
        this.mParserConfig = config
        return this
    }

    fun getParserFeatureValues(): Int = featureValues

    fun setParserFeatureValues(featureValues: Int): FastJsonConverterFactory {
        this.featureValues = featureValues
        return this
    }

    fun getParserFeatures(): Array<Feature?> = features

    fun setParserFeatures(features: Array<Feature?>): FastJsonConverterFactory {
        this.features = features
        return this
    }

    fun getSerializeConfig(): SerializeConfig? = serializeConfig

    fun setSerializeConfig(serializeConfig: SerializeConfig): FastJsonConverterFactory {
        this.serializeConfig = serializeConfig
        return this
    }

    fun getSerializerFeatures(): Array<SerializerFeature> = serializerFeatures

    fun setSerializerFeatures(features: Array<SerializerFeature>): FastJsonConverterFactory {
        this.serializerFeatures = features
        return this
    }

}