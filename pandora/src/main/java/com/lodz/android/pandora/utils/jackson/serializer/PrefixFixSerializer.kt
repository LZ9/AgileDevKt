package com.lodz.android.pandora.utils.jackson.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

/**
 * 前缀修复类（例如修复is前缀被去掉或者字段被忽略）
 * @author zhouL
 * @date 2023/7/20
 */
open class PrefixFixSerializer : JsonSerializer<Any?>() {
    override fun serialize(value: Any?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        value?.let { gen?.writeObject(it) }
    }
}