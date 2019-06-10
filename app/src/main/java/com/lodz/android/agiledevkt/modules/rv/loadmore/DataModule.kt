package com.lodz.android.agiledevkt.modules.rv.loadmore

import com.lodz.android.pandora.rx.utils.RxObservableOnSubscribe
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import java.util.*

/**
 * 数据模块
 * Created by zhouL on 2018/11/27.
 */
class DataModule private constructor() {

    companion object {
        fun get(): DataModule = DataModule()
    }

    fun requestData(page: Int): Observable<List<String>> =
            Observable.create(object : RxObservableOnSubscribe<List<String>>(page) {
                override fun subscribe(emitter: ObservableEmitter<List<String>>) {
                    val pages = getArgs()[0] as Int
                    try {

                        Thread.sleep(1000)
                        if (emitter.isDisposed) {
                            return
                        }
                        emitter.onNext(getList(pages))
                        emitter.onComplete()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        emitter.onError(e)
                    }
                }
            })

    private fun getList(page: Int): List<String> {
        val list = ArrayList<String>()
        for (i in 0 until 20) {
            val data = "第${page}页 - ${i + 1}号小姐姐"
            list.add(data)
        }
        return list
    }

}