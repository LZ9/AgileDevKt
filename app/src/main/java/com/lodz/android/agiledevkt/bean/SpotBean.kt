package com.lodz.android.agiledevkt.bean

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.lodz.android.agiledevkt.modules.api.json.String2Boolean

/**
 * 景点数据实体
 * @author zhouL
 * @date 2019/4/12
 */
class SpotBean {

    var name: String = ""

    var score: String = ""

    @JsonDeserialize(using = String2Boolean::class)
    var isRecommend = true

    override fun toString(): String {
        return "SpotBean(name='$name', score='$score', isRecommend='$isRecommend')"
    }
}