package com.lodz.android.pandora.rx.status

/**
 * 接口数据状态
 * Created by zhouL on 2018/7/3.
 */
interface ResponseStatus {

    /** 数据是否返回成功 */
    fun isSuccess(): Boolean

    /** 获取提示语信息 */
    fun valueMsg(): String

    /** 获取状态标志 */
    fun valueStatus(): String

    /** 是否token过期，默认不需要校验token */
    fun isTokenUnauth(): Boolean = false
}