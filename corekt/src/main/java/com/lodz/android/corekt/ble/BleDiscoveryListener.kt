package com.lodz.android.corekt.ble

import android.bluetooth.BluetoothDevice

/**
 * 蓝牙搜索监听器
 * Created by zhouL on 2018/8/1.
 */
interface BleDiscoveryListener {

    /** 搜索设备开始 */
    fun onStartDiscovery()

    /** 搜索设备结束 */
    fun onFinishedDiscovery()

    /** 发现设备 */
    fun onFoundDevice(device: BluetoothDevice)

    /** 设备断开 */
    fun onDeviceDisconnected()
}