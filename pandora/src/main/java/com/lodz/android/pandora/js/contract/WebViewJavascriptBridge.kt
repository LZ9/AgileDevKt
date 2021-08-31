package com.lodz.android.pandora.js.contract

/**
 * JsBridge接口
 * @author zhouL
 * @date 2021/8/23
 */
interface WebViewJavascriptBridge {

    /** 发送接口名为[apiName]的数据[data]给H5，H5通过[function]回调结果 */
    fun send(apiName: String = "", data: String, function: OnCallBackJsListener? = null)

    /** 注册接口名为[apiName]的接口，接收H5的回调[handler] */
    fun register(apiName: String = "", handler: OnReceiveJsListener)
}