package com.lodz.android.pandora.widget.webview

import android.webkit.WebChromeClient
import android.webkit.WebView

/**
 * 自定义WebChromeClient
 * @author zhouL
 * @date 2019/6/3
 */
open class PgWebChromeClient : WebChromeClient() {

    private var mPdrListener: OnPgStatusChangeListener? = null

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        mPdrListener?.onProgressChanged(view, newProgress)
    }

    internal fun setListener(listener: OnPgStatusChangeListener) {
        mPdrListener = listener
    }
}