package com.lodz.android.pandora.js

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import android.webkit.WebViewClient
import java.util.ArrayList

import android.os.SystemClock

import android.os.Looper
import com.alibaba.fastjson.JSON
import com.lodz.android.pandora.js.contract.OnCallBackJsListener
import com.lodz.android.pandora.js.contract.OnReceiveJsListener
import com.lodz.android.pandora.js.contract.WebViewJavascriptBridge


/**
 *
 * @author zhouL
 * @date 2021/8/23
 */
@SuppressLint("SetJavaScriptEnabled")
open class BridgeWebView : WebView, WebViewJavascriptBridge {

    private val responseCallbacks: HashMap<String, OnCallBackJsListener> = HashMap()
    private val messageHandlers: HashMap<String, OnReceiveJsListener> = HashMap()
    private var defaultHandler: OnReceiveJsListener = OnReceiveJsListener { data, listener -> }

    private var startupMessage: ArrayList<MessageBean>? = ArrayList()

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

    fun getStartupMessage(): ArrayList<MessageBean>? = startupMessage

    fun setStartupMessage(list: ArrayList<MessageBean>?) {
        startupMessage = list
    }

    override fun register(apiName: String, handler: OnReceiveJsListener) {
        if (apiName.isEmpty()) {
            defaultHandler = handler
        } else {
            messageHandlers[apiName] = handler
        }
    }

    open fun generateBridgeWebViewClient(): WebViewClient = BridgeWebViewClient(this)

    fun handlerReturnData(url: String) {
        val functionName = BridgeUtil.getFunctionFromReturnUrl(url)
        val function = responseCallbacks[functionName]
        val data = BridgeUtil.getDataFromReturnUrl(url)
        if (function != null) {
            function.callbackJs(data ?: "")
            responseCallbacks.remove(functionName)
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
            responseCallbacks[callbackStr] = function
            message.callbackId = callbackStr
            message.handlerName = handlerName
        }
        queueMessage(message)
    }

    private fun queueMessage(message: MessageBean) {
        if (startupMessage != null) {
            startupMessage?.add(message)
        } else {
            dispatchMessage(message)
        }
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
                            val function = responseCallbacks[responseId]
                            val responseData = item.responseData
                            function?.callbackJs(responseData)
                            responseCallbacks.remove(responseId)
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
                                messageHandlers[item.handlerName]
                            } else {
                                defaultHandler
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
        responseCallbacks[BridgeUtil.parseFunctionName(jsUrl)] = returnCallback
    }
}