package com.lodz.android.agiledevkt.bean.city

import com.lodz.android.pandora.widget.rv.tree.RvTreeItem

/**
 * 县数据体
 * @author zhouL
 * @date 2022/7/15
 */
class AreasBean : RvTreeItem {

    /** 父节点ID */
    var parentId = ""

    /** 县ID */
    var areaId = ""

    /** 县名称 */
    var areaName = ""

    override fun fetchItemId(): String = areaId

    override fun fetchParentId(): String = parentId

    override fun isRootItem(): Boolean = false
}