package com.lodz.android.componentkt.base.application.config

import android.support.annotation.IntDef
import android.widget.LinearLayout

/**
 * 基类配置
 * Created by zhouL on 2018/6/25.
 */
class BaseLayoutConfig {
    @IntDef(LinearLayout.HORIZONTAL, LinearLayout.VERTICAL)
    @Retention(AnnotationRetention.SOURCE)
    annotation class OrientationType

    /** 异常页面配置  */
    private var mErrorLayoutConfig: ErrorLayoutConfig
    /** 加载页面配置 */
    private var mLoadingLayoutConfig: LoadingLayoutConfig
    /** 无数据页面配置 */
    private var mNoDataLayoutConfig: NoDataLayoutConfig
    /** 标题栏配置  */
    private var mTitleBarLayoutConfig: TitleBarLayoutConfig

    init {
        mErrorLayoutConfig = ErrorLayoutConfig()
        mLoadingLayoutConfig = LoadingLayoutConfig()
        mNoDataLayoutConfig = NoDataLayoutConfig()
        mTitleBarLayoutConfig = TitleBarLayoutConfig()
    }

    /** 获取异常页面配置 */
    fun getErrorLayoutConfig() = mErrorLayoutConfig

    /** 获取加载页面配置 */
    fun getLoadingLayoutConfig() = mLoadingLayoutConfig

    /** 获取无数据页面配置 */
    fun getNoDataLayoutConfig() = mNoDataLayoutConfig

    /** 获取标题栏配置 */
    fun getTitleBarLayoutConfig() = mTitleBarLayoutConfig
}