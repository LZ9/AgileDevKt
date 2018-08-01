package com.lodz.android.corekt.ble

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.annotation.RequiresPermission

/**
 * 简单的蓝牙辅助类
 * Created by zhouL on 2018/7/31.
 */
class BleSimpleHelper private constructor() {

    companion object {
        private val sInstance = BleSimpleHelper()
        fun get() = sInstance
    }

    /** 蓝牙适配器 */
    private val mBluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    /** 蓝牙搜索监听器 */
    private val mBleDiscoveryListener = ArrayList<BleDiscoveryListener>()
    /** 蓝牙状态监听器 */
    private val mBleStateListener = ArrayList<BleStateListener>()
    /** 蓝牙广播接收器 */
    private val mBleReceiver = BleReceiver()

    fun getBluetoothAdapter() = mBluetoothAdapter

    /** 注册蓝牙广播接收器 */
    fun registerBleReceiver(context: Context) {
        try {
            val filter = IntentFilter()
            filter.addAction(BluetoothDevice.ACTION_FOUND)// 蓝牙扫描时，扫描到任一远程蓝牙设备时，会发送此广播
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)// 蓝牙扫描过程开始
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)// 蓝牙扫描过程结束
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)// 蓝牙设备断开
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)// 蓝牙状态变化
            context.applicationContext.registerReceiver(mBleReceiver, filter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** 解注册蓝牙广播接收器 */
    fun unregisterBleReceiver(context: Context) {
        try {
            context.applicationContext.unregisterReceiver(mBleReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取蓝牙的状态
     * 关闭：BluetoothAdapter.STATE_OFF
     * 关闭中：BluetoothAdapter.STATE_TURNING_OFF
     * 打开：BluetoothAdapter.STATE_ON
     * 打开中：BluetoothAdapter.STATE_TURNING_ON
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    fun getBluetoothState(): Int {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.state
        }
        return BluetoothAdapter.STATE_OFF
    }

    /** 设置蓝牙状态是否启用[enable] */
    @RequiresPermission(allOf = arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN))
    fun setBluetoothState(enable: Boolean) {
        if (getBluetoothState() == BluetoothAdapter.STATE_ON && !enable) {
            mBluetoothAdapter?.disable()
        }
        if (getBluetoothState() == BluetoothAdapter.STATE_OFF && enable) {
            mBluetoothAdapter?.enable()
        }
    }

    /** 蓝牙是否打开 */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    fun isBluetoothEnabled() = mBluetoothAdapter?.isEnabled ?: false

    /** 开始扫描 */
    @RequiresPermission(allOf = arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN))
    fun startDiscovery(): Boolean {
        if (mBluetoothAdapter == null) {
            return false
        }
        if (!mBluetoothAdapter.isEnabled) {
            return false
        }
        if (mBluetoothAdapter.isDiscovering) {
            return false
        }
        mBluetoothAdapter.startDiscovery()
        return true
    }

    /** 停止扫描 */
    @RequiresPermission(allOf = arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN))
    fun cancelDiscovery(): Boolean {
        if (mBluetoothAdapter == null) {
            return false
        }
        if (!mBluetoothAdapter.isEnabled) {
            return false
        }
        if (mBluetoothAdapter.isDiscovering) {
            mBluetoothAdapter.cancelDiscovery()
            return true
        }
        return false
    }

    /** 是否正在扫描 */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    fun isDiscovering() = mBluetoothAdapter?.isDiscovering ?: false

    /** 添加蓝牙状态监听器 */
    fun addStateListener(listener: BleStateListener) {
        if (!mBleStateListener.contains(listener)) {
            mBleStateListener.add(listener)
        }
    }

    /** 删除蓝牙状态监听器 */
    fun removeStateListener(listener: BleStateListener) {
        if (mBleStateListener.contains(listener)) {
            mBleStateListener.remove(listener)
        }
    }

    /** 添加蓝牙搜索监听器 */
    fun addDiscoveryListener(listener: BleDiscoveryListener) {
        if (!mBleDiscoveryListener.contains(listener)) {
            mBleDiscoveryListener.add(listener)
        }
    }

    /** 删除蓝牙搜索监听器 */
    fun removeDiscoveryListener(listener: BleDiscoveryListener) {
        if (mBleDiscoveryListener.contains(listener)) {
            mBleDiscoveryListener.remove(listener)
        }
    }

    /** 蓝牙广播接收器 */
    inner class BleReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) {
                return
            }
            val action = intent.action

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {// 蓝牙状态变化
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)
                for (listener in mBleStateListener) {
                    listener.onStateChange(state)
                }
            }

            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {// 蓝牙扫描过程开始
                for (listener in mBleDiscoveryListener) {
                    listener.onStartDiscovery()
                }
            }

            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {// 蓝牙扫描过程结束
                for (listener in mBleDiscoveryListener) {
                    listener.onFinishedDiscovery()
                }
            }

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {// 蓝牙扫描时，扫描到任一远程蓝牙设备时，会发送此广播
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                for (listener in mBleDiscoveryListener) {
                    listener.onFoundDevice(device)
                }
            }

            if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {// 蓝牙设备断开
                for (listener in mBleDiscoveryListener) {
                    listener.onDeviceDisconnected()
                }
            }
        }

    }
}