package com.lodz.android.pandora.js

/**
 * @author zhouL
 * @date 2021/8/23
 */
interface BridgeHandler {
    fun handler(data: String, function: CallBackFunction)
}