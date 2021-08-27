package com.lodz.android.pandora.js

/**
 * @author zhouL
 * @date 2021/8/23
 */
class MessageBean {
    /** 请求id */
    var callbackId: String = ""

    /** 请求参数 */
    var data: String = ""

    /** 响应id */
    var responseId: String = ""

    /** 响应数据 */
    var responseData: String = ""

    /** 回调方法的名称 */
    var handlerName: String = ""
}