package com.lodz.android.agiledevkt.bean

import com.lodz.android.pandora.widget.search.RecomdData

/**
 * 搜索推荐数据
 * @author zhouL
 * @date 2019/10/28
 */
class SearchRecomBean : RecomdData {

    /** 关键字 */
    var text = ""
    /** 标签一 */
    var firstTag = ""
    /** 标签二 */
    var secondTag = ""
    /** 描述 */
    var desc = ""
    /** 提示语 */
    var tips = ""
    /** 关键字标签 */
    var titleTag = ""

    override fun getDescText(): String = desc

    override fun getTipsText(): String = tips

    override fun getTitleTagText(): String = titleTag

    override fun getTitleText(): String = text

    override fun getFirstTagText(): String = firstTag

    override fun getSecondTagText(): String = secondTag
}