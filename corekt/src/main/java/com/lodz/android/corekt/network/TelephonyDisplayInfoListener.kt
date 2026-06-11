package com.lodz.android.corekt.network

import android.os.Build
import android.telephony.TelephonyCallback
import androidx.annotation.RequiresApi

/**
 * 电话显示信息监听器
 * @author zhouL
 * @date 2026/6/11
 */
@RequiresApi(Build.VERSION_CODES.S)
abstract class TelephonyDisplayInfoListener : TelephonyCallback(), TelephonyCallback.DisplayInfoListener