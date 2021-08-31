package com.lodz.android.pandora.js

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import android.webkit.WebViewClient

import android.os.SystemClock

import android.os.Looper
import com.alibaba.fastjson.JSON
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

    private var uniqueId = 0L

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
        webViewClient = generateBridgeWebViewClient()
    }

    override fun register(apiName: String, handler: OnReceiveJsListener) {
        val name =  if (apiName.isEmpty()) DEFAULT_RECEIVE_API_NAME else apiName
        mReceiveJsMap[name] = handler
    }

    open fun generateBridgeWebViewClient(): WebViewClient = BridgeWebViewClient(this)

    fun handlerReturnData(url: String) {
        val functionName = BridgeUtil.getFunctionFromReturnUrl(url)
        val function = mCallBackJsMap[functionName]
        val data = BridgeUtil.getDataFromReturnUrl(url)
        if (function != null) {
            function.callbackJs(data ?: "")
            mCallBackJsMap.remove(functionName)
        }
    }

    override fun send(apiName: String, data: String, function: OnCallBackJsListener?) {
        doSend(apiName, data, function)
    }

    private fun doSend(handlerName: String, data: String, function: OnCallBackJsListener?) {
        val message = MessageBean()
        if (data.isNotEmpty()){
            message.data = data
        }
        if (function != null) {
            val callbackStr = String.format(
                BridgeUtil.CALLBACK_ID_FORMAT,
                (++uniqueId).toString() + BridgeUtil.UNDERLINE_STR + SystemClock.currentThreadTimeMillis()
            )
            mCallBackJsMap[callbackStr] = function
            message.callbackId = callbackStr
            message.handlerName = handlerName
        }
        queueMessage(message)
    }

    private fun queueMessage(message: MessageBean) {
        dispatchMessage(message)
    }

    fun dispatchMessage(message: MessageBean) {
        val json = JSON.toJSONString(message)
            .replace("(\\\\)([^utrn])".toRegex(), "\\\\\\\\$1$2")
            .replace("(?<=[^\\\\])(\")".toRegex(), "\\\\\"")

        val javascriptCommand = String.format(BridgeUtil.JS_HANDLE_MESSAGE_FROM_JAVA, json)
        if (Thread.currentThread() == Looper.getMainLooper().thread){
            loadUrl(javascriptCommand)
        }
    }

    fun flushMessageQueue() {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            loadUrl(BridgeUtil.JS_FETCH_QUEUE_FROM_JAVA, object : OnCallBackJsListener {
                override fun callbackJs(data: String) {
                    val list = JSON.parseArray(data, MessageBean::class.java)
                    if (list.isNullOrEmpty()) {
                        return
                    }
                    for (item in list) {
                        val responseId = item.responseId
                        if (responseId.isNotEmpty()) {
                            val function = mCallBackJsMap[responseId]
                            val responseData = item.responseData
                            function?.callbackJs(responseData)
                            mCallBackJsMap.remove(responseId)
                        } else {
                            var responseFunction: OnCallBackJsListener = object : OnCallBackJsListener {
                                override fun callbackJs(data: String) {
                                }
                            }
                            if (item.callbackId.isNotEmpty()) {
                                responseFunction = OnCallBackJsListener {
                                        val responseMsg = MessageBean()
                                        responseMsg.responseId = item.callbackId
                                        responseMsg.responseData = it
                                        queueMessage(responseMsg)
                                    }
                            }
                            val handler = if (item.handlerName.isNotEmpty()) {
                                mReceiveJsMap[item.handlerName]
                            } else {
                                mReceiveJsMap[DEFAULT_RECEIVE_API_NAME]
                            }
                            handler?.onReceive(item.data, responseFunction)
                        }

                    }

                }

            })
        }
    }

    fun loadUrl(jsUrl: String, returnCallback: OnCallBackJsListener) {
        loadUrl(jsUrl)
        mCallBackJsMap[BridgeUtil.parseFunctionName(jsUrl)] = returnCallback
    }
}