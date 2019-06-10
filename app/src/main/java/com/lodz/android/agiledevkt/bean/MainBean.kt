package com.lodz.android.agiledevkt.bean

import com.lodz.android.corekt.array.Groupable

/**
 * 主页数据，标题名称[titleName]，索引文字[indexText]，跳转的类[cls]
 * Created by zhouL on 2018/11/23.
 */
class MainBean(private val titleName: String, private val indexText: String, private val cls: Class<*>) : Groupable {

    fun getTitleName(): String = titleName

    fun getCls(): Class<*> = cls

    fun getIndexText(): String = indexText

    override fun getSortStr(): String = indexText

}