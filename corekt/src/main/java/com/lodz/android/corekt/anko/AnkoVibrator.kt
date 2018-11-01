package com.lodz.android.corekt.anko

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

/**
 * 手机震动扩展类
 * Created by zhouL on 2018/7/2.
 */

/** 手机硬件是否有震动器 */
fun Context.hasVibrator(): Boolean = (getSystemService(Service.VIBRATOR_SERVICE) as Vibrator).hasVibrator()

/** 创建一个手机震动器对象，震动时长[milliseconds]毫秒 */
@SuppressLint("MissingPermission")
fun Context.createVibrator(milliseconds: Long): Vibrator {
    val vibrator = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(milliseconds)
    }
    return vibrator
}

/** 创建一个手机震动器对象，
 * 自定义震动模式[pattern]，数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长...] 时长的单位是毫秒。
 * 是否反复震动[isRepeat]，如果是true，反复震动，需要代码取消，如果是false，只震动一次
 */
@SuppressLint("MissingPermission")
fun Context.createVibrator(pattern: LongArray, isRepeat: Boolean): Vibrator {
    val vibrator = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
    val repeatFlag = if (isRepeat) 1 else -1
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, repeatFlag))
    } else {
        vibrator.vibrate(pattern, repeatFlag)
    }
    return vibrator
}





