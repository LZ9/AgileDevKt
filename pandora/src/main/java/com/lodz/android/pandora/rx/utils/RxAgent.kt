package com.lodz.android.pandora.rx.utils

import io.reactivex.rxjava3.core.Observable

/**
 * Rx转换代理
 * @author zhouL
 * @date 2019/12/12
 */
class RxAgent<T>(private val data: Observable<T>) {

    fun rx(): Observable<T> = data

    fun sync(): T = data.blockingFirst()

}