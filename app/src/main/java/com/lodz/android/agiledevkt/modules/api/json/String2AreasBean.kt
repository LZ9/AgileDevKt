package com.lodz.android.agiledevkt.modules.api.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.lodz.android.agiledevkt.bean.city.AreasBean
import com.lodz.android.pandora.utils.jackson.parseJsonObject

/**
 * 把Json的字符串转对象
 * @author zhouL
 * @date 2023/6/7
 */
class String2AreasBean : JsonDeserializer<AreasBean>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): AreasBean? =
        try {
            p?.readValueAsTree<TreeNode>()?.toString()?.parseJsonObject<AreasBean>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
}