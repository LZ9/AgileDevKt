package com.lodz.android.agiledevkt.bean.city

import com.lodz.android.pandora.widget.rv.tree.RvTreeGroup
import com.lodz.android.pandora.widget.rv.tree.RvTreeItem

/**
 * 省数据体
 * @author zhouL
 * @date 2022/7/15
 */
class ProvinceBean : RvTreeGroup {

    /** 省ID */
    var provinceId = ""
    /** 省名称 */
    var provinceName = ""
    /** 市列表 */
    var citys :List<CityBean> = arrayListOf()

    var isExpand: Boolean = false

    var isCanExpand: Boolean = true

    override fun getTreeItems(): List<RvTreeItem> = citys

    override fun isExpandItem(): Boolean = isExpand

    override fun onExpandChanged(isExpand: Boolean) {
        this.isExpand = isExpand
    }

    override fun expandEnable(): Boolean = isCanExpand


}