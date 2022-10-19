package com.lodz.android.agiledevkt.modules.info

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityInfoTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.*
import com.lodz.android.corekt.network.NetworkManager
import com.lodz.android.corekt.utils.AppUtils
import com.lodz.android.corekt.utils.DeviceUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import io.reactivex.rxjava3.core.Observable
import kotlin.random.Random

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

    /** QQ包名 */
    private val QQ_PACKAGE_NAME = "com.tencent.mobileqq"
    /** 微信包名 */
    private val WECHAT_PACKAGE_NAME = "com.tencent.mm"

    /** GPS开关广播接收器*/
    private val mGpsBroadcastReceiver = GpsBroadcastReceiver()

    private val mBinding: ActivityInfoTestBinding by bindingLayout(ActivityInfoTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.randomShowBtn.setOnClickListener {
            for (i in 1 until mBinding.rootLayout.childCount){
                mBinding.rootLayout.getChildAt(i).visibility = if (Random.nextInt(100) % 2 == 0) View.VISIBLE else View.GONE
            }
        }
    }

    override fun initData() {
        super.initData()
        showDeviceInfo()
        showScreenInfo()
        showAppInfo()
        showStorage()
        showAssetsInfo()
        showStatusCompleted()
    }

    /** 显示设备信息 */
    private fun showDeviceInfo() {
        NetworkManager.get().addNetworkListener(mNetworkListener)
        //手机UA
        mBinding.uaTv.text = getString(R.string.info_phone_ua).format(DeviceUtils.getUserAgent())
        // 语言
        mBinding.languageTv.text = getString(R.string.info_phone_language).format(DeviceUtils.getLanguage())
        // 国家
        mBinding.countryTv.text = getString(R.string.info_phone_country).format(DeviceUtils.getCountry())

        // 品牌
        mBinding.brandTv.text = getString(R.string.info_phone_brand).format(DeviceUtils.getDeviceValue(DeviceUtils.BRAND))
        // 型号
        mBinding.modelTv.text = getString(R.string.info_phone_model).format(DeviceUtils.getDeviceValue(DeviceUtils.MODEL))
        // 版本
        mBinding.boardTv.text = getString(R.string.info_phone_board).format(DeviceUtils.getDeviceValue(DeviceUtils.BOARD))
        // CPU1
        mBinding.cpuAbiTv.text = getString(R.string.info_phone_cpu_abi).format(DeviceUtils.getDeviceValue(DeviceUtils.CPU_ABI))
        // CPU2
        mBinding.cpuAbi2Tv.text = getString(R.string.info_phone_cpu_abi2).format(DeviceUtils.getDeviceValue(DeviceUtils.CPU_ABI2))
        // 产品
        mBinding.manufacturerTv.text = getString(R.string.info_phone_manufacturer).format(DeviceUtils.getDeviceValue(DeviceUtils.MANUFACTURER))
        // 制造商
        mBinding.productTv.text = getString(R.string.info_phone_product).format(DeviceUtils.getDeviceValue(DeviceUtils.PRODUCT))
        // 设备
        mBinding.deviceTv.text = getString(R.string.info_phone_device).format(DeviceUtils.getDeviceValue(DeviceUtils.DEVICE))

        // AndroidId
        mBinding.androidIdTv.text = getString(R.string.info_phone_id).format(getAndroidId())
        // IMEI1
        mBinding.imei1Tv.text = getString(R.string.info_phone_imei1).format(getIMEI1())
        // IMEI2
        mBinding.imei2Tv.text = getString(R.string.info_phone_imei2).format(getIMEI2())
        // 双卡双待
        mBinding.dualSimTv.text = getString(R.string.info_is_dual_sim).format(isDualSim().toString())
        // IMSI1
        mBinding.imsi1Tv.text = getString(R.string.info_phone_imsi1).format(getIMSI1())
        // sim1可用
        mBinding.sim1ReadyTv.text = getString(R.string.info_phone_sim1_ready).format(isSim1Ready())
        // IMSI2
        mBinding.imsi2Tv.text = getString(R.string.info_phone_imsi2).format(getIMSI2())
        // sim2可用
        mBinding.sim2ReadyTv.text = getString(R.string.info_phone_sim2_ready).format(isSim2Ready())
        // sim卡数据连接状态
        mBinding.simDataStateTv.text = getString(R.string.info_phone_sim_data_state).format(getSimDataState())
        // sim卡是否已连接数据
        mBinding.simConnectedDataTv.text = getString(R.string.info_phone_is_connected_data).format(isSimDataConnected())
        // APN名称
        mBinding.apnNameTv.text = getString(R.string.info_apn_name).format(getApnName())
    }

    /** 显示屏幕信息 */
    private fun showScreenInfo() {
        // 屏幕高度
        mBinding.screenHeightTv.text = getString(R.string.info_screen_height).format(getScreenHeight())
        // 屏幕宽度
        mBinding.screenWidthTv.text = getString(R.string.info_screen_width).format(getScreenWidth())

        // 状态栏高度
        mBinding.statusBarHeightTv.text = getString(R.string.info_status_bar_height).format(getStatusBarHeight())
        // 是否存在虚拟按键
        mBinding.hasNavigationBarTv.text = getString(R.string.info_has_navigation_bar).format(hasNavigationBar())
        // 虚拟按键高度
        mBinding.navigationBarHeightTv.text = getString(R.string.info_navigation_bar_height).format(getNavigationBarHeight())
    }

    /** 显示应用信息 */
    private fun showAppInfo() {
        // 是否主线程
        mBinding.mainThreadTv.text = getString(R.string.info_is_main_thread).format(AppUtils.isMainThread())
        // 随机UUID
        mBinding.uuidTv.text = getString(R.string.info_uuid).format(AppUtils.getUUID32())
        // 应用名称
        mBinding.appNameTv.text = getString(R.string.info_app_name).format(getAppName())
        // 版本名称
        mBinding.versionNameTv.text = getString(R.string.info_version_name).format(getVersionName())
        // 版本号
        mBinding.versionCodeTv.text = getString(R.string.info_version_code).format(getVersionCode())
        // 是否在主进程
        mBinding.mainProcessTv.text = getString(R.string.info_is_main_process).format(isMainProcess())
        // 当前进程名称
        mBinding.processNameTv.text = getString(R.string.info_process_name).format(getProcessName())

        // 是否安装QQ
        val isQQInstalled = isPkgInstalled(QQ_PACKAGE_NAME)
        mBinding.qqInstalledTv.text = getString(R.string.info_is_qq_installed).format(isQQInstalled)
        if (isQQInstalled) {
            mBinding.qqIconTv.visibility = View.VISIBLE
            mBinding.qqIconTv.setCompoundDrawablesWithIntrinsicBounds(null, getAppIcon(QQ_PACKAGE_NAME), null,null)
            mBinding.qqIconTv.text = getAppName(QQ_PACKAGE_NAME)
        } else {
            mBinding.qqIconTv.visibility = View.GONE
        }

        // 是否安装微信
        val isWechatInstalled = isPkgInstalled(WECHAT_PACKAGE_NAME)
        mBinding.wechatInstalledTv.text = getString(R.string.info_is_wechat_installed).format(isWechatInstalled)
        if (isWechatInstalled) {
            mBinding.wechatIconTv.visibility = View.VISIBLE
            mBinding.wechatIconTv.setCompoundDrawablesWithIntrinsicBounds(null, getAppIcon(WECHAT_PACKAGE_NAME), null,null)
            mBinding.wechatIconTv.text = getAppName(WECHAT_PACKAGE_NAME)
        } else {
            mBinding.wechatIconTv.visibility = View.GONE
        }
        // 安装的应用数量
        mBinding.installedAppNumTv.text = getString(R.string.info_installed_app_num).format(getInstalledPackages().size.toString())

        registerGpsReceiver()
        // GPS是否打开
        mBinding.isGpsOpenTv.text = getString(R.string.info_is_gps_open).format(isGpsOpen())
    }

    /** 显示Assets信息 */
    private fun showAssetsInfo(){
        Observable.just("test.txt")
            .map { fileName ->
                getAssetsFileContent(fileName)
            }
            .compose(RxUtils.ioToMainObservable())
            .subscribe(object : BaseObserver<String>() {
                override fun onBaseNext(any: String) {
                    mBinding.assetsTextTv.text = getString(R.string.info_get_assets_text).format(any)
                }

                override fun onBaseError(e: Throwable) {
                    mBinding.assetsTextTv.text = getString(R.string.info_get_assets_text).format(e.cause)
                }

            })
    }

    /** 显示存储信息 */
    private fun showStorage() {
        val pathPair = getStoragePath()
        val internal = pathPair.first ?: ""
        val external = pathPair.second ?: ""

        // 内部存储路径
        mBinding.internalStoragePathTv.text = getString(R.string.info_internal_storage_path).format(internal)
        // 外部存储路径
        mBinding.externalStoragePathTv.text = getString(R.string.info_external_storage_path).format(external)
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
    private val mNetworkListener = NetworkManager.NetworkListener { isNetworkAvailable, netInfo ->
        mBinding.simDataStateTv.text = getString(R.string.info_phone_sim_data_state).format(getSimDataState())
        mBinding.simConnectedDataTv.text = getString(R.string.info_phone_is_connected_data).format(isSimDataConnected())
        mBinding.apnNameTv.text = getString(R.string.info_apn_name).format(getApnName())
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
            if (action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                mBinding.isGpsOpenTv.text = getString(R.string.info_is_gps_open).format(isGpsOpen())
            }
        }

    }

}