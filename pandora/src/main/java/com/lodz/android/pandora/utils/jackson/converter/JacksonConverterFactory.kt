/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lodz.android.pandora.utils.jackson.converter

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


/**
 * A {@linkplain Converter.Factory converter} which uses Jackson.
 *
 * <p>Because Jackson is so flexible in the types it supports, this converter assumes that it can
 * handle all types. If you are mixing JSON serialization with something else (such as protocol
 * buffers), you must {@linkplain Retrofit.Builder#addConverterFactory(Converter.Factory) add this
 * instance} last to allow the other converters a chance to see their types.
 */
class JacksonConverterFactory private constructor(private val mapper: ObjectMapper) : Converter.Factory() {

    companion object {
        /** Create an instance using a default {@link ObjectMapper} instance for conversion. */
        fun create(): JacksonConverterFactory = create(ObjectMapper())

        /** Create an instance using {@code mapper} for conversion. */
        fun create(mapper: ObjectMapper): JacksonConverterFactory {
            return JacksonConverterFactory(mapper)
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val javaType = mapper.typeFactory.constructType(type)
        val reader = mapper.readerFor(javaType)
        return JacksonResponseBodyConverter<Any>(reader)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        val javaType = mapper.typeFactory.constructType(type)
        val writer = mapper.writerFor(javaType)
        return JacksonRequestBodyConverter<Any>(writer)
    }
}
