package com.lodz.android.agiledevkt

import android.widget.LinearLayout
import com.lodz.android.componentkt.base.application.BaseApplication
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.network.NetworkManager
import com.lodz.android.corekt.threadpool.ThreadPoolManager
import com.lodz.android.corekt.utils.UiHandler

/**
 * Application
 * Created by zhouL on 2018/6/20.
 */
class App : BaseApplication() {

    companion object {
        fun get(): App = BaseApplication.get() as App
    }

    override fun onStartCreate() {
        // todo 待完善
        PrintLog.setPrint(BuildConfig.LOG_DEBUG)// 配置日志开关
        NetworkManager.get().init(this)
        configBaseLayout()
    }

    /** 配置基类 */
    private fun configBaseLayout() {
        configTitleBarLayout()
        configErrorLayout()
        configLoadingLayout()
        configNoDataLayout()
    }

    /** 配置标题栏 */
    private fun configTitleBarLayout() {
        getBaseLayoutConfig().getTitleBarLayoutConfig().isNeedBackBtn = true
//        getBaseLayoutConfig().getTitleBarLayoutConfig().backBtnResId = R.drawable.componentkt_ic_launcher
        getBaseLayoutConfig().getTitleBarLayoutConfig().backgroundColor = R.color.color_00a0e9
//        getBaseLayoutConfig().getTitleBarLayoutConfig().backBtnText = "返回"
//        getBaseLayoutConfig().getTitleBarLayoutConfig().backBtnTextColor = R.color.color_d9d9d9
//        getBaseLayoutConfig().getTitleBarLayoutConfig().backBtnTextSize = 14
        getBaseLayoutConfig().getTitleBarLayoutConfig().titleTextColor = R.color.white
//        getBaseLayoutConfig().getTitleBarLayoutConfig().titleTextSize = 18
//        getBaseLayoutConfig().getTitleBarLayoutConfig().isShowDivideLine = true
//        getBaseLayoutConfig().getTitleBarLayoutConfig().divideLineHeightDp = 10
//        getBaseLayoutConfig().getTitleBarLayoutConfig().divideLineColor = R.color.color_2f6dc9
//        getBaseLayoutConfig().getTitleBarLayoutConfig().isNeedElevation = false
//        getBaseLayoutConfig().getTitleBarLayoutConfig().elevationVale = 23f
    }


    /** 配置无数据 */
    private fun configNoDataLayout() {
        getBaseLayoutConfig().getNoDataLayoutConfig().orientation = LinearLayout.VERTICAL
        getBaseLayoutConfig().getNoDataLayoutConfig().isNeedImg = true
        getBaseLayoutConfig().getNoDataLayoutConfig().isNeedTips = false
//        getBaseLayoutConfig().getNoDataLayoutConfig().drawableResId = R.drawable.componentkt_ic_launcher
//        getBaseLayoutConfig().getNoDataLayoutConfig().tips = "没数据飞走了"
//        getBaseLayoutConfig().getNoDataLayoutConfig().textColor = R.color.color_ffa630
//        getBaseLayoutConfig().getNoDataLayoutConfig().textSize = 23
//        getBaseLayoutConfig().getNoDataLayoutConfig().backgroundColor = R.color.color_ea8380

    }

    /** 配置加载页 */
    private fun configLoadingLayout() {
        getBaseLayoutConfig().getLoadingLayoutConfig().orientation = LinearLayout.VERTICAL
        getBaseLayoutConfig().getLoadingLayoutConfig().isNeedTips = true
//        getBaseLayoutConfig().getLoadingLayoutConfig().tips = "正在获取数据"
//        getBaseLayoutConfig().getLoadingLayoutConfig().textColor = R.color.white
//        getBaseLayoutConfig().getLoadingLayoutConfig().textSize = 23
//        getBaseLayoutConfig().getLoadingLayoutConfig().backgroundColor = R.color.color_ff4081
        getBaseLayoutConfig().getLoadingLayoutConfig().isIndeterminate = true
//        getBaseLayoutConfig().getLoadingLayoutConfig().indeterminateDrawable = R.drawable.anims_custom_progress
//        getBaseLayoutConfig().getLoadingLayoutConfig().pbWidthPx = dp2px(70).toInt()
//        getBaseLayoutConfig().getLoadingLayoutConfig().pbHeightPx = dp2px(70).toInt()
    }

    private fun configErrorLayout() {
        getBaseLayoutConfig().getErrorLayoutConfig().orientation = LinearLayout.VERTICAL
        getBaseLayoutConfig().getErrorLayoutConfig().isNeedImg = true
        getBaseLayoutConfig().getErrorLayoutConfig().isNeedTips = false
//        getBaseLayoutConfig().getErrorLayoutConfig().drawableResId = R.drawable.componentkt_ic_launcher
//        getBaseLayoutConfig().getErrorLayoutConfig().backgroundColor = R.color.color_ffa630
//        getBaseLayoutConfig().getErrorLayoutConfig().tips = "接口出错啦"
//        getBaseLayoutConfig().getErrorLayoutConfig().textColor = R.color.color_ea413c
//        getBaseLayoutConfig().getErrorLayoutConfig().textSize = 18
    }


    override fun onExit() {
        // todo 待完善
        UiHandler.destroy()
        ThreadPoolManager.get().releaseAll()
        NetworkManager.get().release(this)
        NetworkManager.get().clearNetworkListener()
    }

}