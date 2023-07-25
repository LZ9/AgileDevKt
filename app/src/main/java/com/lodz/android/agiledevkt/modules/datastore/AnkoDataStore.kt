package com.lodz.android.agiledevkt.modules.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 * DataStore扩展类
 * @author zhouL
 * @date 2023/7/25
 */

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


//fun Context.dataStore(): DataStore<Preferences> =  preferencesDataStore(name = "settings")


///** 在Activity中绑定view */
//fun <T : View> Activity.bindView(id: Int): Lazy<T> = lazy {
//    return@lazy findViewById<T>(id) ?: throw IllegalStateException("View ID $id for '${javaClass.name}' not found.")
//}

