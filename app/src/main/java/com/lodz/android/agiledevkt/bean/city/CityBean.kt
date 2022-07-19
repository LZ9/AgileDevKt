package com.lodz.android.agiledevkt.bean.city

import com.lodz.android.pandora.widget.rv.tree.RvTreeGroup
import com.lodz.android.pandora.widget.rv.tree.RvTreeItem

/**
 * 市数据体
 * @author zhouL
 * @date 2022/7/15
 */
class CityBean : RvTreeGroup {

    /** 父节点ID */
    var parentId = ""

    /** 市ID */
    var cityId = ""

    /** 市名称 */
    var cityName = ""

    /** 城市列表 */
    var areas: List<AreasBean> = arrayListOf()

    var isExpand: Boolean = false

    var isCanExpand: Boolean = true

    override fun fetchTreeItems(): List<RvTreeItem> = areas

    override fun isExpandItem(): Boolean = isExpand

    override fun onExpandChanged(isExpand: Boolean) {
        this.isExpand = isExpand
    }

    override fun expandEnable(): Boolean = isCanExpand

    override fun isRootItem(): Boolean = false

    override fun fetchItemId(): String = cityId

    override fun fetchParentId(): String = parentId

}