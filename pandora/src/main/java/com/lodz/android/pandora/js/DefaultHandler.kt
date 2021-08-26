package com.lodz.android.pandora.js

/**
 * @author zhouL
 * @date 2021/8/23
 */
class DefaultHandler : BridgeHandler {
    override fun handler(data: String, function: CallBackFunction?) {
        function?.onCallBack("DefaultHandler response data")
    }
}