package com.lodz.android.pandora.js

import android.webkit.WebResourceRequest
import android.webkit.WebViewClient
import android.webkit.WebView
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.utils.StringUtils
import com.lodz.android.pandora.js.contract.WebViewJavascriptBridge

/**
 * @author zhouL
 * @date 2021/8/23
 */
open class BridgeWebViewClient(private val bridge: WebViewJavascriptBridge) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url?.toString()
        if (url.isNullOrEmpty()) {
            return super.shouldOverrideUrlLoading(view, request)
        }
        val newUrl = StringUtils.decodeUtf8(url)
        if (newUrl.startsWith(BridgeUtil.YY_RETURN_DATA)) {// 如果是JS返回数据
            bridge.handlerJsReturnData(newUrl)
            return true
        } else if (newUrl.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) {
            bridge.registerBridgeReceive()
            return true
        }
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        view?.loadUrl(BridgeUtil.JAVASCRIPT_STR.append(BridgeUtil.assetJsFile2Str(view.context, BridgeUtil.JAVA_SCRIPT)))
    }

}