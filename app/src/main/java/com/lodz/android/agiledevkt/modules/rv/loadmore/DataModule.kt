package com.lodz.android.agiledevkt.modules.rv.loadmore

import com.lodz.android.pandora.rx.utils.doComplete
import com.lodz.android.pandora.rx.utils.doError
import com.lodz.android.pandora.rx.utils.doNext
import io.reactivex.rxjava3.core.Observable

/**
 * 数据模块
 * Created by zhouL on 2018/11/27.
 */
class DataModule private constructor() {

    companion object {
        fun get(): DataModule = DataModule()
    }

    fun requestData(page: Int): Observable<List<String>> =
        Observable.create { emitter ->
            try {
                Thread.sleep(1000)
                emitter.doNext(getList(page))
                emitter.doComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.doError(e)
            }
        }

    private fun getList(page: Int): List<String> {
        val list = ArrayList<String>()
        for (i in 0 until 10) {
            val data = "第${page}页 - ${i + 1}号小姐姐"
            list.add(data)
        }
        return list
    }

}