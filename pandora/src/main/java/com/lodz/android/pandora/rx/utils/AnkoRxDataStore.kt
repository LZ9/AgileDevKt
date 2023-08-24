package com.lodz.android.pandora.rx.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.lodz.android.corekt.anko.getData
import com.lodz.android.corekt.anko.putData
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.subscribe.subscriber.BaseSubscriber
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.runBlocking

/**
 * DataStore的Rx扩展类
 * @author zhouL
 * @date 2023/7/25
 */

/** 通过键[key]值[value]对存放数据 */
fun <T> DataStore<Preferences>.putDataRx(key: Preferences.Key<T>, value: T): RxFlAgent<Boolean> =
    RxFlAgent(Flowable.create({
        runBlocking {
            try {
                putData(key, value)
                it.doNext(true)
                it.doComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                it.doError(e)
            }
        }
    }, BackpressureStrategy.BUFFER))

/** 通过键[key]值[value]对存放数据 */
fun <T> DataStore<Preferences>.putDataRxAuto(key: Preferences.Key<T>, value: T) =
    putDataRx(key, value).rx().compose(RxUtils.ioToMainFlowable()).subscribe(BaseSubscriber.empty())

/** 通过键[key]值[value]对存放数据 */
inline fun <reified T : Any> DataStore<Preferences>.getDataRx(key: Preferences.Key<T>, defValue: T): RxFlAgent<T> =
    RxFlAgent(Flowable.create({
        runBlocking {
            try {
                it.doNext(getData(key) ?: defValue)
                it.doComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                it.doError(e)
            }
        }
    }, BackpressureStrategy.BUFFER))

/** 清空数据 */
fun DataStore<Preferences>.cleanRx(): RxObAgent<Boolean> =
    RxObAgent(Observable.create {
        runBlocking {
            try {
                edit { it.clear() }
                it.doNext(true)
                it.doComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                it.doError(e)
            }
        }
    })

/** 清空数据 */
fun DataStore<Preferences>.cleanRxAuto() = cleanRx().rx().subscribe(BaseObserver.empty())