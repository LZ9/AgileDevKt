package com.lodz.android.pandora.base.application.config

import android.widget.LinearLayout
import androidx.annotation.IntDef

/**
 * 基类配置
 * Created by zhouL on 2018/6/25.
 */
class BaseLayoutConfig {
    @IntDef(LinearLayout.HORIZONTAL, LinearLayout.VERTICAL)
    @Retention(AnnotationRetention.SOURCE)
    annotation class OrientationType

    /** 异常页面配置  */
    private var mErrorLayoutConfig: ErrorLayoutConfig = ErrorLayoutConfig()
    /** 加载页面配置 */
    private var mLoadingLayoutConfig: LoadingLayoutConfig = LoadingLayoutConfig()
    /** 无数据页面配置 */
    private var mNoDataLayoutConfig: NoDataLayoutConfig = NoDataLayoutConfig()
    /** 标题栏配置  */
    private var mTitleBarLayoutConfig: TitleBarLayoutConfig = TitleBarLayoutConfig()

    /** 获取异常页面配置 */
    fun getErrorLayoutConfig(): ErrorLayoutConfig = mErrorLayoutConfig

    /** 获取加载页面配置 */
    fun getLoadingLayoutConfig(): LoadingLayoutConfig = mLoadingLayoutConfig

    /** 获取无数据页面配置 */
    fun getNoDataLayoutConfig(): NoDataLayoutConfig = mNoDataLayoutConfig

    /** 获取标题栏配置 */
    fun getTitleBarLayoutConfig(): TitleBarLayoutConfig = mTitleBarLayoutConfig
}