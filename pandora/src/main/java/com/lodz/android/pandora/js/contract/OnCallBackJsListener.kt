package com.lodz.android.pandora.js.contract

/**
 * 回调数据给JS监听器
 * @author zhouL
 * @date 2021/8/23
 */
fun interface OnCallBackJsListener {
    /** 回调数据[data]给JS */
    fun callbackJs(data: String)
}