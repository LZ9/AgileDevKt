package com.lodz.android.pandora.rx.utils

import io.reactivex.rxjava3.core.Flowable

/**
 * Rx转换代理
 * @author zhouL
 * @date 2019/12/12
 */
class RxFlAgent<T : Any>(private val data: Flowable<T>) {

    fun rx(): Flowable<T> = data

    fun sync(): T = data.blockingFirst()

}