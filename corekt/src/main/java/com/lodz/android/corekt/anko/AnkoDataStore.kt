package com.lodz.android.corekt.anko

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * DataStore扩展类
 * @author zhouL
 * @date 2023/7/25
 */

/** 通过键[key]值[value]对异步存放数据 */
fun <T> DataStore<Preferences>.putDataIO(key: Preferences.Key<T>, value: T) = IoScope().launch { edit { it[key] = value } }

/** 异步存放数据，支持数据编辑 */
fun DataStore<Preferences>.putDataIO(action: (transform: MutablePreferences) -> Unit) = IoScope().launch { edit(action) }

/** 通过键[key]值[value]对存放数据 */
suspend fun <T> DataStore<Preferences>.putData(key: Preferences.Key<T>, value: T) = edit { it[key] = value }

/** 通过[key]获取数据，[defValue]为默认值 */
inline fun <reified T> DataStore<Preferences>.getDataSync(key: Preferences.Key<T>, defValue: T): T = await { getData(key, defValue) }

/** 通过[key]获取数据 */
inline fun <reified T> DataStore<Preferences>.getDataSync(key: Preferences.Key<T>): T? = awaitOrNull { getData(key) }

/** 通过[key]获取数据 */
suspend inline fun <reified T> DataStore<Preferences>.getData(key: Preferences.Key<T>): T? =
    data.map {
        it.asMap().filter { it.key == key }.toList()
    }.map {
        if (it.isEmpty()){
            return@map null
        }
        val data = it.last().second
        if (data is T) {
            return@map data
        } else {
            null
        }
    }.first()

/** 通过[key]获取数据，[defValue]为默认值 */
suspend inline fun <reified T> DataStore<Preferences>.getData(key: Preferences.Key<T>, defValue: T): T = getData(key) ?: defValue

/** 异步清空数据数据 */
fun DataStore<Preferences>.cleanIO() = IoScope().launch { edit { it.clear() } }

/** 清空数据数据 */
suspend fun DataStore<Preferences>.clean() = edit { it.clear() }