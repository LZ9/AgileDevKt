package com.lodz.android.pandora.js.contract

/**
 * 接收JS数据监听器
 * @author zhouL
 * @date 2021/8/23
 */
fun interface OnReceiveJsListener{

    /** 注册接口名为[apiName]的接口，接收H5的回调[onReceive] */
    fun onReceive(data: String, listener: OnCallBackJsListener)
}