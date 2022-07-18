package com.lodz.android.pandora.widget.rv.tree

/**
 * RV树结构节点
 * @author zhouL
 * @date 2022/7/15
 */
interface RvTreeItem {

    fun getCls(): Class<*> = this::class.java

}