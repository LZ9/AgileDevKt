package com.lodz.android.agiledevkt.bean.base

import com.lodz.android.pandora.rx.status.ResponseStatus

/**
 * 数据接收实体类
 * Created by zhouL on 2019/1/8.
 */
open class ResponseBean<T> : ResponseStatus {

    companion object {
        const val SUCCESS = 1
        const val FAIL = 2
    }

    /** 状态 */
    var code = FAIL
    /** 信息 */
    var msg = ""
    /** 数据内容 */
    var data: T? = null

    override fun isSuccess(): Boolean = code == SUCCESS

    override fun valueMsg(): String = msg

    override fun valueStatus(): String = code.toString()
}