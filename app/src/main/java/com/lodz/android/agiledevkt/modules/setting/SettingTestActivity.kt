package com.lodz.android.agiledevkt.modules.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.widget.RadioGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.utils.isScreenBrightnessModeAutomatic
import com.lodz.android.corekt.utils.setScreenBrightnessMode
import com.lodz.android.corekt.utils.toastShort

/**
 * 设置测试类
 * Created by zhouL on 2018/7/20.
 */
class SettingTestActivity :BaseActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, SettingTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 写入设置权限请求码 */
    private val REQUEST_CODE_WRITE_SETTINGS = 77

    /** 亮度模式单选组 */
    @BindView(R.id.brightness_rg)
    lateinit var mBrightnessRadioGroup: RadioGroup




    override fun getLayoutId() = R.layout.activity_setting_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun clickBackBtn() {
        super.clickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mBrightnessRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.automatic_rb -> setScreenBrightnessMode(Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
                R.id.manual_rb -> setScreenBrightnessMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)
            }
        }
    }

    override fun initData() {
        super.initData()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// android6.0以上版本WRITE_SETTINGS需要通过startActivityForResult获取
            if (!Settings.System.canWrite(getContext())) {// 未获取到权限
                requestWriteSettingsPermission()
            }else{
                initLogic()
            }
        }else{
            initLogic()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (Settings.System.canWrite(getContext())) {
                initLogic()
            }else{// 用户未开启权限继续申请
                requestWriteSettingsPermission()
            }
        }
    }

    /** 申请设置写入权限 */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestWriteSettingsPermission(){
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = Uri.parse("package:" + packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS)
        toastShort(R.string.setting_request_permission_tips)
    }

    private fun initLogic() {
        mBrightnessRadioGroup.check(if(isScreenBrightnessModeAutomatic()) R.id.automatic_rb else R.id.manual_rb)
        showStatusCompleted()
    }

}