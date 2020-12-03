package com.lodz.android.pandora.base.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.R
import com.lodz.android.pandora.widget.base.ErrorLayout
import com.lodz.android.pandora.widget.base.LoadingLayout
import com.lodz.android.pandora.widget.base.NoDataLayout

/**
 * 基类Fragment（带基础状态控件、中部刷新控件和顶部/底部扩展）
 * Created by zhouL on 2018/11/19.
 */
abstract class BaseSandwichFragment : LazyFragment() {

    /** 加载页 */
    private val mPdrLoadingViewStub by bindView<ViewStub>(R.id.pdr_view_stub_loading_layout)
    /** 无数据页 */
    private val mPdrNoDataViewStub by bindView<ViewStub>(R.id.pdr_view_stub_no_data_layout)
    /** 失败页 */
    private val mPdrErrorViewStub by bindView<ViewStub>(R.id.pdr_view_stub_error_layout)

    /** 加载布局 */
    private var mPdrLoadingLayout: LoadingLayout? = null
    /** 无数据布局 */
    private var mPdrNoDataLayout: NoDataLayout? = null
    /** 错误布局 */
    private var mPdrErrorLayout: ErrorLayout? = null

    /** 顶部布局 */
    private val mPdrTopLayout by bindView<FrameLayout>(R.id.pdr_top_layout)
    /** 内容布局 */
    private val mPdrContentLayout by bindView<LinearLayout>(R.id.pdr_content_layout)
    /** 下拉刷新 */
    private val mPdrSwipeRefreshLayout by bindView<SwipeRefreshLayout>(R.id.pdr_swipe_refresh_layout)
    /** 底部布局 */
    private val mPdrBottomLayout by bindView<FrameLayout>(R.id.pdr_bottom_layout)
    /** 根目录布局 */
    private val mPdrRootLayout by bindView<ViewGroup>(R.id.pdr_base_root_layout)

    final override fun getAnkoLayoutView(): View? = super.getAnkoLayoutView()

    final override fun getAbsLayoutId(): Int = R.layout.pandora_fragment_base_sandwich

    final override fun beforeFindViews(view: View) {
        super.beforeFindViews(view)
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout , please extends LazyFragment and use @UseAnkoLayout annotation")
        }
        showStatusLoading()
        setTopView()
        setBottomView()
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
        setSwipeRefreshEnabled(false)
    }

    /** 把顶部布局设置进来 */
    private fun setTopView() {
        if (getTopLayoutId() == 0) {
            mPdrTopLayout.visibility = View.GONE
            return
        }
        val view = LayoutInflater.from(context).inflate(getTopLayoutId(), null)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mPdrTopLayout.addView(view, layoutParams)
        mPdrTopLayout.visibility = View.VISIBLE
    }

    @LayoutRes
    protected open fun getTopLayoutId(): Int = 0

    /** 把底部布局设置进来 */
    private fun setBottomView() {
        if (getBottomLayoutId() == 0) {
            mPdrBottomLayout.visibility = View.GONE
            return
        }
        val view = LayoutInflater.from(context).inflate(getBottomLayoutId(), null)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mPdrBottomLayout.addView(view, layoutParams)
        mPdrBottomLayout.visibility = View.VISIBLE
    }

    @LayoutRes
    protected open fun getBottomLayoutId(): Int = 0

    /** 把内容布局设置进来 */
    private fun setContainerView() {
        if (getLayoutId() == 0) {
            showStatusNoData()
            return
        }
        val view = LayoutInflater.from(context).inflate(getLayoutId(), null)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mPdrContentLayout.addView(view, layoutParams)
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    override fun setListeners(view: View) {
        super.setListeners(view)
        // 下拉刷新
        mPdrSwipeRefreshLayout.setOnRefreshListener {
            onDataRefresh()
        }
    }

    /** 下拉刷新 */
    protected open fun onDataRefresh() {}

    /** 点击错误页面的重试按钮 */
    protected open fun onClickReload() {}

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
        mPdrSwipeRefreshLayout.isRefreshing = false
    }

    /** 设置刷新控件是否启用[enabled] */
    protected open fun setSwipeRefreshEnabled(enabled: Boolean) {
        mPdrSwipeRefreshLayout.isEnabled = enabled
    }

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

    /** 获取顶部布局 */
    protected fun getTopView(): ViewGroup = mPdrTopLayout

    /** 获取底部布局 */
    protected fun getBottomView(): ViewGroup = mPdrBottomLayout

    /** 获取根目录布局 */
    protected fun getRootView(): ViewGroup = mPdrRootLayout

}