package com.lodz.android.agiledevkt.bean

/**
 * 景点数据实体
 * @author zhouL
 * @date 2019/4/12
 */
class SpotBean {

    var name: String = ""

    var score: String = ""

    override fun toString(): String {
        return "SpotBean(name='$name', score='$score')"
    }
}