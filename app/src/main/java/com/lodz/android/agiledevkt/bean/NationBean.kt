package com.lodz.android.agiledevkt.bean

import com.lodz.android.corekt.array.Groupable
import java.util.*

/**
 * 国家数据类
 * Created by zhouL on 2018/12/3.
 */
class NationBean : Groupable {
    /** 国旗图片 */
    var imgUrl = ""
    /** 国籍代码 */
    var code = ""
    /** 名称 */
    var name = ""
    /** 拼音首字母缩写 */
    var pinYin = ""

    override fun getSortStr(): String = code.toUpperCase(Locale.getDefault())

    /** 获取标题 */
    fun getTitle(): String = "$code-$name"
}