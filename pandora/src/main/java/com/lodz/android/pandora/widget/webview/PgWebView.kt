package com.lodz.android.pandora.widget.webview

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getDrawableCompat
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.R
import java.nio.charset.StandardCharsets

/**
 * 加载控件的WebView
 * @author zhouL
 * @date 2019/6/3
 */
open class PgWebView : FrameLayout {

    companion object {
        /** 日志标签 */
        internal var TAG = "PgWebViewTag"
    }

    /** 浏览器  */
    private var mWebView: WebView? = null
    /** 进度条  */
    private lateinit var mProgressBar: ProgressBar

    /** 监听器 */
    private var mListener: OnPgStatusChangeListener? = null
    /** 是否加载成功 */
    private var isLoadSuccess = true

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        initViews()
        initWebView()
        onInit()
    }

    private fun initViews() {
        mWebView = createWebView(LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        addView(mWebView, mWebView?.layoutParams)
        mProgressBar = createProgressBar(LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(3)))
        addView(mProgressBar, mProgressBar.layoutParams)
    }

    protected open fun createWebView(lp: LayoutParams): WebView {
        val wv = WebView(context)
        wv.layoutParams = lp
        return wv
    }

    protected open fun createProgressBar(lp: LayoutParams): ProgressBar {
        val pg = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
        pg.max = 100
        pg.progress = 0
        pg.progressDrawable = getDrawableCompat(R.drawable.pandora_bg_ffffff_progress_aaaaaa)
        pg.visibility = View.GONE
        pg.layoutParams = lp
        return pg
    }

    private fun initWebView() {
        val wv = mWebView
        if (wv != null) {
            val wvc = createWebViewClient()
            wvc.setListener(object : OnPgStatusChangeListener {
                override fun onProgressChanged(webView: WebView?, progress: Int) {}
                override fun onPageStarted(webView: WebView?, url: String?, favicon: Bitmap?) {
                    mProgressBar.visibility = View.VISIBLE
                    PrintLog.wS(TAG, "onPageStarted")
                    mListener?.onPageStarted(webView, url, favicon)
                }

                override fun onReceivedError(webView: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    mProgressBar.visibility = View.GONE
                    isLoadSuccess = false
                    PrintLog.eS(TAG, error.toString())
                    mListener?.onReceivedError(webView, request, error)
                }

                override fun onPageFinished(webView: WebView?, url: String?) {
                    PrintLog.iS(TAG, "onPageFinished")
                    if (!isLoadSuccess) {
                        isLoadSuccess = true
                        return
                    }
                    mProgressBar.visibility = View.GONE
                    mListener?.onPageFinished(webView, url)
                }
            })
            wv.webViewClient = wvc

            val wcc = createWebChromeClient()
            wcc.setListener(object : OnPgStatusChangeListener {
                override fun onProgressChanged(webView: WebView?, progress: Int) {
                    PrintLog.dS(TAG, "Progress : $progress")
                    mProgressBar.progress = progress
                    mListener?.onProgressChanged(webView, progress)
                }

                override fun onPageStarted(webView: WebView?, url: String?, favicon: Bitmap?) {}
                override fun onReceivedError(webView: WebView?, request: WebResourceRequest?, error: WebResourceError?) {}
                override fun onPageFinished(webView: WebView?, url: String?) {}
            })

            wv.webChromeClient = wcc
            initWebSettings(wv.settings)
        }
    }


    /** 创建WebViewClient */
    protected open fun createWebViewClient(): PgWebViewClient = PgWebViewClient()

    /** 创建WebChromeClient */
    protected open fun createWebChromeClient(): PgWebChromeClient = PgWebChromeClient()

    /** 初始化WebSettings */
    protected open fun initWebSettings(settings: WebSettings) {
        // 默认文本编码，默认值 "UTF-8"
        settings.defaultTextEncodingName = StandardCharsets.UTF_8.toString()
        // 是否自动加载图片
        settings.loadsImagesAutomatically = true
        // 是否支持缩放
        settings.setSupportZoom(false)
        // 设置缓存模式
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
    }

    /** 初始化 */
    protected open fun onInit() {}

    /** 设置日志标签[tag] */
    fun setLogTag(tag: String) {
        if (!tag.isEmpty()) {
            TAG = tag
        }
    }

    /** 加载地址[url] */
    fun loadUrl(url: String) {
        mWebView?.loadUrl(url)
    }

    /** 是否可以回退 */
    fun isCanGoBack(): Boolean {
        return mWebView?.canGoBack() ?: false
    }

    /** 回退 */
    fun goBack() {
        mWebView?.goBack()
    }

    /** 是否可以前进 */
    fun isCanForward(): Boolean {
        return mWebView?.canGoForward() ?: false
    }

    /** 前进 */
    fun goForward() {
        mWebView?.goForward()
    }

    /** 重载 */
    fun reload() {
        mWebView?.reload()
    }

    /** 释放资源 */
    fun release() {
        mWebView?.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        mWebView?.clearHistory()
        mWebView?.clearCache(true)
        mWebView?.destroy()
        mWebView = null
    }

    fun setOnPgStatusChangeListener(listener: OnPgStatusChangeListener?) {
        mListener = listener
    }

}