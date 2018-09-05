package com.lodz.android.agiledevkt.modules.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.anko.goBluetoothSetting
import com.lodz.android.corekt.ble.BleDiscoveryListener
import com.lodz.android.corekt.ble.BleSimpleHelper
import com.lodz.android.corekt.ble.BleStateListener
import com.lodz.android.corekt.utils.toastShort

/**
 * 蓝牙测试类
 * Created by zhouL on 2018/7/31.
 */
class BluetoothTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, BluetoothTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 蓝牙状态 */
    @BindView(R.id.ble_status_tv)
    lateinit var mBleStatusTv: TextView
    /** 蓝牙开关 */
    @BindView(R.id.ble_switch_btn)
    lateinit var mBleSwitchBtn: Switch

    /** 跳转蓝牙设置页 */
    @BindView(R.id.go_setting_btn)
    lateinit var mGoSettingBtn: Button
    /** 开始扫描 */
    @BindView(R.id.start_disconnected_btn)
    lateinit var mStartDisconnectedBtn: Button
    /** 停止扫描 */
    @BindView(R.id.stop_disconnected_btn)
    lateinit var mStopDisconnectedBtn: Button

    /** 设备列表 */
    @BindView(R.id.recycler_view)
    lateinit var mRecyclerView: RecyclerView
    /** 蓝牙设备适配器 */
    private lateinit var mAdapter: BleDeviceAdapter

    /** 蓝牙设备列表 */
    private val mDeviceList = ArrayList<BluetoothDevice>()

    override fun getLayoutId() = R.layout.activity_bluetooth_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mAdapter = BleDeviceAdapter(getContext())
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter
    }

    override fun clickBackBtn() {
        super.clickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        BleSimpleHelper.get().addStateListener(mBleStateListener)

        BleSimpleHelper.get().addDiscoveryListener(mBleDiscoveryListener)

        // 跳转蓝牙设置页
        mGoSettingBtn.setOnClickListener {
            goBluetoothSetting()
        }

        // 蓝牙开关
        mBleSwitchBtn.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                BleSimpleHelper.get().setBluetoothState(isChecked)
            }
        })

        // 列表点击
        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            toastShort(item.name + " - " + item.address)
        }

        // 开始扫描
        mStartDisconnectedBtn.setOnClickListener {
            if (!BleSimpleHelper.get().isBluetoothEnabled()){
                toastShort(R.string.ble_bluetooth_unopen)
                return@setOnClickListener
            }

            if (BleSimpleHelper.get().isDiscovering()){
                BleSimpleHelper.get().cancelDiscovery()
            }
            if (!BleSimpleHelper.get().startDiscovery()){
                toastShort(R.string.ble_scan_fail)
                return@setOnClickListener
            }
            mDeviceList.clear()
            mAdapter.notifyDataSetChanged()
        }

        // 停止扫描
        mStopDisconnectedBtn.setOnClickListener {
            if (!BleSimpleHelper.get().isBluetoothEnabled()){
                toastShort(R.string.ble_bluetooth_unopen)
                return@setOnClickListener
            }
            BleSimpleHelper.get().cancelDiscovery()
        }
    }

    /** 蓝牙状态监听器 */
    private val mBleStateListener = object : BleStateListener {

        override fun onStateChange(state: Int) {
            mBleStatusTv.text = getString(R.string.ble_status).format(getBleTips(state))
            setSwitchByState(state)
        }

    }

    /** 蓝牙搜索监听器 */
    private val mBleDiscoveryListener = object : BleDiscoveryListener {
        override fun onStartDiscovery() {
            mBleStatusTv.text = getString(R.string.ble_status).format(getString(R.string.ble_scanning))
        }

        override fun onFinishedDiscovery() {
            mBleStatusTv.text = getString(R.string.ble_status).format(getString(R.string.ble_finish_discovery))
        }

        override fun onFoundDevice(device: BluetoothDevice) {
            if (mDeviceList.contains(device)){
                return
            }
            mDeviceList.add(device)
            mAdapter.setData(mDeviceList)
            mAdapter.notifyDataSetChanged()
        }

        override fun onDeviceDisconnected() {
            mBleStatusTv.text = getString(R.string.ble_status).format(getString(R.string.ble_device_disconnected))
        }

    }

    override fun initData() {
        super.initData()
        BleSimpleHelper.get().registerBleReceiver(getContext())
        val state = BleSimpleHelper.get().getBluetoothState()
        mBleStatusTv.text = getString(R.string.ble_status).format(getBleTips(state))
        setSwitchByState(state)
        showStatusCompleted()
    }

    /** 根据蓝牙状态[state]获取提示语 */
    private fun getBleTips(state: Int) = when (state) {
        BluetoothAdapter.STATE_OFF -> getString(R.string.ble_off)
        BluetoothAdapter.STATE_TURNING_OFF -> getString(R.string.ble_turning_off)
        BluetoothAdapter.STATE_ON -> getString(R.string.ble_on)
        BluetoothAdapter.STATE_TURNING_ON -> getString(R.string.ble_turning_on)
        else -> getString(R.string.ble_unknown)
    }

    /** 根据蓝牙状态[state]设置开关 */
    private fun setSwitchByState(state: Int) {
        if (state == BluetoothAdapter.STATE_OFF) {
            mBleSwitchBtn.isChecked = false
            return
        }
        if (state == BluetoothAdapter.STATE_ON) {
            mBleSwitchBtn.isChecked = true
            return
        }
    }

    override fun finish() {
        BleSimpleHelper.get().unregisterBleReceiver(getContext())
        BleSimpleHelper.get().removeStateListener(mBleStateListener)
        BleSimpleHelper.get().removeDiscoveryListener(mBleDiscoveryListener)
        super.finish()
    }



}