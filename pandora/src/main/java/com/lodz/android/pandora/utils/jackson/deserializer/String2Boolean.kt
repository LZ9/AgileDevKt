package com.lodz.android.pandora.utils.jackson.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext

/**
 * 把Json的字符串转布尔型
 * @author zhouL
 * @date 2023/6/7
 */
open class String2Boolean() : BaseJsonDeserializer<Boolean>() {
    override fun deserialize(json: String?, p: JsonParser?, ctxt: DeserializationContext?): Boolean {
        val str = json?.lowercase()?.replace("\"", "") ?: return defValue()
        if (str == "true" || str == "1") {
            return true
        }
        if (str == "false" || str == "0") {
            return false
        }
        return defValue()
    }

    open fun defValue(): Boolean = false
}