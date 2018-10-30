package com.lodz.android.componentkt.base.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.lodz.android.componentkt.R
import com.lodz.android.componentkt.widget.base.ErrorLayout
import com.lodz.android.componentkt.widget.base.LoadingLayout
import com.lodz.android.componentkt.widget.base.NoDataLayout
import com.lodz.android.componentkt.widget.base.TitleBarLayout

/**
 * 基类Activity（带基础状态控件）
 * Created by zhouL on 2018/6/27.
 */
abstract class BaseActivity : AbsActivity() {

    private val mTitleBarViewStub: ViewStub by lazy {
        findViewById(R.id.view_stub_title_bar_layout) as ViewStub
    }
    private val mLoadingViewStub: ViewStub by lazy {
        findViewById(R.id.view_stub_loading_layout) as ViewStub
    }
    private val mNoDataViewStub: ViewStub by lazy {
        findViewById(R.id.view_stub_no_data_layout) as ViewStub
    }
    private val mErrorViewStub: ViewStub by lazy {
        findViewById(R.id.view_stub_error_layout) as ViewStub
    }

    /** 顶部标题布局  */
    private var mTitleBarLayout: TitleBarLayout? = null
    /** 加载布局  */
    private var mLoadingLayout: LoadingLayout? = null
    /** 无数据布局  */
    private var mNoDataLayout: NoDataLayout? = null
    /** 错误布局  */
    private var mErrorLayout: ErrorLayout? = null

    /** 内容布局  */
    private val mContentLayout: LinearLayout by lazy {
        findViewById(R.id.content_layout) as LinearLayout
    }

    override fun getAbsLayoutId() = R.layout.componentkt_activity_base_layout

    final override fun afterSetContentView() {
        super.afterSetContentView()
        if (!isUseAnkoLayout()){
            showStatusLoading()
            setContainerView()
        }
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
        if (!isUseAnkoLayout()){
            getTitleBarLayout().setOnBackBtnClickListener { view ->
                onClickBackBtn()
            }
        }
    }

    /** 点击标题栏的返回按钮 */
    protected open fun onClickBackBtn() {}

    /** 点击错误页面的重试按钮 */
    protected open fun onClickReload() {}

    /** 显示无数据页面 */
    protected open fun showStatusNoData() {
        if (isUseAnkoLayout()){
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        mContentLayout.visibility = View.GONE
        if (mLoadingLayout != null){
            mLoadingLayout!!.visibility = View.GONE
        }
        if (mErrorLayout != null){
            mErrorLayout!!.visibility = View.GONE
        }
        getNoDataLayout().visibility = View.VISIBLE
    }

    /** 显示错误页面 */
    protected open fun showStatusError() {
        if (isUseAnkoLayout()){
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        mContentLayout.visibility = View.GONE
        if (mLoadingLayout != null){
            mLoadingLayout!!.visibility = View.GONE
        }
        getErrorLayout().visibility = View.VISIBLE
        if (mNoDataLayout != null){
            mNoDataLayout!!.visibility = View.GONE
        }
    }

    /** 显示加载页面 */
    protected open fun showStatusLoading(){
        if (isUseAnkoLayout()){
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        mContentLayout.visibility = View.GONE
        getLoadingLayout().visibility = View.VISIBLE
        if (mErrorLayout != null){
            mErrorLayout!!.visibility = View.GONE
        }
        if (mNoDataLayout != null){
            mNoDataLayout!!.visibility = View.GONE
        }
    }

    /** 显示内容页面 */
    protected open fun showStatusCompleted(){
        if (isUseAnkoLayout()){
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        mContentLayout.visibility = View.VISIBLE
        if (mLoadingLayout != null){
            mLoadingLayout!!.visibility = View.GONE
        }
        if (mErrorLayout != null){
            mErrorLayout!!.visibility = View.GONE
        }
        if (mNoDataLayout != null){
            mNoDataLayout!!.visibility = View.GONE
        }
    }

    /** 隐藏TitleBar */
    protected fun goneTitleBar(){
        if (isUseAnkoLayout()){
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        getTitleBarLayout().visibility = View.GONE
    }

    /** 显示TitleBar */
    protected fun showTitleBar() {
        if (isUseAnkoLayout()){
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        getTitleBarLayout().visibility = View.VISIBLE
    }

    /** 获取顶部标题栏控件 */
    protected fun getTitleBarLayout() : TitleBarLayout{
        if (isUseAnkoLayout()){
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        if (mTitleBarLayout == null){
            mTitleBarLayout = mTitleBarViewStub.inflate() as TitleBarLayout
        }
        return mTitleBarLayout!!
    }

    /** 获取加载控件 */
    protected fun getLoadingLayout() : LoadingLayout{
        if (isUseAnkoLayout()){
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        if (mLoadingLayout == null){
            mLoadingLayout = mLoadingViewStub.inflate() as LoadingLayout
            mLoadingLayout!!.visibility = View.GONE
        }
        return mLoadingLayout!!
    }

    /** 获取无数据控件 */
    protected fun getNoDataLayout() : NoDataLayout{
        if (isUseAnkoLayout()){
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        if (mNoDataLayout == null){
            mNoDataLayout = mNoDataViewStub.inflate() as NoDataLayout
            mNoDataLayout!!.visibility = View.GONE
        }
        return mNoDataLayout!!
    }

    /** 获取加载失败界面 */
    protected fun getErrorLayout() : ErrorLayout{
        if (isUseAnkoLayout()){
            throw RuntimeException("you already use anko layout do not use base widget")
        }
        if (mErrorLayout == null){
            mErrorLayout = mErrorViewStub.inflate() as ErrorLayout
            mErrorLayout!!.visibility = View.GONE
            mErrorLayout!!.setReloadListener {
                onClickReload()
            }
        }
        return mErrorLayout!!
    }
}