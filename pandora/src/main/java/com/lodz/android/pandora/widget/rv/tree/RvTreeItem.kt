package com.lodz.android.pandora.widget.rv.tree

/**
 * RV树结构节点
 * @author zhouL
 * @date 2022/7/15
 */
interface RvTreeItem {

    /** 数据实体类型 */
    fun fetchCls(): Class<*> = this::class.java

    /** 是否根节点 */
    fun fetchItemId(): String

    /** 父节点ID */
    fun fetchParentId(): String

    /** 是否根节点 */
    fun isRootItem(): Boolean
}