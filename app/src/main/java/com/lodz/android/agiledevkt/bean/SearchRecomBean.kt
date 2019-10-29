package com.lodz.android.agiledevkt.bean

import com.lodz.android.pandora.widget.search.RecomdData

/**
 * 搜索推荐数据
 * @author zhouL
 * @date 2019/10/28
 */
class SearchRecomBean : RecomdData {
    var text: String = ""

    override fun getTitleText(): String = text
}