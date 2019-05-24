package com.lodz.android.agiledevkt.bean

import com.lodz.android.pandora.widget.collect.radio.Radioable

/**
 * 人员类型数据
 * @author zhouL
 * @date 2019/5/24
 */
class PersonTypeBean : Radioable {

    var id = ""

    var name = ""

    override fun getIdTag(): String = id

    override fun getNameText(): String = name
}