package com.lodz.android.agiledevkt.modules.mvc

import com.lodz.android.pandora.rx.exception.DataException
import com.lodz.android.pandora.rx.utils.doComplete
import com.lodz.android.pandora.rx.utils.doError
import com.lodz.android.pandora.rx.utils.doNext
import io.reactivex.Observable

/**
 * 数据
 * Created by zhouL on 2018/11/19.
 */
object ApiModuleRx {
    fun requestResult(isSuccess: Boolean): Observable<String> = Observable.create { emitter ->
        if (emitter.isDisposed) {
            return@create
        }

        try {
            Thread.sleep(1000)
            if (!isSuccess) {
                emitter.doError(DataException("request fail"))
                return@create
            }
            emitter.doNext("result is ${System.currentTimeMillis()}")
            emitter.doComplete()
        } catch (e: Exception) {
            e.printStackTrace()
            emitter.doError(e)
        }
    }
}