package com.lodz.android.pandora.rx.utils

import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe

/**
 * 带入参的SingleOnSubscribe
 * Created by zhouL on 2019/1/18.
 */
abstract class RxSingleOnSubscribe<T>(vararg args: Any?) : SingleOnSubscribe<T> {

    private var mArgs: Array<out Any?> = args

    fun getArgs(): Array<out Any?> = mArgs

    fun doSuccess(emitter: SingleEmitter<T>, t: T) {
        if (!emitter.isDisposed) {
            emitter.onSuccess(t)
        }
    }

    fun doError(emitter: SingleEmitter<T>, error: Throwable) {
        if (!emitter.isDisposed) {
            emitter.onError(error)
        }
    }
}