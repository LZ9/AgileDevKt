package com.lodz.android.agiledevkt.modules.info

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.*
import com.lodz.android.corekt.network.NetInfo
import com.lodz.android.corekt.network.NetworkManager
import com.lodz.android.corekt.utils.*
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import io.reactivex.Observable

/**
 * 信息展示测试类
 * Created by zhouL on 2018/7/27.
 */
class InfoTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, InfoTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 手机UA */
    private val mUaTv by bindView<TextView>(R.id.ua_tv)

    /** 语言 */
    private val mLanguageTv by bindView<TextView>(R.id.language_tv)
    /** 国家 */
    private val mCountryTv by bindView<TextView>(R.id.country_tv)

    /** 品牌 */
    private val mBrandTv by bindView<TextView>(R.id.brand_tv)
    /** 型号 */
    private val mModelTv by bindView<TextView>(R.id.model_tv)
    /** 版本 */
    private val mBoardTv by bindView<TextView>(R.id.board_tv)
    /** CPU1 */
    private val mCpuAbiTv by bindView<TextView>(R.id.cpu_abi_tv)
    /** CPU2 */
    private val mCpuAbi2Tv by bindView<TextView>(R.id.cpu_abi2_tv)
    /** 制造商 */
    private val mManufacturerTv by bindView<TextView>(R.id.manufacturer_tv)
    /** 产品 */
    private val mProductTv by bindView<TextView>(R.id.product_tv)
    /** 设备 */
    private val mDeviceTv by bindView<TextView>(R.id.device_tv)

    /** IMEI1 */
    private val mImei1Tv by bindView<TextView>(R.id.imei1_tv)
    /** IMEI2 */
    private val mImei2Tv by bindView<TextView>(R.id.imei2_tv)
    /** 双卡双待 */
    private val mDualSimTv by bindView<TextView>(R.id.dual_sim_tv)
    /** IMSI1 */
    private val mImsi1Tv by bindView<TextView>(R.id.imsi1_tv)
    /** sim1可用 */
    private val mSim1ReadyTv by bindView<TextView>(R.id.sim1_ready_tv)
    /** IMSI2 */
    private val mImsi2Tv by bindView<TextView>(R.id.imsi2_tv)
    /** sim2可用 */
    private val mSim2ReadyTv by bindView<TextView>(R.id.sim2_ready_tv)
    /** sim卡数据连接状态 */
    private val mSimDataStateTv by bindView<TextView>(R.id.sim_data_state_tv)
    /** sim卡是否已连接数据 */
    private val mSimConnectedDataTv by bindView<TextView>(R.id.sim_connected_data_tv)
    /** APN名称 */
    private val mApnNameTv by bindView<TextView>(R.id.apn_name_tv)

    /** 屏幕高度 */
    private val mScreenHeightTv by bindView<TextView>(R.id.screen_height_tv)
    /** 屏幕宽度 */
    private val mScreenWidthTv by bindView<TextView>(R.id.screen_width_tv)
    /** 状态栏高度 */
    private val StatusBarHeightTv by bindView<TextView>(R.id.status_bar_height_tv)
    /** 是否存在虚拟按键 */
    private val mHasNavigationBarTv by bindView<TextView>(R.id.has_navigation_bar_tv)
    /** 虚拟按键高度 */
    private val mNavigationBarHeightTv by bindView<TextView>(R.id.navigation_bar_height_tv)

    /** 是否主线程 */
    private val mMainThreadTv by bindView<TextView>(R.id.main_thread_tv)
    /** 随机UUID */
    private val mUuidTv by bindView<TextView>(R.id.uuid_tv)
    /** 应用名称 */
    private val mAppNameTv by bindView<TextView>(R.id.app_name_tv)
    /** 版本名称 */
    private val mVersionNameTv by bindView<TextView>(R.id.version_name_tv)
    /** 版本号 */
    private val mVersionCodeTv by bindView<TextView>(R.id.version_code_tv)
    /** 是否在主进程 */
    private val mMainProcessTv by bindView<TextView>(R.id.main_process_tv)
    /** 当前进程名称 */
    private val mProcessNameTv by bindView<TextView>(R.id.process_name_tv)
    /** 是否安装QQ */
    private val mQQInstalledTv by bindView<TextView>(R.id.qq_installed_tv)
    /** 是否安装微信 */
    private val mWechatInstalledTv by bindView<TextView>(R.id.wechat_installed_tv)
    /** 安装的应用数量 */
    private val mInstalledAppNumTv by bindView<TextView>(R.id.installed_app_num_tv)
    /** GPS是否打开 */
    private val mGpsOpenTv by bindView<TextView>(R.id.is_gps_open_tv)
    /** assets内文本内容 */
    private val mAssetsTextTv by bindView<TextView>(R.id.assets_text_tv)

    /** 内部存储路径 */
    private val mInternalStoragePathTv by bindView<TextView>(R.id.internal_storage_path_tv)
    /** 外部存储路径 */
    private val mExternalStoragePathTv by bindView<TextView>(R.id.external_storage_path_tv)


    /** GPS开关广播接收器*/
    private val mGpsBroadcastReceiver = GpsBroadcastReceiver()

    override fun getLayoutId() = R.layout.activity_info_test

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        showDeviceInfo()
        showScreenInfo()
        showAppInfo()
        showStorage()
        showStatusCompleted()
    }

    /** 显示设备信息 */
    private fun showDeviceInfo() {
        NetworkManager.get().addNetworkListener(mNetworkListener)
        mUaTv.text = getString(R.string.info_phone_ua).format(DeviceUtils.getUserAgent())
        mLanguageTv.text = getString(R.string.info_phone_language).format(DeviceUtils.getLanguage())
        mCountryTv.text = getString(R.string.info_phone_country).format(DeviceUtils.getCountry())

        mBrandTv.text = getString(R.string.info_phone_brand).format(DeviceUtils.getDeviceValue(DeviceUtils.BRAND))
        mModelTv.text = getString(R.string.info_phone_model).format(DeviceUtils.getDeviceValue(DeviceUtils.MODEL))
        mBoardTv.text = getString(R.string.info_phone_board).format(DeviceUtils.getDeviceValue(DeviceUtils.BOARD))
        mCpuAbiTv.text = getString(R.string.info_phone_cpu_abi).format(DeviceUtils.getDeviceValue(DeviceUtils.CPU_ABI))
        mCpuAbi2Tv.text = getString(R.string.info_phone_cpu_abi2).format(DeviceUtils.getDeviceValue(DeviceUtils.CPU_ABI2))
        mManufacturerTv.text = getString(R.string.info_phone_manufacturer).format(DeviceUtils.getDeviceValue(DeviceUtils.MANUFACTURER))
        mProductTv.text = getString(R.string.info_phone_product).format(DeviceUtils.getDeviceValue(DeviceUtils.PRODUCT))
        mDeviceTv.text = getString(R.string.info_phone_device).format(DeviceUtils.getDeviceValue(DeviceUtils.DEVICE))

        mImei1Tv.text = getString(R.string.info_phone_imei1).format(getIMEI1())
        mImei2Tv.text = getString(R.string.info_phone_imei2).format(getIMEI2())
        mDualSimTv.text = getString(R.string.info_is_dual_sim).format(isDualSim().toString())
        mImsi1Tv.text = getString(R.string.info_phone_imsi1).format(getIMSI1())
        mSim1ReadyTv.text = getString(R.string.info_phone_sim1_ready).format(isSim1Ready())
        mImsi2Tv.text = getString(R.string.info_phone_imsi2).format(getIMSI2())
        mSim2ReadyTv.text = getString(R.string.info_phone_sim2_ready).format(isSim2Ready())
        mSimDataStateTv.text = getString(R.string.info_phone_sim_data_state).format(getSimDataState())
        mSimConnectedDataTv.text = getString(R.string.info_phone_is_connected_data).format(isSimDataConnected())
        mApnNameTv.text = getString(R.string.info_apn_name).format(getApnName())
    }

    /** 显示屏幕信息 */
    private fun showScreenInfo() {
        mScreenHeightTv.text = getString(R.string.info_screen_height).format(getScreenHeight())
        mScreenWidthTv.text = getString(R.string.info_screen_width).format(getScreenWidth())

        StatusBarHeightTv.text = getString(R.string.info_status_bar_height).format(getStatusBarHeight())
        mHasNavigationBarTv.text = getString(R.string.info_has_navigation_bar).format(hasNavigationBar())
        mNavigationBarHeightTv.text = getString(R.string.info_navigation_bar_height).format(getNavigationBarHeight())
    }

    /** 显示应用信息 */
    private fun showAppInfo() {
        mMainThreadTv.text = getString(R.string.info_is_main_thread).format(AppUtils.isMainThread())
        mUuidTv.text = getString(R.string.info_uuid).format(AppUtils.getUUID())
        mAppNameTv.text = getString(R.string.info_app_name).format(getAppName())
        mVersionNameTv.text = getString(R.string.info_version_name).format(getVersionName())
        mVersionCodeTv.text = getString(R.string.info_version_code).format(getVersionCode())
        mMainProcessTv.text = getString(R.string.info_is_main_process).format(isMainProcess())
        mProcessNameTv.text = getString(R.string.info_process_name).format(getProcessName())
        mQQInstalledTv.text = getString(R.string.info_is_qq_installed).format(isPkgInstalled("com.tencent.mobileqq"))
        mWechatInstalledTv.text = getString(R.string.info_is_wechat_installed).format(isPkgInstalled("com.tencent.mm"))
        mInstalledAppNumTv.text = getString(R.string.info_installed_app_num).format(getInstalledPackages().size.toString())

        registerGpsReceiver()
        mGpsOpenTv.text = getString(R.string.info_is_gps_open).format(isGpsOpen())

        Observable.just("test.txt")
                .map { fileName ->
                    getAssetsFileContent(fileName)
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<String>() {
                    override fun onBaseNext(any: String) {
                        mAssetsTextTv.text = getString(R.string.info_get_assets_text).format(any)
                    }

                    override fun onBaseError(e: Throwable) {
                        mAssetsTextTv.text = getString(R.string.info_get_assets_text).format(e.cause)
                    }

                })
    }

    /** 显示存储信息 */
    private fun showStorage() {
        val pathPair = getStoragePath()
        val internal = pathPair.first ?: ""
        val external = pathPair.second ?: ""

        mInternalStoragePathTv.text = getString(R.string.info_internal_storage_path).format(internal)
        mExternalStoragePathTv.text = getString(R.string.info_external_storage_path).format(external)
    }

    /** 注册GPS广播接收器 */
    private fun registerGpsReceiver() {
        try {
            val fileter = IntentFilter()
            fileter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
            registerReceiver(mGpsBroadcastReceiver, fileter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun finish() {
        try {
            NetworkManager.get().removeNetworkListener(mNetworkListener)
            unregisterReceiver(mGpsBroadcastReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.finish()
    }

    /** 网络监听器 */
    private val mNetworkListener = object : NetworkManager.NetworkListener {
        override fun onNetworkStatusChanged(isNetworkAvailable: Boolean, netInfo: NetInfo) {
            mSimDataStateTv.text = getString(R.string.info_phone_sim_data_state).format(getSimDataState())
            mSimConnectedDataTv.text = getString(R.string.info_phone_is_connected_data).format(isSimDataConnected())
            mApnNameTv.text = getString(R.string.info_apn_name).format(getApnName())
        }
    }

    /** GPS广播接收器 */
    inner class GpsBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) {
                return
            }
            val action = intent.action
            if (action.isNullOrEmpty()){
                return
            }
            if (action!!.equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                mGpsOpenTv.text = getString(R.string.info_is_gps_open).format(isGpsOpen())
            }
        }

    }

}