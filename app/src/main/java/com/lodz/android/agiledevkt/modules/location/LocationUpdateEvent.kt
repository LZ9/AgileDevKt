package com.lodz.android.agiledevkt.modules.location

/**
 * 定位更新事件
 * Created by zhouL on 2018/10/17.
 */
class LocationUpdateEvent(
        /** 是否定位数据 */
        val isLocationData: Boolean,
        /** 经度 */
        val longitude: String,
        /** 纬度 */
        val latitude: String,
        /** 基站信息-网络运营商 */
        val networkOperator: String,
        /** 基站信息-信号强度（dbm） */
        val dbm: Int,
        /** 基站信息-mcc */
        val mcc: String,
        /** 基站信息-mnc */
        val mnc: String,
        /** 基站信息-areaCode */
        val areaCode: String,
        /** 基站信息-cellId */
        val cellId: String,
        /** 日志信息 */
        val log: String)