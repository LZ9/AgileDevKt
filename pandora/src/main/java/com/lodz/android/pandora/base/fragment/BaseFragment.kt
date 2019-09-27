package com.lodz.android.pandora.base.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.R
import com.lodz.android.pandora.widget.base.ErrorLayout
import com.lodz.android.pandora.widget.base.LoadingLayout
import com.lodz.android.pandora.widget.base.NoDataLayout
import com.lodz.android.pandora.widget.base.TitleBarLayout

/**
 * 基类Fragment（带基础状态控件）
 * Created by zhouL on 2018/11/19.
 */
abstract class BaseFragment : LazyFragment() {

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

    /** 内容布局  */
    private val mContentLayout by bindView<LinearLayout>(R.id.content_layout)

    final override fun getAnkoLayoutView(): View? = super.getAnkoLayoutView()

    final override fun getAbsLayoutId(): Int = R.layout.pandora_fragment_base

    final override fun beforeFindViews(view: View) {
        super.beforeFindViews(view)
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout , please extends LazyFragment and use @UseAnkoLayout annotation")
        }
        showStatusLoading()
        setContainerView()
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

    /** 点击标题栏的返回按钮 */
    protected open fun onClickBackBtn() {}

    /** 点击错误页面的重试按钮 */
    protected open fun onClickReload() {}

    /** 显示无数据页面 */
    protected open fun showStatusNoData() {
        mContentLayout.visibility = View.GONE
        mLoadingLayout?.visibility = View.GONE
        mErrorLayout?.visibility = View.GONE
        getNoDataLayout().visibility = View.VISIBLE
    }

    /** 显示错误页面 */
    @JvmOverloads
    protected open fun showStatusError(t: Throwable? = null) {
        mContentLayout.visibility = View.GONE
        mLoadingLayout?.visibility = View.GONE
        getErrorLayout().visibility = View.VISIBLE
        getErrorLayout().showAuto(t)
        mNoDataLayout?.visibility = View.GONE
    }

    /** 显示加载页面 */
    protected open fun showStatusLoading() {
        mContentLayout.visibility = View.GONE
        getLoadingLayout().visibility = View.VISIBLE
        mErrorLayout?.visibility = View.GONE
        mNoDataLayout?.visibility = View.GONE
    }

    /** 显示内容页面 */
    protected open fun showStatusCompleted() {
        mContentLayout.visibility = View.VISIBLE
        mLoadingLayout?.visibility = View.GONE
        mErrorLayout?.visibility = View.GONE
        mNoDataLayout?.visibility = View.GONE
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
            mTitleBarLayout?.visibility = View.GONE
            mTitleBarLayout?.setOnBackBtnClickListener {
                onClickBackBtn()
            }
        }
        return mTitleBarLayout!!
    }

    /** 获取加载控件 */
    protected fun getLoadingLayout(): LoadingLayout {
        if (mLoadingLayout == null) {
            mLoadingLayout = mLoadingViewStub.inflate() as LoadingLayout
            mLoadingLayout?.visibility = View.GONE
        }
        return mLoadingLayout!!
    }

    /** 获取无数据控件 */
    protected fun getNoDataLayout(): NoDataLayout {
        if (mNoDataLayout == null) {
            mNoDataLayout = mNoDataViewStub.inflate() as NoDataLayout
            mNoDataLayout?.visibility = View.GONE
        }
        return mNoDataLayout!!
    }

    /** 获取加载失败界面 */
    protected fun getErrorLayout(): ErrorLayout {
        if (mErrorLayout == null) {
            mErrorLayout = mErrorViewStub.inflate() as ErrorLayout
            mErrorLayout?.visibility = View.GONE
            mErrorLayout?.setReloadListener {
                onClickReload()
            }
        }
        return mErrorLayout!!
    }
}