package com.lodz.android.agiledevkt.modules.info

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.network.NetInfo
import com.lodz.android.corekt.network.NetworkManager
import com.lodz.android.corekt.utils.*

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
    @BindView(R.id.ua_tv)
    lateinit var mUaTv: TextView

    /** 语言 */
    @BindView(R.id.language_tv)
    lateinit var mLanguageTv: TextView
    /** 国家 */
    @BindView(R.id.country_tv)
    lateinit var mCountryTv: TextView

    /** 品牌 */
    @BindView(R.id.brand_tv)
    lateinit var mBrandTv: TextView
    /** 型号 */
    @BindView(R.id.model_tv)
    lateinit var mModelTv: TextView
    /** 版本 */
    @BindView(R.id.board_tv)
    lateinit var mBoardTv: TextView
    /** CPU1 */
    @BindView(R.id.cpu_abi_tv)
    lateinit var mCpuAbiTv: TextView
    /** CPU2 */
    @BindView(R.id.cpu_abi2_tv)
    lateinit var mCpuAbi2Tv: TextView
    /** 制造商 */
    @BindView(R.id.manufacturer_tv)
    lateinit var mManufacturerTv: TextView
    /** 产品 */
    @BindView(R.id.product_tv)
    lateinit var mProductTv: TextView
    /** 设备 */
    @BindView(R.id.device_tv)
    lateinit var mDeviceTv: TextView

    /** IMEI1 */
    @BindView(R.id.imei1_tv)
    lateinit var mImei1Tv: TextView
    /** IMEI2 */
    @BindView(R.id.imei2_tv)
    lateinit var mImei2Tv: TextView
    /** 双卡双待 */
    @BindView(R.id.dual_sim_tv)
    lateinit var mDualSimTv: TextView
    /** IMSI1 */
    @BindView(R.id.imsi1_tv)
    lateinit var mImsi1Tv: TextView
    /** sim1可用 */
    @BindView(R.id.sim1_ready_tv)
    lateinit var mSim1ReadyTv: TextView
    /** IMSI2 */
    @BindView(R.id.imsi2_tv)
    lateinit var mImsi2Tv: TextView
    /** sim2可用 */
    @BindView(R.id.sim2_ready_tv)
    lateinit var mSim2ReadyTv: TextView
    /** sim卡数据连接状态 */
    @BindView(R.id.sim_data_state_tv)
    lateinit var mSimDataStateTv: TextView
    /** sim卡是否已连接数据 */
    @BindView(R.id.sim_connected_data_tv)
    lateinit var mSimConnectedDataTv: TextView
    /** APN名称 */
    @BindView(R.id.apn_name_tv)
    lateinit var mApnNameTv: TextView


    override fun getLayoutId() = R.layout.activity_info_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun clickBackBtn() {
        super.clickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
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

        showStatusCompleted()
    }

    override fun finish() {
        NetworkManager.get().removeNetworkListener(mNetworkListener)
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
}