package com.lodz.android.pandora.js

import android.webkit.WebViewClient
import android.webkit.WebView
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.utils.StringUtils

/**
 * @author zhouL
 * @date 2021/8/23
 */
open class BridgeWebViewClient(private var webView: BridgeWebView?) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url.isNullOrEmpty()) {
            return super.shouldOverrideUrlLoading(view, url)
        }
        val newUrl = StringUtils.decodeUtf8(url)
        if (newUrl.startsWith(BridgeUtil.YY_RETURN_DATA)) {// 如果是返回数据
            webView?.handlerReturnData(newUrl)
            return true
        } else if (newUrl.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) {
            webView?.flushMessageQueue()
            return true
        }
        return super.shouldOverrideUrlLoading(view, url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        view?.loadUrl(BridgeUtil.JAVASCRIPT_STR.append(BridgeUtil.assetJsFile2Str(view.context, BridgeUtil.JAVA_SCRIPT)))
        val list = webView?.getStartupMessage()
        if (list != null){
            list.forEach {
                webView?.dispatchMessage(it)
            }
            webView?.setStartupMessage(null)
        }
    }

}