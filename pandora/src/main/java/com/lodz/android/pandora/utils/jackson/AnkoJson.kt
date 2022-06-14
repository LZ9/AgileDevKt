package com.lodz.android.pandora.utils.jackson

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * Json工具类
 * @author zhouL
 * @date 2022/6/13
 */

/** 把Json字符串转为对象 */
inline fun <reified T> String.parseObject(mapper: ObjectMapper = ObjectMapper()): T = mapper.readValue(this, object : TypeReference<T>() {})

/** 把对象转为Json字符串 */
fun <T> T.toJsonString(mapper: ObjectMapper = ObjectMapper()): String = mapper.writeValueAsString(this)