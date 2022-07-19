package com.lodz.android.pandora.widget.rv.tree

/**
 * RV树结构组
 * @author zhouL
 * @date 2022/7/15
 */
interface RvTreeGroup : RvTreeItem {

    /** 子节点列表 */
    fun fetchTreeItems(): List<RvTreeItem>

    /** 当前是否展开子节点 */
    fun isExpandItem(): Boolean

    /** 当前是否展开子节点 */
    fun onExpandChanged(isExpand: Boolean)

    /** 是否允许展开 */
    fun expandEnable(): Boolean
}