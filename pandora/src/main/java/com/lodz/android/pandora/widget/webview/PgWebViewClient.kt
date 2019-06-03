package com.lodz.android.pandora.widget.webview

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * 自定义WebViewClient
 * @author zhouL
 * @date 2019/6/3
 */
open class PgWebViewClient : WebViewClient() {

    private var mListener: OnPgStatusChangeListener? = null

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        mListener?.onPageStarted(view, url, favicon)
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        super.onReceivedError(view, request, error)
        mListener?.onReceivedError(view, request, error)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        mListener?.onPageFinished(view, url)
    }

    internal fun setListener(listener: OnPgStatusChangeListener) {
        mListener = listener
    }
}