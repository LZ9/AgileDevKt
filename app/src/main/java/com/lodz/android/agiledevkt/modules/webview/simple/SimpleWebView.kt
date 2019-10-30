package com.lodz.android.agiledevkt.modules.webview.simple

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ProgressBar
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.getDrawableCompat
import com.lodz.android.pandora.widget.webview.PgWebChromeClient
import com.lodz.android.pandora.widget.webview.PgWebView
import com.lodz.android.pandora.widget.webview.PgWebViewClient
import org.jetbrains.anko.padding

/**
 * 简单配置的WebView
 * @author zhouL
 * @date 2019/6/3
 */
class SimpleWebView : PgWebView {

    /** 百分比 */
    private lateinit var mPercentageTv: TextView
    /** 地址重定向回调 */
    private var mListener: ((view: WebView?, uri: Uri) -> Boolean)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onInit() {
        super.onInit()
        createTextView()
    }

    override fun createWebView(lp: LayoutParams): WebView {
        val wv = super.createWebView(lp)
        return wv
    }

    override fun createProgressBar(lp: LayoutParams): ProgressBar {
        val pg = super.createProgressBar(lp)
        lp.gravity = Gravity.BOTTOM
        pg.layoutParams = lp
        pg.progressDrawable = getDrawableCompat(R.drawable.bg_ffffff_progress_00a0e9)
        return pg
    }

    private fun createTextView() {
        mPercentageTv = TextView(context)
        mPercentageTv.padding = dp2px(10)
        mPercentageTv.setBackgroundResource(R.drawable.pandora_bg_a0191919_corners_8)
        mPercentageTv.setTextColor(getColorCompat(R.color.white))
        val lp = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER
        addView(mPercentageTv, lp)
    }

    override fun createWebViewClient(): PgWebViewClient = SimpleWebViewClient()

    override fun createWebChromeClient(): PgWebChromeClient = SimpleWebChromeClient()

    private inner class SimpleWebViewClient : PgWebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            mPercentageTv.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            mPercentageTv.visibility = View.GONE
        }

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            mPercentageTv.visibility = View.GONE
        }


        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && request != null) {
                if ("http" == request.url.scheme || "https" == request.url.scheme) {
                    return super.shouldOverrideUrlLoading(view, request)
                }
                return mListener?.invoke(view, request.url) ?: super.shouldOverrideUrlLoading(view, request)
            }
            return super.shouldOverrideUrlLoading(view, request)
        }
    }

    private inner class SimpleWebChromeClient : PgWebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            mPercentageTv.text = StringBuilder(newProgress.toString()).append("%")
            mPercentageTv.visibility = if (newProgress == 100) View.GONE else View.VISIBLE
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initWebSettings(settings: WebSettings) {
        super.initWebSettings(settings)
        // 开启JS
        settings.javaScriptEnabled = true
        // 使用DOM storage
        settings.domStorageEnabled = true
        // 网页图片展示
        settings.blockNetworkImage = false
        settings.blockNetworkLoads = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
    }

    /** 设置地址重定向回调监听器[listener] */
    fun setOnOtherOverrideUrlLoading(listener: (view: WebView?, uri: Uri) -> Boolean) {
        mListener = listener
    }
}