package com.lodz.android.corekt.network

import android.support.annotation.IntDef

/**
 * 运营商信息
 * Created by zhouL on 2018/7/3.
 */
class OperatorInfo {

    companion object {

        /** 未知  */
        const val OPERATOR_UNKNOWN = 0
        /** 移动  */
        const val OPERATOR_CMCC = 1
        /** 联通  */
        const val OPERATOR_CUCC = 2
        /** 电信  */
        const val OPERATOR_CTCC = 3
    }

    @IntDef(OPERATOR_UNKNOWN, OPERATOR_CMCC, OPERATOR_CUCC, OPERATOR_CTCC)
    @Retention(AnnotationRetention.SOURCE)
    annotation class OperatorType

    /** 网络类型 */
    @OperatorType
    var type = OPERATOR_UNKNOWN

    // --------------------- 基站信息 ---------------------------
    /** 中国460  */
    var mcc = ""
    /** 00移动、01联通、11电信4G  */
    var mnc = ""
    /** 1~65535  */
    var lac = ""
    /** 2G（1~65535） 3G/4G（1~268435455）  */
    var cid = ""

    /** 是否获取成功 */
    fun isSuccess() = type != OPERATOR_UNKNOWN
}