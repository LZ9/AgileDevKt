package com.lodz.android.pandora.widget.base.compose.base

/**
 * 基础框架内容组件状态
 * @author zhouL
 * @date 2026/5/19
 */
sealed class BaseContentState {

    /** 是否显示加载页 */
    data object Loading : BaseContentState()

    /** 是否显示异常页面 */
    data object Error : BaseContentState()

    /** 是否显示无数据页面 */
    data object NoData : BaseContentState()

    /** 是否显示内容页 */
    data object Content : BaseContentState()

}