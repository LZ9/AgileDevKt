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
        /** 基站信息mcc */
        val mcc: String,
        /** 基站信息mnc */
        val mnc: String,
        /** 基站信息lac */
        val lac: String,
        /** 基站信息cid */
        val cid: String,
        /** 日志信息 */
        val log: String) {

}