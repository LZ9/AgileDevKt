package com.lodz.android.agiledevkt.modules.webview.simple

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.ImageView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.splash.CheckDialog
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

        mWebView.setOnOtherOverrideUrlLoading { view, uri ->
            if ("bilibili" == uri.scheme) {
                showJumpBilibiliDialog(uri)
            }
            return@setOnOtherOverrideUrlLoading true
        }
    }

    override fun initData() {
        super.initData()
        mWebView.loadUrl(BAIDU_URL)
        mBackwardBtn.isEnabled = false
        mForwardBtn.isEnabled = false
        doDebug()
        showStatusCompleted()
    }

    /** 支持PC浏览器调试 */
    private fun doDebug() {
        WebView.setWebContentsDebuggingEnabled(true)
    }

    override fun finish() {
        super.finish()
        mWebView.release()
    }

    /** 显示跳转哔哩哔哩提示弹框 */
    private fun showJumpBilibiliDialog(uri: Uri) {
        val checkDialog = CheckDialog(getContext())
        checkDialog.setContentMsg(R.string.wv_simple_check_bilibili_title)
        checkDialog.setPositiveText(R.string.wv_simple_check_confirm, DialogInterface.OnClickListener { dialog, which ->
            startActivity(Intent(Intent.ACTION_VIEW, uri))
            dialog.dismiss()
        })
        checkDialog.setNegativeText(R.string.wv_simple_check_cancel, DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        checkDialog.setCanceledOnTouchOutside(true)
        checkDialog.show()
    }
}