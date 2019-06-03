package com.lodz.android.pandora.widget.webview

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView

/**
 * PgWebView监听器
 * @author zhouL
 * @date 2019/6/3
 */
interface OnPgStatusChangeListener {

    /** 开始加载页面，控件[webView]，地址[url]，图标[favicon] */
    fun onPageStarted(webView: WebView?, url: String?, favicon: Bitmap?)

    /** 加载进度回调，控件[webView]，进度[progress] */
    fun onProgressChanged(webView: WebView?, progress: Int)

    /** 加载失败，控件[webView]，请求[request]，失败结果[error] */
    fun onReceivedError(webView: WebView?, request: WebResourceRequest?, error: WebResourceError?)

    /** 加载完成，控件[webView]，地址[url] */
    fun onPageFinished(webView: WebView?, url: String?)

}