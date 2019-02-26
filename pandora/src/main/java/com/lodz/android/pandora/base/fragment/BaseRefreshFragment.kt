package com.lodz.android.pandora.base.fragment

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
 * 基类Fragment（带基础状态控件和下来刷新控件）
 * Created by zhouL on 2018/11/19.
 */
abstract class BaseRefreshFragment : LazyFragment() {

    /** 标题栏 */
    private val mTitleBarViewStub by bindView<ViewStub>(R.id.view_stub_title_bar_layout)
    /** 加载页 */
    private val mLoadingViewStub by bindView<ViewStub>(R.id.view_stub_loading_layout)
    /** 无数据页 */
    private val mNoDataViewStub by bindView<ViewStub>(R.id.view_stub_no_data_layout)
    /** 失败页 */
    private val mErrorViewStub by bindView<ViewStub>(R.id.view_stub_error_layout)

    /** 顶部标题布局  */
    private var mTitleBarLayout: TitleBarLayout? = null
    /** 加载布局  */
    private var mLoadingLayout: LoadingLayout? = null
    /** 无数据布局  */
    private var mNoDataLayout: NoDataLayout? = null
    /** 错误布局  */
    private var mErrorLayout: ErrorLayout? = null

    /** 下拉刷新  */
    private val mSwipeRefreshLayout by bindView<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
    /** 内容布局  */
    private val mContentLayout by bindView<LinearLayout>(R.id.content_layout)

    final override fun getAnkoLayoutView(): View? = super.getAnkoLayoutView()

    final override fun getAbsLayoutId(): Int = R.layout.pandora_fragment_base_refresh

    final override fun beforeFindViews(view: View) {
        super.beforeFindViews(view)
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout , please extends LazyFragment and use @UseAnkoLayout annotation")
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
        if (getLayoutId() == 0) {
            showStatusNoData()
            return
        }
        val view = LayoutInflater.from(context).inflate(getLayoutId(), null)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mContentLayout.addView(view, layoutParams)
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    override fun setListeners(view: View) {
        super.setListeners(view)
        // 下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener {
            onDataRefresh()
        }
    }

    /** 下拉刷新 */
    protected abstract fun onDataRefresh()

    /** 点击标题栏的返回按钮 */
    protected open fun onClickBackBtn() {}

    /** 点击错误页面的重试按钮 */
    protected open fun onClickReload() {}

    /** 设置下拉进度的切换颜色[colorResIds] */
    protected fun setSwipeRefreshColorScheme(@ColorRes vararg colorResIds: Int) {
        mSwipeRefreshLayout.setColorSchemeResources(*colorResIds)
    }

    /** 设置下拉进度的背景颜色[colorResId] */
    protected fun setSwipeRefreshBackgroundColor(@ColorRes colorResId: Int) {
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(colorResId)
    }

    /** 设置刷新结束（隐藏刷新进度条） */
    protected fun setSwipeRefreshFinish() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    /** 设置刷新控件是否启用[enabled] */
    protected fun setSwipeRefreshEnabled(enabled: Boolean) {
        mSwipeRefreshLayout.isEnabled = enabled
    }

    /** 显示无数据页面 */
    protected open fun showStatusNoData() {
        mSwipeRefreshLayout.visibility = View.VISIBLE
        getNoDataLayout().visibility = View.VISIBLE
        mContentLayout.visibility = View.GONE
        if (mLoadingLayout != null) {
            mLoadingLayout!!.visibility = View.GONE
        }
        if (mErrorLayout != null) {
            mErrorLayout!!.visibility = View.GONE
        }
    }

    /** 显示错误页面 */
    protected open fun showStatusError() {
        mSwipeRefreshLayout.visibility = View.GONE
        mContentLayout.visibility = View.GONE
        if (mLoadingLayout != null) {
            mLoadingLayout!!.visibility = View.GONE
        }
        if (mNoDataLayout != null) {
            mNoDataLayout!!.visibility = View.GONE
        }
        getErrorLayout().visibility = View.VISIBLE
    }

    /** 显示加载页面 */
    protected open fun showStatusLoading() {
        mSwipeRefreshLayout.visibility = View.GONE
        mContentLayout.visibility = View.GONE
        if (mErrorLayout != null) {
            mErrorLayout!!.visibility = View.GONE
        }
        getLoadingLayout().visibility = View.VISIBLE
        if (mNoDataLayout != null) {
            mNoDataLayout!!.visibility = View.GONE
        }
    }

    /** 显示内容页面 */
    protected open fun showStatusCompleted() {
        mSwipeRefreshLayout.visibility = View.VISIBLE
        mContentLayout.visibility = View.VISIBLE
        if (mLoadingLayout != null) {
            mLoadingLayout!!.visibility = View.GONE
        }
        if (mErrorLayout != null) {
            mErrorLayout!!.visibility = View.GONE
        }
        if (mNoDataLayout != null) {
            mNoDataLayout!!.visibility = View.GONE
        }
    }

    /** 隐藏TitleBar */
    protected fun goneTitleBar() {
        getTitleBarLayout().visibility = View.GONE
    }

    /** 显示TitleBar */
    protected fun showTitleBar() {
        getTitleBarLayout().visibility = View.VISIBLE
    }

    /** 获取顶部标题栏控件 */
    protected fun getTitleBarLayout(): TitleBarLayout {
        if (mTitleBarLayout == null) {
            mTitleBarLayout = mTitleBarViewStub.inflate() as TitleBarLayout
            mTitleBarLayout!!.visibility = View.GONE
            mTitleBarLayout!!.setOnBackBtnClickListener {
                onClickBackBtn()
            }
        }
        return mTitleBarLayout!!
    }

    /** 获取加载控件 */
    protected fun getLoadingLayout(): LoadingLayout {
        if (mLoadingLayout == null) {
            mLoadingLayout = mLoadingViewStub.inflate() as LoadingLayout
            mLoadingLayout!!.visibility = View.GONE
        }
        return mLoadingLayout!!
    }

    /** 获取无数据控件 */
    protected fun getNoDataLayout(): NoDataLayout {
        if (mNoDataLayout == null) {
            mNoDataLayout = mNoDataViewStub.inflate() as NoDataLayout
            mNoDataLayout!!.visibility = View.GONE
        }
        return mNoDataLayout!!
    }

    /** 获取加载失败界面 */
    protected fun getErrorLayout(): ErrorLayout {
        if (mErrorLayout == null) {
            mErrorLayout = mErrorViewStub.inflate() as ErrorLayout
            mErrorLayout!!.visibility = View.GONE
            mErrorLayout!!.setReloadListener {
                onClickReload()
            }
        }
        return mErrorLayout!!
    }

}