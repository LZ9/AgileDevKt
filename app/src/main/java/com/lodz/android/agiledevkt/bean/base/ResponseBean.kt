package com.lodz.android.agiledevkt.bean.base

import com.lodz.android.pandora.rx.status.ResponseStatus

/**
 * 数据接收实体类
 * Created by zhouL on 2019/1/8.
 */
open class ResponseBean<T> : ResponseStatus {

    companion object {
        const val SUCCESS = 200
        const val FAIL = 500

        @JvmStatic
        fun <T> createFail(): ResponseBean<T> = ResponseBean()

        @JvmStatic
        fun <T> createSuccess(): ResponseBean<T> {
            val responseBean = ResponseBean<T>()
            responseBean.code = SUCCESS
            return responseBean
        }
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