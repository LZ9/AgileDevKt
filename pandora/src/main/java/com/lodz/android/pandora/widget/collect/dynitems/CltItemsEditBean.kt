package com.lodz.android.pandora.widget.collect.dynitems

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView.ScaleType
import com.lodz.android.pandora.widget.collect.CltEditView.Companion.TYPE_TEXT
import com.lodz.android.pandora.widget.collect.CltEditView.EditInputType

/**
 * 动态条目输入框数据对象
 * @author zhouL
 * @date 2025/11/25
 */
class CltItemsEditBean {
    /** 编号 */
    var id = ""

    /** 是否只读 */
    var readOnly = false

    /** 条目高度 */
    var itemHeight: Int = 0
    /** 条目标题文本 */
    var itemTitleText = ""
    /** 条目标题文本颜色 */
    var itemTitleTextColor: ColorStateList? = null
    /** 条目标题文本字体大小 */
    var itemTitleTextSizeSp: Float = 0f
    /** 条目标题控件宽度 */
    var itemTitleWidthPx: Int = 0
    /** 条目标题背景图 */
    var itemTitleBackgroundDrawable: Drawable? = null

    /** 条目输入框标记 */
    var itemEtTag = ""
    /** 条目输入框文本 */
    var itemEtText = ""
    /** 条目输入框文本颜色 */
    var itemEtTextColor: ColorStateList? = null
    /** 条目输入框文本字体大小 */
    var itemEtTextSizeSp: Float = 0f
    /** 条目输入框提示文本 */
    var itemEtHintText = ""
    /** 条目输入框提示文本颜色 */
    var itemEtHintTextColor: ColorStateList? = null
    /** 条目输入框背景图 */
    var itemEtBackgroundDrawable: Drawable? = null
    /** 条目输入框文字位置 */
    var itemEtGravity: Int = 0
    /** 条目输入框最后一条记录是否启用请求焦点 */
    var itemEtLastFocusEnabled = false
    /** 条目输入框是否单行显示 */
    var itemEtSingleLine: Boolean = false
    /** 条目输入框最大输入字数 */
    var itemEtMaxCount = 0
    /** 条目输入框最小行数 */
    var itemEtMinLines = 0
    /** 条目输入框最大行数 */
    var itemEtMaxLines = 0
    /** 条目输入框输入限制提醒可见性 */
    var itemEtLimitVisibility = View.GONE
    /** 条目输入类型 */
    @EditInputType
    var itemEtInputType = TYPE_TEXT

    /** 是否需要条目单位 */
    var needItemUnit = false
    /** 条目单位文本 */
    var itemUnitText = ""
    /** 条目单位文本颜色 */
    var itemUnitTextColor: ColorStateList? = null
    /** 条目单位文本字体大小 */
    var itemUnitTextSizeSp: Float = 0f

    /** 条目删除图标 */
    var itemDeleteBtnDrawable: Drawable? = null
    /** 条目删除图标宽高边长 */
    var itemDeleteBtnSidePx: Int = 0
    /** 条目删除图标的ScaleType */
    var itemDeleteBtnScaleType: ScaleType? = null
    /** 条目删除图标可见性 */
    var itemDeleteBtnVisibility = View.GONE

    /** 条目编辑图标 */
    var itemEditBtnDrawable: Drawable? = null
    /** 条目编辑图标宽高边长 */
    var itemEditBtnSidePx: Int = 0
    /** 条目编辑图标的ScaleType */
    var itemEditBtnScaleType: ScaleType? = null
    /** 条目编辑图标可见性 */
    var itemEditBtnVisibility = View.GONE
}