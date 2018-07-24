package com.lodz.android.corekt.utils

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.provider.Settings
import android.support.annotation.IntDef
import android.support.annotation.IntRange
import android.support.annotation.RequiresPermission

/**
 * 系统设置帮助类
 * Created by zhouL on 2018/7/19.
 */
object SettingUtils {

    @IntDef(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ScreenBrightnessMode

    /**
     * 获取蓝牙的状态
     * 关闭：BluetoothAdapter.STATE_OFF
     * 关闭中：BluetoothAdapter.STATE_TURNING_OFF
     * 打开：BluetoothAdapter.STATE_ON
     * 打开中：BluetoothAdapter.STATE_TURNING_ON
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    fun getBluetoothState(): Int {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter != null) {
            bluetoothAdapter.state
        }
        return BluetoothAdapter.STATE_OFF
    }

    /** 设置蓝牙状态是否启用[enable] */
    @RequiresPermission(allOf = arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN))
    fun setBluetoothState(enable: Boolean) {
        if (getBluetoothState() == BluetoothAdapter.STATE_ON && !enable) {
            BluetoothAdapter.getDefaultAdapter().disable()
        }
        if (getBluetoothState() == BluetoothAdapter.STATE_OFF && enable) {
            BluetoothAdapter.getDefaultAdapter().enable()
        }
    }
}

/** 获取系统屏幕亮度模式的状态（手动：Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL ； 自动：Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC） */
fun Context.getScreenBrightnessModeState() = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)

/** 系统屏幕亮度是否为自动模式 */
fun Context.isScreenBrightnessModeAutomatic() = getScreenBrightnessModeState() == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC

/** 设置系统屏幕亮度模式[mode]（手动：Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL ； 自动：Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC） */
fun Context.setScreenBrightnessMode(@SettingUtils.ScreenBrightnessMode mode: Int) = Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, mode)

/** 获取手动模式下的系统亮度值，范围0-255（默认值255） */
fun Context.getScreenBrightness(def: Int = 255) = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, def)

/** 设置手动模式下的系统亮度[brightness]（0-255）亮度显示的优先级：窗口>系统 */
fun Context.setScreenBrightness(@IntRange(from = 1, to = 255) brightness: Int) {
    Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness)
}

/** 获取给定Activity的窗口的亮度（0-255），如果当前未设置则返回系统的亮度值 */
fun Activity.getWindowBrightness(): Int {
    if (window == null) {
        return 255
    }
    val localLayoutParams = window.attributes
    if (localLayoutParams.screenBrightness < 0){// 小于0表示使用默认系统亮度
        return getScreenBrightness()
    }
    return (localLayoutParams.screenBrightness * 255).toInt()
}

/** 设置给定Activity的窗口的亮度[brightness]（0-255）可以看到效果，但系统的亮度属性不会改变，退出Activity后亮度恢复系统亮度值 */
fun Activity.setWindowBrightness(@IntRange(from = 1, to = 255) brightness: Int) {
    if (window == null) {
        return
    }
    val localLayoutParams = window.attributes
    localLayoutParams.screenBrightness = brightness / 255f
    window.attributes = localLayoutParams
}

/** 同时设置系统和窗口亮度[brightness]（0-255） */
fun Activity.setScreenAndWindowBrightness(@IntRange(from = 1, to = 255) brightness: Int) {
    setScreenBrightness(brightness)
    setWindowBrightness(brightness)
}

/** 获取屏幕休眠时间（单位毫秒，默认30秒） */
fun Context.getScreenDormantTime(def: Int = 30000) = Settings.System.getInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, def)

/** 设置屏幕休眠时间[millis]（毫秒） */
fun Context.setScreenDormantTime(millis: Int) {
    Settings.System.putInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, millis)
}

/** 获取飞行模式的状态 1：打开；0：关闭 */
@RequiresPermission(Manifest.permission.WRITE_SECURE_SETTINGS)
fun Context.getAirplaneModeState(def: Int = 0) = Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, def)

/** 判断飞行模式是否打开 */
@RequiresPermission(Manifest.permission.WRITE_SECURE_SETTINGS)
fun Context.isAirplaneModeOpen() = getAirplaneModeState() == 1

/** 设置飞行模式的状态是否启用[enable] */
@RequiresPermission(Manifest.permission.WRITE_SECURE_SETTINGS)
fun Context.setAirplaneMode(enable: Boolean) {
    Settings.Global.putInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, if (enable) 1 else 0)
    sendBroadcast(Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED))// 发送飞行模式已经改变广播
}

/** 获取铃声音量（一般取值范围为0-7） */
fun Context.getRingVolume() = (getSystemService(Context.AUDIO_SERVICE) as AudioManager).getStreamVolume(AudioManager.STREAM_RING)

/** 获取铃声音量最大值（一般是7） */
fun Context.getMaxRingVolume() = (getSystemService(Context.AUDIO_SERVICE) as AudioManager).getStreamMaxVolume(AudioManager.STREAM_RING)

/** 设置铃声音量[volume] */
fun Context.setRingVolume(volume: Int) {
    var fixVolume = volume
    if (fixVolume < 0){
        fixVolume = 0
    }
    if (fixVolume > getMaxRingVolume()){
        fixVolume = getMaxRingVolume()
    }
    val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.setStreamVolume(AudioManager.STREAM_RING, fixVolume, AudioManager.FLAG_PLAY_SOUND)
}
