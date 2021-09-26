package com.lodz.android.agiledevkt.modules.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityBluetoothTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.splash.CheckDialog
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.anko.goBluetoothSetting
import com.lodz.android.corekt.anko.isPermissionGranted
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.ble.BleDiscoveryListener
import com.lodz.android.corekt.ble.BleSimpleHelper
import com.lodz.android.corekt.ble.BleStateListener
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.LocationPermission
import permissions.dispatcher.ktx.constructLocationPermissionRequest

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

    private val mBinding: ActivityBluetoothTestBinding by bindingLayout(ActivityBluetoothTestBinding::inflate)

    /** 申请定位权限 */
    private val hasFinePermissions by lazy {
        constructLocationPermissionRequest(
            LocationPermission.FINE,//后台定位
            onShowRationale = ::onShowRationaleBeforeRequest,
            onPermissionDenied = ::onDenied,
            onNeverAskAgain = ::onNeverAskAgain,
            requiresPermission = ::onRequestPermission
        )
    }

    /** 蓝牙设备适配器 */
    private lateinit var mAdapter: BleDeviceAdapter

    /** 蓝牙设备列表 */
    private val mDeviceList = ArrayList<BluetoothDevice>()

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        mAdapter = BleDeviceAdapter(getContext())
        mBinding.recyclerView.layoutManager = layoutManager
        mBinding.recyclerView.setHasFixedSize(true)
        mBinding.recyclerView.adapter = mAdapter
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        BleSimpleHelper.get().addStateListener(mBleStateListener)

        BleSimpleHelper.get().addDiscoveryListener(mBleDiscoveryListener)

        // 跳转蓝牙设置页
        mBinding.goSettingBtn.setOnClickListener {
            goBluetoothSetting()
        }

        // 蓝牙开关
        mBinding.bleSwitchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            BleSimpleHelper.get().setBluetoothState(isChecked)
        }

        // 列表点击
        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            toastShort(item.name + " - " + item.address)
        }

        // 开始扫描
        mBinding.startDisconnectedBtn.setOnClickListener {
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
        mBinding.stopDisconnectedBtn.setOnClickListener {
            if (!BleSimpleHelper.get().isBluetoothEnabled()){
                toastShort(R.string.ble_bluetooth_unopen)
                return@setOnClickListener
            }
            BleSimpleHelper.get().cancelDiscovery()
        }
    }

    /** 蓝牙状态监听器 */
    private val mBleStateListener = BleStateListener { state ->
        mBinding.bleStatusTv.text = getString(R.string.ble_status).format(getBleTips(state))
        setSwitchByState(state)
    }

    /** 蓝牙搜索监听器 */
    private val mBleDiscoveryListener = object : BleDiscoveryListener {
        override fun onStartDiscovery() {
            mBinding.bleStatusTv.text = getString(R.string.ble_status).format(getString(R.string.ble_scanning))
        }

        override fun onFinishedDiscovery() {
            mBinding.bleStatusTv.text = getString(R.string.ble_status).format(getString(R.string.ble_finish_discovery))
        }

        override fun onFoundDevice(device: BluetoothDevice?) {
            if (device == null){
                return
            }
            if (mDeviceList.contains(device)){
                return
            }
            mDeviceList.add(device)
            mAdapter.setData(mDeviceList)
            mAdapter.notifyDataSetChanged()
        }

        override fun onDeviceDisconnected() {
            mBinding.bleStatusTv.text = getString(R.string.ble_status).format(getString(R.string.ble_device_disconnected))
        }
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        onRequestPermission()
    }

    override fun initData() {
        super.initData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0以上的手机对权限进行动态申请
            onRequestPermission()//申请权限
        } else {
            initLogic()
        }
    }

    private fun initLogic() {
        BleSimpleHelper.get().registerBleReceiver(getContext())
        val state = BleSimpleHelper.get().getBluetoothState()
        mBinding.bleStatusTv.text = getString(R.string.ble_status).format(getBleTips(state))
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
            mBinding.bleSwitchBtn.isChecked = false
            return
        }
        if (state == BluetoothAdapter.STATE_ON) {
            mBinding.bleSwitchBtn.isChecked = true
            return
        }
    }

    override fun finish() {
        BleSimpleHelper.get().unregisterBleReceiver(getContext())
        BleSimpleHelper.get().removeStateListener(mBleStateListener)
        BleSimpleHelper.get().removeDiscoveryListener(mBleDiscoveryListener)
        super.finish()
    }

    /** 权限申请成功 */
    private fun onRequestPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            hasFinePermissions.launch()
            return
        }
        initLogic()
    }

    /** 用户拒绝后再次申请前告知用户为什么需要该权限 */
    private fun onShowRationaleBeforeRequest(request: PermissionRequest) {
        request.proceed()//请求权限
    }

    /** 被拒绝 */
    private fun onDenied() {
        toastShort(R.string.location_denied_permission_tips)
        showStatusError()
    }

    /** 被拒绝并且勾选了不再提醒 */
    private fun onNeverAskAgain() {
        toastShort(R.string.location_check_permission_tips)
        showPermissionCheckDialog()
        goAppDetailSetting()
    }

    /** 显示权限核对弹框 */
    private fun showPermissionCheckDialog() {
        val checkDialog = CheckDialog(getContext())
        checkDialog.setContentMsg(R.string.location_check_permission_title)
        checkDialog.setPositiveText(R.string.splash_check_permission_confirm) { dialog, which ->
            onRequestPermission()
            dialog.dismiss()
        }
        checkDialog.setNegativeText(R.string.splash_check_permission_unconfirmed) { dialog, which ->
            goAppDetailSetting()
        }
        checkDialog.setCanceledOnTouchOutside(false)
        checkDialog.setOnCancelListener {
            finish()
        }
        checkDialog.show()
    }

}