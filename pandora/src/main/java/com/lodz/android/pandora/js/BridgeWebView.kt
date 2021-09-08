package com.lodz.android.pandora.js

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import com.lodz.android.pandora.js.contract.OnCallBackJsListener
import com.lodz.android.pandora.js.contract.OnReceiveJsListener
import com.lodz.android.pandora.js.contract.WebViewJavascriptBridge

/**
 * @author zhouL
 * @date 2021/8/23
 */
@SuppressLint("SetJavaScriptEnabled")
class BridgeWebView : WebView, WebViewJavascriptBridge {

    private lateinit var mJsBridgeManager :JsBridgeManager

    constructor(context: Context) : super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes){
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        privateBrowsing: Boolean
    ) : super(context, attrs, defStyleAttr, privateBrowsing){
        init()
    }

    private fun init(){
        mJsBridgeManager = JsBridgeManager(this)
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        settings.javaScriptEnabled = true
        setWebContentsDebuggingEnabled(true)
        webViewClient = createBridgeWebViewClient()
    }

    /** 创建一个BridgeWebViewClient */
     fun createBridgeWebViewClient(): BridgeWebViewClient = BridgeWebViewClient(this)

    override fun send(apiName: String, data: String, function: OnCallBackJsListener?) {
        mJsBridgeManager.send(apiName, data, function)
    }

    override fun register(apiName: String, handler: OnReceiveJsListener) {
        mJsBridgeManager.register(apiName, handler)
    }

    override fun handlerJsReturnData(url: String) {
        mJsBridgeManager.handlerJsReturnData(url)
    }

    override fun registerBridgeReceive() {
        mJsBridgeManager.registerBridgeReceive()
    }
}