package com.lodz.android.pandora.js.contract

/**
 * JsBridge的内部数据接收接口
 * @author zhouL
 * @date 2021/8/23
 */
internal fun interface OnBridgeReceiveListener {
    /** 接收JsBridge数据[data] */
    fun onReceive(data: String)
}