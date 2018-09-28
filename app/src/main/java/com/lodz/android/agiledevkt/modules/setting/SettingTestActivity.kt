package com.lodz.android.agiledevkt.modules.setting

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.widget.*
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.componentkt.rx.subscribe.observer.BaseObserver
import com.lodz.android.componentkt.rx.utils.RxUtils
import com.lodz.android.corekt.anko.*
import com.lodz.android.corekt.utils.toastShort
import io.reactivex.Observable

/**
 * 设置测试类
 * Created by zhouL on 2018/7/20.
 */
class SettingTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SettingTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 写入设置权限请求码 */
    private val REQUEST_CODE_WRITE_SETTINGS = 77

    /** 亮度模式单选组 */
    private val mBrightnessRadioGroup by bindView<RadioGroup>(R.id.brightness_rg)
    /** 系统亮度值 */
    private val mSystemBrightnessTv by bindView<TextView>(R.id.system_brightness_tv)
    /** 系统亮度值拖拽条 */
    private val mSystemBrightnessSeekBar by bindView<SeekBar>(R.id.system_brightness_sb)
    /** 窗口亮度值 */
    private val mWindowBrightnessTv by bindView<TextView>(R.id.window_brightness_tv)
    /** 窗口亮度值拖拽条 */
    private val mWindowBrightnessSeekBar by bindView<SeekBar>(R.id.window_brightness_sb)

    /** 屏幕休眠时间 */
    private val mScreenDdormantTimeTv by bindView<TextView>(R.id.screen_dormant_time_tv)
    /** 刷新屏幕休眠时间 */
    private val mRefreshScreenDdormantTimeBtn by bindView<Button>(R.id.refresh_screen_dormant_time_btn)
    /** 屏幕休眠时间输入框 */
    private val mScreenDdormantTimeEdit by bindView<EditText>(R.id.screen_dormant_time_edit)
    /** 屏幕休眠时间设置按钮 */
    private val mScreenDdormantTimeSettingBtn by bindView<Button>(R.id.screen_dormant_time_setting_btn)

    /** 铃声音量值 */
    private val mRingVolumeTv by bindView<TextView>(R.id.ring_volume_tv)
    /** 铃声音量值拖拽条 */
    private val mRingVolumeSeekBar by bindView<SeekBar>(R.id.ring_volume_sb)

    /** 亮度监听器 */
    private val mBrightnessObserver = BrightnessObserver()
    /** 屏幕休眠时间变化监听器 */
    private val mScreenDdormantTimeObserver = ScreenDdormantTimeObserver()
    /** 铃声音量变化监听器 */
    private val mRingVolumeObserver = RingVolumeObserver()

    override fun getLayoutId() = R.layout.activity_setting_test

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 亮度模式单选组
        mBrightnessRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.automatic_rb -> setAutomaticMode()
                R.id.manual_rb -> setManualMode()
            }
        }

        // 系统亮度值拖拽条
        mSystemBrightnessSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mSystemBrightnessSeekBar.progress = progress
                if (fromUser) {
                    setScreenBrightness(progress)
                    return
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 窗口亮度值拖拽条
        mWindowBrightnessSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    return
                }
                mWindowBrightnessSeekBar.progress = progress
                setWindowBrightness(progress)
                mWindowBrightnessTv.setText(getWindowBrightness().toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 刷新屏幕休眠时间
        mRefreshScreenDdormantTimeBtn.setOnClickListener {
            showScreenDdormantTime()
        }

        mScreenDdormantTimeSettingBtn.setOnClickListener {
            if (mScreenDdormantTimeEdit.text.isEmpty()) {
                toastShort(R.string.setting_screen_dormant_time_error)
                return@setOnClickListener
            }
            val second = mScreenDdormantTimeEdit.text.toString().toInt()
            if (second !in 15..600) {
                toastShort(R.string.setting_screen_dormant_time_error)
                return@setOnClickListener
            }
            setScreenDormantTime(second * 1000)
            showScreenDdormantTime()
        }

        // 铃声音量值拖拽条
        mRingVolumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mRingVolumeSeekBar.progress = progress
                if (fromUser) {
                    mRingVolumeTv.text = progress.toString()
                    setRingVolume(progress)
                    return
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    /** 亮度变化监听器 */
    inner class BrightnessObserver : ContentObserver(null) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            Observable.just(getScreenBrightness())
                    .compose(RxUtils.ioToMainObservable())
                    .subscribe(object : BaseObserver<Int>() {
                        override fun onBaseNext(any: Int) {
                            mSystemBrightnessTv.setText(any.toString())
                            mSystemBrightnessSeekBar.progress = any
                        }

                        override fun onBaseError(e: Throwable) {
                        }
                    })
        }
    }

    /** 屏幕休眠时间变化监听器 */
    inner class ScreenDdormantTimeObserver : ContentObserver(null) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            Observable.just(getScreenDormantTime())
                    .compose(RxUtils.ioToMainObservable())
                    .subscribe(object : BaseObserver<Int>() {
                        override fun onBaseNext(any: Int) {
                            showScreenDdormantTime()
                        }

                        override fun onBaseError(e: Throwable) {
                        }
                    })
        }
    }

    /** 铃声音量变化监听器 */
    inner class RingVolumeObserver : ContentObserver(null) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            Observable.just(getScreenDormantTime())
                    .compose(RxUtils.ioToMainObservable())
                    .subscribe(object : BaseObserver<Int>() {
                        override fun onBaseNext(any: Int) {
                            showRingVolume()
                        }

                        override fun onBaseError(e: Throwable) {
                        }
                    })
        }
    }

    override fun initData() {
        super.initData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// android6.0以上版本WRITE_SETTINGS需要通过startActivityForResult获取
            if (!Settings.System.canWrite(getContext())) {// 未获取到权限
                requestWriteSettingsPermission()
            } else {
                initLogic()
            }
        } else {
            initLogic()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (Settings.System.canWrite(getContext())) {
                initLogic()
            } else {// 用户未开启权限继续申请
                requestWriteSettingsPermission()
            }
        }
    }

    /** 申请设置写入权限 */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestWriteSettingsPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = Uri.parse("package:" + packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS)
        toastShort(R.string.setting_request_permission_tips)
    }

    /** 初始化逻辑 */
    private fun initLogic() {
        contentResolver.registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), true, mBrightnessObserver)
        mBrightnessRadioGroup.check(if (isScreenBrightnessModeAutomatic()) R.id.automatic_rb else R.id.manual_rb)// 根据当前亮度模式设置按钮选中

        contentResolver.registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_OFF_TIMEOUT), true, mScreenDdormantTimeObserver)
        showScreenDdormantTime()

        contentResolver.registerContentObserver(Settings.System.CONTENT_URI, true, mRingVolumeObserver)
        initRingVolume()

        showStatusCompleted()
    }

    /** 设置亮度自动模式 */
    private fun setAutomaticMode() {
        setScreenBrightnessMode(Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
        mSystemBrightnessSeekBar.isEnabled = false
        mWindowBrightnessSeekBar.isEnabled = false
    }

    /** 设置亮度手动模式 */
    private fun setManualMode() {
        setScreenBrightnessMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)

        mSystemBrightnessSeekBar.isEnabled = true
        val systemValue = getScreenBrightness()
        mSystemBrightnessTv.setText(systemValue.toString())
        mSystemBrightnessSeekBar.progress = systemValue

        mWindowBrightnessSeekBar.isEnabled = true
        val windowValue = getWindowBrightness()
        mWindowBrightnessTv.setText(windowValue.toString())
        mWindowBrightnessSeekBar.progress = windowValue
    }

    private fun showScreenDdormantTime() {
        val time = getScreenDormantTime() / 1000.0f
        mScreenDdormantTimeTv.text = (time.toString() + "秒")
    }

    /** 初始化铃音 */
    private fun initRingVolume() {
        mRingVolumeSeekBar.max = getMaxRingVolume()
        showRingVolume()
    }

    /** 显示铃音 */
    private fun showRingVolume() {
        val volume = getRingVolume()
        mRingVolumeTv.text = volume.toString()
        mRingVolumeSeekBar.progress = volume
    }

    override fun finish() {
        contentResolver.unregisterContentObserver(mBrightnessObserver)
        contentResolver.unregisterContentObserver(mScreenDdormantTimeObserver)
        contentResolver.unregisterContentObserver(mRingVolumeObserver)
        super.finish()
    }
}