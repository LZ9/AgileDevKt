package com.lodz.android.agiledevkt.modules.rxjava.utils

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

/**
 * Rx缓存获取
 * Created by zhouL on 2019/1/7.
 */
class RxCache private constructor(private val memorySuccess: Boolean, private val diskSuccess: Boolean, private val networkSuccess: Boolean) {

    companion object {
        fun create(memorySuccess: Boolean, diskSuccess: Boolean, networkSuccess: Boolean): RxCache =
                RxCache(memorySuccess, diskSuccess, networkSuccess)
    }

    fun requestData(): Observable<String> = Observable
            .zip(memory(), disk(), BiFunction<String, String, Observable<String>> { memory, disk ->

                val cache = if (memory.isNotEmpty()) {
                    memory
                } else if (disk.isNotEmpty()) {
                    disk
                } else {
                    "暂无缓存数据"
                }
                return@BiFunction Observable.concat(Observable.just(cache), network())
            })
            .flatMap { result ->
                return@flatMap result
            }


    private fun memory(): Observable<String> = Observable.just(memorySuccess)
            .delay(100, TimeUnit.MILLISECONDS)
            .map { isSuccess ->
                return@map if (isSuccess) "内存缓存：${Thread.currentThread().name}" else ""
            }

    private fun disk(): Observable<String> = Observable.just(diskSuccess)
            .delay(200, TimeUnit.MILLISECONDS)
            .map { isSuccess ->
                return@map if (isSuccess) "磁盘缓存：${Thread.currentThread().name}" else ""
            }

    private fun network(): Observable<String> = Observable.just(networkSuccess)
            .delay(2, TimeUnit.SECONDS)
            .map { isSuccess ->
                return@map if (isSuccess) "网络请求：${Thread.currentThread().name}" else "网络请求：获取失败"
            }
}