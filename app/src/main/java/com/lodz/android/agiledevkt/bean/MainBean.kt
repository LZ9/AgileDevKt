package com.lodz.android.agiledevkt.bean

import com.lodz.android.corekt.array.Groupable

/**
 * 主页数据
 * Created by zhouL on 2018/11/23.
 */
class MainBean(title: String, indexText: String, cls: Class<*>) : Groupable {

    /** 标题名称  */
    private val titleName: String
    /** 跳转的类  */
    private val cls: Class<*>
    /** 索引文字  */
    private val indexText: String

    init {
        this.titleName = title
        this.indexText = indexText
        this.cls = cls
    }

    fun getTitleName(): String = titleName

    fun getCls(): Class<*> = cls

    fun getIndexText(): String = indexText

    override fun getSortStr(): String = indexText

}