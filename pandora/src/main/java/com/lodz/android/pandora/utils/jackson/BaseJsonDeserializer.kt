package com.lodz.android.pandora.utils.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

/**
 * Json解析基类
 * @author zhouL
 * @date 2023/7/19
 */
 abstract class BaseJsonDeserializer<T> : JsonDeserializer<T>() {

    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): T {
        val json = p?.readValueAsTree<TreeNode>()?.toString()
        return deserialize(json, p, ctxt)
    }

    abstract fun deserialize(json: String?, p: JsonParser?, ctxt: DeserializationContext?): T

}