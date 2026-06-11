package com.lodz.android.corekt.network

import androidx.annotation.IntDef

/**
 * 运营商信息
 * Created by zhouL on 2018/7/3.
 */
class OperatorInfo {

    companion object {

        /** 未知 */
        const val OPERATOR_UNKNOWN = 0
        /** 移动 */
        const val OPERATOR_CMCC = 1
        /** 联通 */
        const val OPERATOR_CUCC = 2
        /** 电信 */
        const val OPERATOR_CTCC = 3
        /** 广电 */
        const val OPERATOR_CBN = 4
    }

    @IntDef(OPERATOR_UNKNOWN, OPERATOR_CMCC, OPERATOR_CUCC, OPERATOR_CTCC, OPERATOR_CBN)
    @Retention(AnnotationRetention.SOURCE)
    annotation class OperatorType

    /** 运营商代码 */
    @OperatorType
    var operatorType = OPERATOR_UNKNOWN

    /** 运营商名称 */
    var operatorName = ""

    /** 运营商名称（MNC转换） */
    var operatorNameMNC = ""

    /** 网络类型 */
    var networkType = ""

    // --------------------- 基站信息 ---------------------------
    /** 移动国家代码，中国460 */
    var mcc = ""
    /** 移动网络代码 */
    var mnc = ""

    /**
     * 2G/3G：LAC
     * 4G/5G：TAC
     */
    var areaCode = ""

    /**
     * 2G/3G：CID
     * 4G：CI
     * 5G：NCI
     */
    var cellId  = ""

    /** 信号强度 */
    var dbm = 0

    /** 是否是当前注册基站 */
    var isRegistered = false

    /** 是否获取成功 */
    fun isSuccess(): Boolean = operatorType != OPERATOR_UNKNOWN
}