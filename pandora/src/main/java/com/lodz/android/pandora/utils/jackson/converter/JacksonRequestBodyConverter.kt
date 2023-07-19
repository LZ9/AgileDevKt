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

import com.fasterxml.jackson.databind.ObjectWriter
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter

class JacksonRequestBodyConverter<T>(private val adapter: ObjectWriter?) : Converter<T, RequestBody> {

    override fun convert(value: T): RequestBody? {
        val bytes = adapter?.writeValueAsBytes(value) ?: return null
        return RequestBody.create(MediaType.get("application/json; charset=UTF-8"), bytes)
    }
}