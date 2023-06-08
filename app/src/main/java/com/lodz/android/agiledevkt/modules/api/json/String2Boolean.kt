package com.lodz.android.agiledevkt.modules.api.json

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

/**
 * 把Json的字符串转布尔型
 * @author zhouL
 * @date 2023/6/7
 */
class String2Boolean : JsonDeserializer<Boolean>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Boolean {
        val str = p?.readValueAsTree<TreeNode>()?.toString()?.lowercase()?.replace("\"", "")
            ?: throw JsonParseException(p, "JsonParser cannot read value as tree")
        if (str == "true" || str == "1") {
            return true
        }
        if (str == "false" || str == "0"){
            return false
        }
        throw JsonParseException(p, "cannot parse to Boolean")
    }
}