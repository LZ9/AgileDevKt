package com.lodz.android.agiledevkt.modules.webview.simple

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivitySimpleWebviewBinding
import com.lodz.android.agiledevkt.modules.splash.CheckDialog
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
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

    private val mBinding: ActivitySimpleWebviewBinding by bindingLayout(ActivitySimpleWebviewBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.wv_simple_title)
    }

    override fun onPressBack(): Boolean {
        if (mBinding.simpleWebview.isCanGoBack()) {
            mBinding.simpleWebview.goBack()
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
        // 后退按钮
        mBinding.backwardBtn.setOnClickListener {
            if (mBinding.simpleWebview.isCanGoBack()) {
                mBinding.simpleWebview.goBack()
            }
            mBinding.backwardBtn.isEnabled = mBinding.simpleWebview.isCanGoBack()
            mBinding.forwardBtn.isEnabled = mBinding.simpleWebview.isCanForward()
        }

        // 前进按钮
        mBinding.forwardBtn.setOnClickListener {
            if (mBinding.simpleWebview.isCanForward()) {
                mBinding.simpleWebview.goForward()
            }
            mBinding.backwardBtn.isEnabled = mBinding.simpleWebview.isCanGoBack()
            mBinding.forwardBtn.isEnabled = mBinding.simpleWebview.isCanForward()
        }

        mBinding.simpleWebview.setOnPgStatusChangeListener(object : OnPgStatusChangeListener {
            override fun onPageStarted(webView: WebView?, url: String?, favicon: Bitmap?) {}

            override fun onProgressChanged(webView: WebView?, progress: Int) {}

            override fun onReceivedError(webView: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                mBinding.backwardBtn.isEnabled = mBinding.simpleWebview.isCanGoBack()
                mBinding.forwardBtn.isEnabled = mBinding.simpleWebview.isCanForward()
            }

            override fun onPageFinished(webView: WebView?, url: String?) {
                mBinding.backwardBtn.isEnabled = mBinding.simpleWebview.isCanGoBack()
                mBinding.forwardBtn.isEnabled = mBinding.simpleWebview.isCanForward()
            }
        })

        mBinding.simpleWebview.setOnOtherOverrideUrlLoading { view, uri ->
            if ("bilibili" == uri.scheme) {
                showJumpBilibiliDialog(uri)
            }
            return@setOnOtherOverrideUrlLoading true
        }
    }

    override fun initData() {
        super.initData()
        mBinding.simpleWebview.loadUrl(BAIDU_URL)
        mBinding.backwardBtn.isEnabled = false
        mBinding.forwardBtn.isEnabled = false
        doDebug()
        showStatusCompleted()
    }

    /** 支持PC浏览器调试 */
    private fun doDebug() {
        WebView.setWebContentsDebuggingEnabled(true)
    }

    override fun finish() {
        super.finish()
        mBinding.simpleWebview.release()
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