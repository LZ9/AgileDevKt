package com.lodz.android.agiledevkt.utils.sp

import com.lodz.android.corekt.utils.SharedPreferencesUtils

/**
 * SharedPreferences管理类
 * @author zhouL
 * @date 2020/10/15
 */
object SpManager {
    /**
     * 设置凭证
     * @param account 凭证
     */
    fun setToken(account: String?) {
        SharedPreferencesUtils.get().putString(SpConfig.TOKEN, account ?: "")
    }

    /** 获取凭证  */
    fun getToken(): String {
        return SharedPreferencesUtils.get().getString(SpConfig.TOKEN, "")
    }
}