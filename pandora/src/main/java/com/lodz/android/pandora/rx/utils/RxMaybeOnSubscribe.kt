package com.lodz.android.pandora.rx.utils

import io.reactivex.MaybeEmitter
import io.reactivex.MaybeOnSubscribe

/**
 * 带入参的MaybeOnSubscribe
 * Created by zhouL on 2019/1/21.
 */
abstract class RxMaybeOnSubscribe<T>(vararg args: Any?) : MaybeOnSubscribe<T> {

    private var mArgs: Array<out Any?>

    init {
        mArgs = args
    }

    fun getArgs(): Array<out Any?> = mArgs

    fun doSuccess(emitter: MaybeEmitter<T>, t: T) {
        if (!emitter.isDisposed) {
            emitter.onSuccess(t)
        }
    }

    fun doComplete(emitter: MaybeEmitter<T>) {
        if (!emitter.isDisposed) {
            emitter.onComplete()
        }
    }

    fun doError(emitter: MaybeEmitter<T>, error: Throwable) {
        if (!emitter.isDisposed) {
            emitter.onError(error)
        }
    }
}