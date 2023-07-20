package com.lodz.android.agiledevkt.bean

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.lodz.android.pandora.utils.jackson.deserializer.String2Boolean
import com.lodz.android.pandora.utils.jackson.serializer.PrefixFixSerializer

/**
 * 景点数据实体
 * @author zhouL
 * @date 2019/4/12
 */
class SpotBean {

    var name: String = ""

    var score: String = ""

//    @JsonProperty("isDriver")
    @JsonSerialize(using = PrefixFixSerializer::class)
    var isDriver = 0

    @JsonDeserialize(using = String2Boolean::class)
    @JsonSerialize(using = PrefixFixSerializer::class)
//    @JsonProperty("isRecommend")
    var isRecommend = true

}