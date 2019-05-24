package com.lodz.android.pandora.widget.collect.radio

/**
 * 单选项包装类
 * @author zhouL
 * @date 2019/5/23
 */
internal class RadioableWrapper(val radioable: Radioable) : Radioable {

    override fun getIdTag(): String = radioable.getIdTag()

    override fun getNameText(): String = radioable.getNameText()

    internal var isSelected = false
}