package com.lodz.android.agiledevkt.modules.webview

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.ImageView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.webview.OnPgStatusChangeListener

/**
 * 简单配置的WebView测试类
 * @author zhouL
 * @date 2019/6/3
 */
class SimpleWebViewActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SimpleWebViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val BAIDU_URL = "https://www.bilibili.com/"

    /** WebView */
    private val mWebView by bindView<SimpleWebView>(R.id.simple_webview)
    /** 后退按钮 */
    private val mBackwardBtn by bindView<ImageView>(R.id.backward_btn)
    /** 前进按钮 */
    private val mForwardBtn by bindView<ImageView>(R.id.forward_btn)

    override fun getLayoutId(): Int = R.layout.activity_simple_webview

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.wv_simple_title)
    }

    override fun onPressBack(): Boolean {
        if (mWebView.isCanGoBack()) {
            mWebView.goBack()
            return true
        }
        return super.onPressBack()
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mBackwardBtn.setOnClickListener {
            if (mWebView.isCanGoBack()) {
                mWebView.goBack()
            }
            mBackwardBtn.isEnabled = mWebView.isCanGoBack()
            mForwardBtn.isEnabled = mWebView.isCanForward()
        }

        mForwardBtn.setOnClickListener {
            if (mWebView.isCanForward()) {
                mWebView.goForward()
            }
            mBackwardBtn.isEnabled = mWebView.isCanGoBack()
            mForwardBtn.isEnabled = mWebView.isCanForward()
        }

        mWebView.setOnPgStatusChangeListener(object : OnPgStatusChangeListener {
            override fun onPageStarted(webView: WebView?, url: String?, favicon: Bitmap?) {}

            override fun onProgressChanged(webView: WebView?, progress: Int) {}

            override fun onReceivedError(webView: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                mBackwardBtn.isEnabled = mWebView.isCanGoBack()
                mForwardBtn.isEnabled = mWebView.isCanForward()
            }

            override fun onPageFinished(webView: WebView?, url: String?) {
                mBackwardBtn.isEnabled = mWebView.isCanGoBack()
                mForwardBtn.isEnabled = mWebView.isCanForward()
            }
        })
    }

    override fun initData() {
        super.initData()
        mWebView.loadUrl(BAIDU_URL)
        mBackwardBtn.isEnabled = false
        mForwardBtn.isEnabled = false
        showStatusCompleted()
    }
}