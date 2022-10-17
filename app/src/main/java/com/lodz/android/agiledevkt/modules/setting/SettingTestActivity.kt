package com.lodz.android.agiledevkt.modules.setting

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivitySettingTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.*
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import io.reactivex.rxjava3.core.Observable

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

    /** 亮度监听器 */
    private val mBrightnessObserver = BrightnessObserver()
    /** 屏幕休眠时间变化监听器 */
    private val mScreenDdormantTimeObserver = ScreenDdormantTimeObserver()
    /** 铃声音量变化监听器 */
    private val mRingVolumeObserver = RingVolumeObserver()

    private val mBinding: ActivitySettingTestBinding by bindingLayout(ActivitySettingTestBinding::inflate)

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

        // 亮度模式单选组
        mBinding.brightnessRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.automatic_rb -> setAutomaticMode()
                R.id.manual_rb -> setManualMode()
            }
        }

        // 系统亮度值拖拽条
        mBinding.systemBrightnessSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mBinding.systemBrightnessSb.progress = progress
                if (fromUser) {
                    setScreenBrightness(progress)
                    return
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 窗口亮度值拖拽条
        mBinding.windowBrightnessSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    return
                }
                mBinding.windowBrightnessSb.progress = progress
                setWindowBrightness(progress)
                mBinding.windowBrightnessTv.text = getWindowBrightness().toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 刷新屏幕休眠时间
        mBinding.refreshScreenDormantTimeBtn.setOnClickListener {
            showScreenDdormantTime()
        }

        mBinding.screenDormantTimeSettingBtn.setOnClickListener {
            if (mBinding.screenDormantTimeEdit.text.isEmpty()) {
                toastShort(R.string.setting_screen_dormant_time_error)
                return@setOnClickListener
            }
            val second = mBinding.screenDormantTimeEdit.text.toString().toInt()
            if (second !in 15..600) {
                toastShort(R.string.setting_screen_dormant_time_error)
                return@setOnClickListener
            }
            setScreenDormantTime(second * 1000)
            showScreenDdormantTime()
        }

        // 铃声音量值拖拽条
        mBinding.ringVolumeSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mBinding.ringVolumeSb.progress = progress
                if (fromUser) {
                    mBinding.ringVolumeTv.text = progress.toString()
                    setVolume(AudioManager.STREAM_RING, progress)
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
                            mBinding.systemBrightnessTv.text = any.toString()
                            mBinding.systemBrightnessSb.progress = any
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

    /** Activity传递回调 */
    val mWriteSettingsPermissionResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        intent.data = Uri.parse("package:$packageName")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        mWriteSettingsPermissionResult.launch(intent)
        toastShort(R.string.setting_request_permission_tips)
    }

    /** 初始化逻辑 */
    private fun initLogic() {
        contentResolver.registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), true, mBrightnessObserver)
        mBinding.brightnessRg.check(if (isScreenBrightnessModeAutomatic()) R.id.automatic_rb else R.id.manual_rb)// 根据当前亮度模式设置按钮选中

        contentResolver.registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_OFF_TIMEOUT), true, mScreenDdormantTimeObserver)
        showScreenDdormantTime()

        contentResolver.registerContentObserver(Settings.System.CONTENT_URI, true, mRingVolumeObserver)
        initRingVolume()

        showStatusCompleted()
    }

    /** 设置亮度自动模式 */
    private fun setAutomaticMode() {
        setScreenBrightnessMode(Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
        mBinding.systemBrightnessSb.isEnabled = false
        mBinding.windowBrightnessSb.isEnabled = false
    }

    /** 设置亮度手动模式 */
    private fun setManualMode() {
        setScreenBrightnessMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)

        mBinding.systemBrightnessSb.isEnabled = true
        val systemValue = getScreenBrightness()
        mBinding.systemBrightnessTv.text = systemValue.toString()
        mBinding.systemBrightnessSb.progress = systemValue

        mBinding.windowBrightnessSb.isEnabled = true
        val windowValue = getWindowBrightness()
        mBinding.windowBrightnessTv.text = windowValue.toString()
        mBinding.windowBrightnessSb.progress = windowValue
    }

    private fun showScreenDdormantTime() {
        val time = getScreenDormantTime() / 1000.0f
        mBinding.screenDormantTimeTv.text = (time.toString() + "秒")
    }

    /** 初始化铃音 */
    private fun initRingVolume() {
        mBinding.ringVolumeSb.max = getMaxVolume(AudioManager.STREAM_RING)
        showRingVolume()
    }

    /** 显示铃音 */
    private fun showRingVolume() {
        val volume = getVolume(AudioManager.STREAM_RING)
        mBinding.ringVolumeTv.text = volume.toString()
        mBinding.ringVolumeSb.progress = volume
    }

    override fun finish() {
        contentResolver.unregisterContentObserver(mBrightnessObserver)
        contentResolver.unregisterContentObserver(mScreenDdormantTimeObserver)
        contentResolver.unregisterContentObserver(mRingVolumeObserver)
        super.finish()
    }
}