package com.lodz.android.pandora.rx.utils

import io.reactivex.CompletableEmitter
import io.reactivex.CompletableOnSubscribe

/**
 * 带入参的CompletableOnSubscribe
 * Created by zhouL on 2019/1/22.
 */
abstract class RxCompletableOnSubscribe(vararg args: Any?) : CompletableOnSubscribe {

    private var mArgs: Array<out Any?>

    init {
        mArgs = args
    }

    fun getArgs(): Array<out Any?> = mArgs

    fun doComplete(emitter: CompletableEmitter) {
        if (!emitter.isDisposed) {
            emitter.onComplete()
        }
    }

    fun doError(emitter: CompletableEmitter, error: Throwable) {
        if (!emitter.isDisposed) {
            emitter.onError(error)
        }
    }
}