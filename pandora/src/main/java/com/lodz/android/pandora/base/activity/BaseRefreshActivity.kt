package com.lodz.android.pandora.base.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.R
import com.lodz.android.pandora.widget.base.ErrorLayout
import com.lodz.android.pandora.widget.base.LoadingLayout
import com.lodz.android.pandora.widget.base.NoDataLayout
import com.lodz.android.pandora.widget.base.TitleBarLayout

/**
 * 基类Activity（带基础状态控件和下拉刷新控件）
 * Created by zhouL on 2018/11/12.
 */
abstract class BaseRefreshActivity : AbsActivity() {

    /** 标题栏 */
    private val mPdrTitleBarViewStub by bindView<ViewStub>(R.id.pdr_view_stub_title_bar_layout)
    /** 加载页 */
    private val mPdrLoadingViewStub by bindView<ViewStub>(R.id.pdr_view_stub_loading_layout)
    /** 无数据页 */
    private val mPdrNoDataViewStub by bindView<ViewStub>(R.id.pdr_view_stub_no_data_layout)
    /** 失败页 */
    private val mPdrErrorViewStub by bindView<ViewStub>(R.id.pdr_view_stub_error_layout)
    /** 根目录布局 */
    private val mPdrRootLayout by bindView<ViewGroup>(R.id.pdr_base_root_layout)

    /** 顶部标题布局 */
    private var mPdrTitleBarLayout: TitleBarLayout? = null
    /** 加载布局 */
    private var mPdrLoadingLayout: LoadingLayout? = null
    /** 无数据布局 */
    private var mPdrNoDataLayout: NoDataLayout? = null
    /** 错误布局 */
    private var mPdrErrorLayout: ErrorLayout? = null

    /** 下拉刷新 */
    private val mPdrSwipeRefreshLayout by bindView<SwipeRefreshLayout>(R.id.pdr_swipe_refresh_layout)
    /** 内容布局 */
    private val mPdrContentLayout by bindView<LinearLayout>(R.id.pdr_content_layout)

    final override fun getAbsLayoutId(): Int = R.layout.pandora_activity_base_refresh

    final override fun getAbsViewBindingLayout(): View? = super.getAbsViewBindingLayout()

    final override fun afterSetContentView() {
        super.afterSetContentView()
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout , please extends AbsActivity and use @UseAnkoLayout annotation")
        }
        showStatusLoading()
        setContainerView()
        initSwipeRefreshLayout()
    }

    /** 初始化下拉刷新配置 */
    private fun initSwipeRefreshLayout() {
        // 设置下拉进度的切换颜色
        setSwipeRefreshColorScheme(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)
        // 设置下拉进度的背景颜色
        setSwipeRefreshBackgroundColor(android.R.color.white)
    }

    /** 把内容布局设置进来 */
    private fun setContainerView() {
        val layoutId = getLayoutId()
        val view = if (layoutId != 0) {
            LayoutInflater.from(this).inflate(layoutId, null)
        } else {
            getViewBindingLayout()
        }
        if (view == null) {
            showStatusNoData()
            return
        }
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mPdrContentLayout.addView(view, layoutParams)
    }

    @LayoutRes
    protected open fun getLayoutId(): Int = 0

    protected open fun getViewBindingLayout(): View? = null

    override fun setListeners() {
        super.setListeners()
        getTitleBarLayout().setOnBackBtnClickListener {
            onClickBackBtn()
        }

        // 下拉刷新
        mPdrSwipeRefreshLayout.setOnRefreshListener {
            onDataRefresh()
        }
    }

    /** 下拉刷新 */
    protected abstract fun onDataRefresh()

    /** 设置下拉进度的切换颜色[colorResIds] */
    protected fun setSwipeRefreshColorScheme(@ColorRes vararg colorResIds: Int) {
        mPdrSwipeRefreshLayout.setColorSchemeResources(*colorResIds)
    }

    /** 设置下拉进度的背景颜色[colorResId] */
    protected fun setSwipeRefreshBackgroundColor(@ColorRes colorResId: Int) {
        mPdrSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(colorResId)
    }

    /** 设置刷新结束（隐藏刷新进度条） */
    protected open fun setSwipeRefreshFinish() {
        setSwipeRefreshStatus(false)
    }

    /** 设置刷新状态[isRefreshing] */
    protected open fun setSwipeRefreshStatus(isRefreshing: Boolean) {
        mPdrSwipeRefreshLayout.isRefreshing = isRefreshing
    }

    /** 设置刷新控件是否启用[enabled] */
    protected open  fun setSwipeRefreshEnabled(enabled: Boolean) {
        mPdrSwipeRefreshLayout.isEnabled = enabled
    }

    /** 点击标题栏的返回按钮 */
    protected open fun onClickBackBtn() {}

    /** 点击错误页面的重试按钮 */
    protected open fun onClickReload() {}

    /** 显示无数据页面 */
    protected open fun showStatusNoData() {
        mPdrSwipeRefreshLayout.visibility = View.VISIBLE
        mPdrContentLayout.visibility = View.GONE
        getNoDataLayout().visibility = View.VISIBLE
        mPdrLoadingLayout?.visibility = View.GONE
        mPdrErrorLayout?.visibility = View.GONE
    }

    /** 显示错误页面 */
    protected open fun showStatusError() {
        showStatusError(null)
    }

    /** 显示错误页面 */
    protected open fun showStatusError(t: Throwable?) {
        mPdrSwipeRefreshLayout.visibility = View.GONE
        mPdrContentLayout.visibility = View.GONE
        mPdrLoadingLayout?.visibility = View.GONE
        mPdrNoDataLayout?.visibility = View.GONE
        getErrorLayout().visibility = View.VISIBLE
        getErrorLayout().showAuto(t)
    }

    /** 显示加载页面 */
    protected open fun showStatusLoading() {
        mPdrSwipeRefreshLayout.visibility = View.GONE
        mPdrContentLayout.visibility = View.GONE
        mPdrErrorLayout?.visibility = View.GONE
        getLoadingLayout().visibility = View.VISIBLE
        mPdrNoDataLayout?.visibility = View.GONE
    }

    /** 显示内容页面 */
    protected open fun showStatusCompleted() {
        mPdrSwipeRefreshLayout.visibility = View.VISIBLE
        mPdrContentLayout.visibility = View.VISIBLE
        mPdrLoadingLayout?.visibility = View.GONE
        mPdrErrorLayout?.visibility = View.GONE
        mPdrNoDataLayout?.visibility = View.GONE
    }

    /** 隐藏TitleBar */
    protected open fun goneTitleBar() {
        getTitleBarLayout().visibility = View.GONE
    }

    /** 显示TitleBar */
    protected open fun showTitleBar() {
        getTitleBarLayout().visibility = View.VISIBLE
    }

    /** 获取顶部标题栏控件 */
    protected fun getTitleBarLayout(): TitleBarLayout {
        if (mPdrTitleBarLayout == null) {
            mPdrTitleBarLayout = mPdrTitleBarViewStub.inflate() as TitleBarLayout
        }
        return mPdrTitleBarLayout!!
    }

    /** 获取加载控件 */
    protected fun getLoadingLayout(): LoadingLayout {
        if (mPdrLoadingLayout == null) {
            mPdrLoadingLayout = mPdrLoadingViewStub.inflate() as LoadingLayout
            mPdrLoadingLayout?.visibility = View.GONE
        }
        return mPdrLoadingLayout!!
    }

    /** 获取无数据控件 */
    protected fun getNoDataLayout(): NoDataLayout {
        if (mPdrNoDataLayout == null) {
            mPdrNoDataLayout = mPdrNoDataViewStub.inflate() as NoDataLayout
            mPdrNoDataLayout?.visibility = View.GONE
        }
        return mPdrNoDataLayout!!
    }

    /** 获取加载失败界面 */
    protected fun getErrorLayout(): ErrorLayout {
        if (mPdrErrorLayout == null) {
            mPdrErrorLayout = mPdrErrorViewStub.inflate() as ErrorLayout
            mPdrErrorLayout?.visibility = View.GONE
            mPdrErrorLayout?.setReloadListener {
                onClickReload()
            }
        }
        return mPdrErrorLayout!!
    }

    /** 获取下拉刷新控件 */
    protected fun getSwipeRefreshLayout(): SwipeRefreshLayout = mPdrSwipeRefreshLayout

    /** 是否加载中 */
    protected fun isLoading(): Boolean = getLoadingLayout().visibility == View.VISIBLE

    /** 是否加载完成 */
    protected fun isCompleted(): Boolean = mPdrContentLayout.visibility == View.VISIBLE

    /** 是否加载异常 */
    protected fun isError(): Boolean = getErrorLayout().visibility == View.VISIBLE

    /** 是否无数据 */
    protected fun isNoData(): Boolean = getNoDataLayout().visibility == View.VISIBLE

    /** 获取根目录布局 */
    protected fun getRootView(): ViewGroup = mPdrRootLayout
}