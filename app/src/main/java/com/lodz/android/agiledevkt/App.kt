package com.lodz.android.agiledevkt

import android.content.Context
import android.widget.LinearLayout
import androidx.multidex.MultiDex
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.network.NetworkManager
import com.lodz.android.corekt.threadpool.ThreadPoolManager
import com.lodz.android.pandora.base.application.BaseApplication
import io.reactivex.plugins.RxJavaPlugins

/**
 * Application
 * Created by zhouL on 2018/6/20.
 */
class App : BaseApplication() {

    companion object {
        @JvmStatic
        fun get(): App = BaseApplication.get() as App
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onStartCreate() {
        configBaseLayout()
        //捕获RxDownloader的异常报错问题
        RxJavaPlugins.setErrorHandler {
            PrintLog.e("testtag", "App", it)
        }
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
//        getBaseLayoutConfig().getTitleBarLayoutConfig().backBtnResId = R.drawable.pandora_ic_launcher
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
        getBaseLayoutConfig().getNoDataLayoutConfig().isNeedTips = true
//        getBaseLayoutConfig().getNoDataLayoutConfig().drawableResId = R.drawable.pandora_ic_launcher
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
        getBaseLayoutConfig().getLoadingLayoutConfig().useSysDefDrawable = false
//        getBaseLayoutConfig().getLoadingLayoutConfig().pbWidthPx = dp2px(70)
//        getBaseLayoutConfig().getLoadingLayoutConfig().pbHeightPx = dp2px(70)
    }

    private fun configErrorLayout() {
        getBaseLayoutConfig().getErrorLayoutConfig().orientation = LinearLayout.VERTICAL
        getBaseLayoutConfig().getErrorLayoutConfig().isNeedImg = true
        getBaseLayoutConfig().getErrorLayoutConfig().isNeedTips = true
//        getBaseLayoutConfig().getErrorLayoutConfig().drawableResId = R.drawable.pandora_ic_launcher
//        getBaseLayoutConfig().getErrorLayoutConfig().backgroundColor = R.color.color_ffa630
//        getBaseLayoutConfig().getErrorLayoutConfig().tips = "接口出错啦"
//        getBaseLayoutConfig().getErrorLayoutConfig().textColor = R.color.color_ea413c
//        getBaseLayoutConfig().getErrorLayoutConfig().textSize = 18
    }

    override fun onExit() {
        // todo 待完善
        ThreadPoolManager.get().releaseAll()
        NetworkManager.get().release(this)
        NetworkManager.get().clearNetworkListener()
    }

}