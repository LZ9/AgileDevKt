package com.lodz.android.pandora.js

/**
 * @author zhouL
 * @date 2021/8/23
 */
interface WebViewJavascriptBridge {

    fun send(data: String)

    fun send(data: String, function: CallBackFunction?)

}