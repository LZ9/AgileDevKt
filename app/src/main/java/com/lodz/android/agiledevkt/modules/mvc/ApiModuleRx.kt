package com.lodz.android.agiledevkt.modules.mvc

import com.lodz.android.corekt.anko.runOnMainDelay
import com.lodz.android.pandora.rx.exception.DataException
import com.lodz.android.pandora.rx.utils.RxObservableOnSubscribe
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import kotlinx.coroutines.GlobalScope

/**
 * 数据
 * Created by zhouL on 2018/11/19.
 */
object ApiModuleRx {

    fun requestResult(isSuccess: Boolean): Observable<String> {
        return Observable.create(object : RxObservableOnSubscribe<String>(isSuccess) {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                val success = getArgs()[0] as Boolean
                if (emitter.isDisposed) {
                    return
                }
                try {
                    GlobalScope.runOnMainDelay(1000){
                        if (emitter.isDisposed) {
                            return@runOnMainDelay
                        }
                        if (!success) {
                            emitter.onError(DataException("request fail"))
                            return@runOnMainDelay
                        }
                        emitter.onNext("result is ${System.currentTimeMillis()}")
                        emitter.onComplete()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    emitter.onError(e)
                }
            }

        })
    }
}