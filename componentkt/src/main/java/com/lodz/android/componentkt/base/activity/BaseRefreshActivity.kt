package com.lodz.android.componentkt.base.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lodz.android.componentkt.R
import com.lodz.android.componentkt.widget.base.ErrorLayout
import com.lodz.android.componentkt.widget.base.LoadingLayout
import com.lodz.android.componentkt.widget.base.NoDataLayout
import com.lodz.android.componentkt.widget.base.TitleBarLayout
import com.lodz.android.corekt.anko.bindView

/**
 * 基类Activity（带基础状态控件和下拉刷新控件）
 * Created by zhouL on 2018/11/12.
 */
abstract class BaseRefreshActivity : AbsActivity() {

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

    override fun getAbsLayoutId(): Int = R.layout.componentkt_activity_base_refresh

    override fun afterSetContentView() {
        super.afterSetContentView()
        if (!isUseAnkoLayout()) {
            showStatusLoading()
            setContainerView()
            initSwipeRefreshLayout()
        }
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
        val view = LayoutInflater.from(this).inflate(getLayoutId(), null)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mContentLayout.addView(view, layoutParams)
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    override fun setListeners() {
        super.setListeners()
        if (!isUseAnkoLayout()) {
            getTitleBarLayout().setOnBackBtnClickListener {
                onClickBackBtn()
            }

            // 下拉刷新
            mSwipeRefreshLayout.setOnRefreshListener {
                onDataRefresh()
            }
        }
    }

    /** 下拉刷新 */
    protected abstract fun onDataRefresh()

    /** 设置下拉进度的切换颜色[colorResIds] */
    protected fun setSwipeRefreshColorScheme(@ColorRes vararg colorResIds: Int) {
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        mSwipeRefreshLayout.setColorSchemeResources(*colorResIds)
    }

    /** 设置下拉进度的背景颜色[colorResId] */
    protected fun setSwipeRefreshBackgroundColor(@ColorRes colorResId: Int) {
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(colorResId)
    }

    /** 设置刷新结束（隐藏刷新进度条） */
    protected fun setSwipeRefreshFinish() {
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        mSwipeRefreshLayout.isRefreshing = false
    }

    /** 设置刷新控件是否启用[enabled] */
    protected fun setSwipeRefreshEnabled(enabled: Boolean) {
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        mSwipeRefreshLayout.isEnabled = enabled
    }

    /** 点击标题栏的返回按钮 */
    protected open fun onClickBackBtn() {}

    /** 点击错误页面的重试按钮 */
    protected open fun onClickReload() {}

    /** 显示无数据页面 */
    protected open fun showStatusNoData() {
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        mSwipeRefreshLayout.visibility = View.VISIBLE
        mContentLayout.visibility = View.GONE
        getNoDataLayout().visibility = View.VISIBLE
        if (mLoadingLayout != null) {
            mLoadingLayout!!.visibility = View.GONE
        }
        if (mErrorLayout != null) {
            mErrorLayout!!.visibility = View.GONE
        }
    }

    /** 显示错误页面 */
    protected open fun showStatusError() {
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout do not use base widget")
        }
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
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout do not use base widget")
        }
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
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout do not use base widget")
        }
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
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        getTitleBarLayout().visibility = View.GONE
    }

    /** 显示TitleBar */
    protected fun showTitleBar() {
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        getTitleBarLayout().visibility = View.VISIBLE
    }

    /** 获取顶部标题栏控件 */
    protected fun getTitleBarLayout(): TitleBarLayout {
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        if (mTitleBarLayout == null) {
            mTitleBarLayout = mTitleBarViewStub.inflate() as TitleBarLayout
        }
        return mTitleBarLayout!!
    }

    /** 获取加载控件 */
    protected fun getLoadingLayout(): LoadingLayout {
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        if (mLoadingLayout == null) {
            mLoadingLayout = mLoadingViewStub.inflate() as LoadingLayout
            mLoadingLayout!!.visibility = View.GONE
        }
        return mLoadingLayout!!
    }

    /** 获取无数据控件 */
    protected fun getNoDataLayout(): NoDataLayout {
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        if (mNoDataLayout == null) {
            mNoDataLayout = mNoDataViewStub.inflate() as NoDataLayout
            mNoDataLayout!!.visibility = View.GONE
        }
        return mNoDataLayout!!
    }

    /** 获取加载失败界面 */
    protected fun getErrorLayout(): ErrorLayout {
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout do not use base widget")
        }
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