package com.lodz.android.agiledevkt.utils.ds

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

/**
 * DataStore配置信息
 * @author zhouL
 * @date 2023/8/4
 */
object DsConfig {

    /** 编号 */
    val KEY_ID = longPreferencesKey("key_id")

    /** 姓名 */
    val KEY_NAME = stringPreferencesKey("key_name")

    /** 年龄 */
    val KEY_AGE = intPreferencesKey("key_age")

    /** 身高 */
    val KEY_HEIGHT = floatPreferencesKey("key_height")

    /** 是否研究生 */
    val KEY_IS_POSTGRADUATE = booleanPreferencesKey("key_is_postgraduate")

    /** 薪资 */
    val KEY_SALARY = doublePreferencesKey("key_salary")

    /** 兴趣爱好 */
    val KEY_HOBBY = stringSetPreferencesKey("key_hobby")
}