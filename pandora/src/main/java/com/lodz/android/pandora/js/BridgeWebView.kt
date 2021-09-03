package com.lodz.android.pandora.js

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

import android.os.Looper
import com.alibaba.fastjson.JSON
import com.lodz.android.pandora.js.contract.OnBridgeReceiveListener
import com.lodz.android.pandora.js.contract.OnCallBackJsListener
import com.lodz.android.pandora.js.contract.OnReceiveJsListener
import com.lodz.android.pandora.js.contract.WebViewJavascriptBridge


/**
 * @author zhouL
 * @date 2021/8/23
 */
@SuppressLint("SetJavaScriptEnabled")
open class BridgeWebView : WebView, WebViewJavascriptBridge {

    private val DEFAULT_RECEIVE_API_NAME = "default_receive"

    /** 回调JS接口缓存集合 */
    private val mCallBackJsMap: HashMap<String, OnCallBackJsListener> = HashMap()
    /** 接收JS数据接口缓存集合 */
    private val mReceiveJsMap: HashMap<String, OnReceiveJsListener> = HashMap()
    /** JsBridge接收接口缓存集合 */
    private val mBridgeReceiveMap: HashMap<String, OnBridgeReceiveListener> = HashMap()

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
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        settings.javaScriptEnabled = true
        setWebContentsDebuggingEnabled(true)
        webViewClient = createBridgeWebViewClient()
    }

    /** 创建一个BridgeWebViewClient */
    open fun createBridgeWebViewClient(): BridgeWebViewClient = BridgeWebViewClient(this)

    /** 注册接口名为[apiName]的接口，接收H5的回调[handler] */
    override fun register(apiName: String, handler: OnReceiveJsListener) {
        val name =  if (apiName.isEmpty()) DEFAULT_RECEIVE_API_NAME else apiName
        mReceiveJsMap[name] = handler
    }

    /** 发送接口名为[apiName]的数据[data]给H5，H5通过[function]回调结果 */
    override fun send(apiName: String, data: String, function: OnCallBackJsListener?) {
        val message = MessageBean()
        message.data = data
        if (function != null) {
            val callbackId = String.format(BridgeUtil.CALLBACK_ID_FORMAT, System.currentTimeMillis())
            mCallBackJsMap[callbackId] = function
            message.callbackId = callbackId
        }
        message.handlerName = apiName
        sendMessageToJs(message)
    }

    /** 发送数据给H5 */
    private fun sendMessageToJs(message: MessageBean) {
        val json = JSON.toJSONString(message)
            .replace("(\\\\)([^utrn])".toRegex(), "\\\\\\\\$1$2")
            .replace("(?<=[^\\\\])(\")".toRegex(), "\\\\\"")

        val javascriptCommand = String.format(BridgeUtil.JS_HANDLE_MESSAGE_FROM_JAVA, json)
        if (Thread.currentThread() == Looper.getMainLooper().thread){
            loadUrl(javascriptCommand)
        }
    }

    fun registerBridgeReceive() {
        if (Thread.currentThread() != Looper.getMainLooper().thread) {
            return
        }
        loadUrl(BridgeUtil.JS_FETCH_QUEUE_FROM_JAVA)
        mBridgeReceiveMap[BridgeUtil.getJsBridgeName()] = OnBridgeReceiveListener {
            val list = JSON.parseArray(it, MessageBean::class.java)
            if (list.isNullOrEmpty()) {
                return@OnBridgeReceiveListener
            }
            for (item in list) {
                val responseId = item.responseId
                if (responseId.isNotEmpty()) {
                    val function = mCallBackJsMap[responseId]
                    val responseData = item.responseData
                    function?.callbackJs(responseData)
                    mCallBackJsMap.remove(responseId)
                } else {
                    val responseFunction = OnCallBackJsListener {
                        if (item.callbackId.isNotEmpty()) {
                            val responseMsg = MessageBean()
                            responseMsg.responseId = item.callbackId
                            responseMsg.responseData = it
                            sendMessageToJs(responseMsg)
                        }
                    }
                    val apiName = if (item.handlerName.isEmpty()) DEFAULT_RECEIVE_API_NAME else item.handlerName
                    mReceiveJsMap[apiName]?.onReceive(item.data, responseFunction)
                }
            }
        }
    }

    /** 处理JS发送来的[url]数据 */
    fun handlerJsReturnData(url: String) {
        val jsBridgeName = BridgeUtil.getJsBridgeNameFromReturnUrl(url)
        val function = mBridgeReceiveMap[jsBridgeName]
        if (function != null) {
            function.onReceive(BridgeUtil.getDataFromReturnUrl(url) ?: "")
            mBridgeReceiveMap.remove(jsBridgeName)
        }
    }
}