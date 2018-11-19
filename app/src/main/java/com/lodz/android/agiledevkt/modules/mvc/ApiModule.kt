package com.lodz.android.agiledevkt.modules.mvc

import com.lodz.android.componentkt.rx.exception.DataException
import com.lodz.android.componentkt.rx.utils.RxObservableOnSubscribe
import com.lodz.android.corekt.utils.UiHandler
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

/**
 * 数据
 * Created by zhouL on 2018/11/19.
 */
object ApiModule {

    fun requestResult(isSuccess: Boolean): Observable<String> {
        return Observable.create(object : RxObservableOnSubscribe<String>(isSuccess) {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                val success = getArgs()[0] as Boolean
                if (emitter.isDisposed) {
                    return
                }
                try {
                    UiHandler.postDelayed(Runnable {
                        if (emitter.isDisposed) {
                            return@Runnable
                        }
                        if (!success) {
                            emitter.onError(DataException("request fail"))
                            return@Runnable
                        }
                        emitter.onNext("result is ${System.currentTimeMillis()}")
                        emitter.onComplete()
                    }, 1000)
                } catch (e: Exception) {
                    e.printStackTrace()
                    emitter.onError(e)
                }
            }

        })
    }
}