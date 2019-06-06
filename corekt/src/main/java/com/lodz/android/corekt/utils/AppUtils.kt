package com.lodz.android.corekt.utils

import android.os.Looper
import java.util.*

/**
 * APP帮助类
 * Created by zhouL on 2018/6/26.
 */
object AppUtils {

    /** 当前是否在主线程（UI线程） */
    @JvmStatic
    fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

    /** 获取36位随机UUID */
    @JvmStatic
    fun getUUID36(): String = UUID.randomUUID().toString()

    /** 获取32位随机UUID */
    @JvmStatic
    fun getUUID32(): String = UUID.randomUUID().toString().replace("-", "")

}
