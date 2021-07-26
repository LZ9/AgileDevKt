package com.lodz.android.pandora.base.activity

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
 * 基类Activity（带基础状态控件）
 * Created by zhouL on 2018/6/27.
 */
abstract class BaseActivity : AbsActivity() {

    /** 标题栏 */
    private val mPdrTitleBarViewStub by bindView<ViewStub>(R.id.pdr_view_stub_title_bar_layout)
    /** 加载页 */
    private val mPdrLoadingViewStub by bindView<ViewStub>(R.id.pdr_view_stub_loading_layout)
    /** 无数据页 */
    private val mPdrNoDataViewStub by bindView<ViewStub>(R.id.pdr_view_stub_no_data_layout)
    /** 失败页 */
    private val mPdrErrorViewStub by bindView<ViewStub>(R.id.pdr_view_stub_error_layout)

    /** 顶部标题布局 */
    private var mPdrTitleBarLayout: TitleBarLayout? = null
    /** 加载布局 */
    private var mPdrLoadingLayout: LoadingLayout? = null
    /** 无数据布局 */
    private var mPdrNoDataLayout: NoDataLayout? = null
    /** 错误布局 */
    private var mPdrErrorLayout: ErrorLayout? = null

    /** 内容布局 */
    private val mPdrContentLayout by bindView<LinearLayout>(R.id.pdr_content_layout)

    final override fun getAbsViewBindingLayout(): View? = super.getAbsViewBindingLayout()

    final override fun getAbsLayoutId(): Int = R.layout.pandora_activity_base

    final override fun afterSetContentView() {
        super.afterSetContentView()
        if (isUseAnkoLayout()) {
            throw RuntimeException("you already use anko layout , please extends AbsActivity and use @UseAnkoLayout annotation")
        }
        showStatusLoading()
        setContainerView()
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
        getTitleBarLayout().setOnBackBtnClickListener { view ->
            onClickBackBtn()
        }
    }

    /** 点击标题栏的返回按钮 */
    protected open fun onClickBackBtn() {}

    /** 点击错误页面的重试按钮 */
    protected open fun onClickReload() {}

    /** 显示无数据页面 */
    protected open fun showStatusNoData() {
        mPdrContentLayout.visibility = View.GONE
        mPdrLoadingLayout?.visibility = View.GONE
        mPdrErrorLayout?.visibility = View.GONE
        getNoDataLayout().visibility = View.VISIBLE
    }

    /** 显示错误页面 */
    protected open fun showStatusError() {
        showStatusError(null)
    }

    /** 显示错误页面 */
    protected open fun showStatusError(t: Throwable?) {
        mPdrContentLayout.visibility = View.GONE
        mPdrLoadingLayout?.visibility = View.GONE
        getErrorLayout().visibility = View.VISIBLE
        getErrorLayout().showAuto(t)
        mPdrNoDataLayout?.visibility = View.GONE
    }

    /** 显示加载页面 */
    protected open fun showStatusLoading() {
        mPdrContentLayout.visibility = View.GONE
        getLoadingLayout().visibility = View.VISIBLE
        mPdrErrorLayout?.visibility = View.GONE
        mPdrNoDataLayout?.visibility = View.GONE
    }

    /** 显示内容页面 */
    protected open fun showStatusCompleted() {
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
}