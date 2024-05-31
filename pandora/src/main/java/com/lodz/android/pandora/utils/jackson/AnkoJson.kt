package com.lodz.android.pandora.utils.jackson

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.lodz.android.pandora.base.application.BaseApplication

/**
 * Json工具类
 * @author zhouL
 * @date 2022/6/13
 */

/** 把Json字符串转为对象 */
inline fun <reified T> String.parseJsonObject(mapper: ObjectMapper = BaseApplication.get()?.getJacksonObjectMapper() ?: ObjectMapper()): T? {
    if (this.isEmpty()) {
        return null
    }
    return mapper.readValue(this, object : TypeReference<T>() {})
}

/** 把对象转为Json字符串 */
fun <T> T.toJsonString(
    mapper: ObjectMapper = BaseApplication.get()?.getJacksonObjectMapper() ?: ObjectMapper()
): String = mapper.writeValueAsString(this)

/** 把对象转为Json字符串（去掉String转json后多出来的\和"） */
fun String.jsonFormat(): String = this.replace("\\", "").replace("\"{", "{").replace("}\"", "}")