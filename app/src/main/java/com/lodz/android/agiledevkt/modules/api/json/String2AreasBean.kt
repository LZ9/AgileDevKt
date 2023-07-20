package com.lodz.android.agiledevkt.modules.api.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.lodz.android.agiledevkt.bean.city.AreasBean
import com.lodz.android.pandora.utils.jackson.deserializer.BaseJsonDeserializer
import com.lodz.android.pandora.utils.jackson.parseJsonObject

/**
 * 把Json的字符串转对象
 * @author zhouL
 * @date 2023/6/7
 */
class String2AreasBean : BaseJsonDeserializer<AreasBean?>() {
    override fun deserialize(json: String?, p: JsonParser?, ctxt: DeserializationContext?): AreasBean? = json?.parseJsonObject<AreasBean>()
}