package com.lodz.android.corekt.ble

/**
 * 蓝牙状态监听器
 * Created by zhouL on 2018/8/1.
 */
fun interface BleStateListener {

    /** 状态[state]变化 */
    fun onStateChange(state: Int)
}