package com.lodz.android.pandora.widget.rv.tree

/**
 * RV树结构节点
 * @author zhouL
 * @date 2022/7/15
 */
interface RvTreeItem {

    /** 数据实体类型 */
    fun getCls(): Class<*> = this::class.java

    /** 是否根节点 */
    fun isRootItem(): Boolean
}