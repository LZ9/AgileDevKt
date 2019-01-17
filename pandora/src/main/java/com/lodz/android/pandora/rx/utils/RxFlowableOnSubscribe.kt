package com.lodz.android.pandora.rx.utils

import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe

/**
 * 带入参的FlowableOnSubscribe
 * Created by zhouL on 2019/1/17.
 */
abstract class RxFlowableOnSubscribe<T>(vararg args: Any?) : FlowableOnSubscribe<T> {

    private var mArgs: Array<out Any?>

    init {
        mArgs = args
    }

    fun getArgs(): Array<out Any?> = mArgs

    fun doNext(emitter: FlowableEmitter<T>, t: T) {
        if (!emitter.isCancelled) {
            emitter.onNext(t)
        }
    }

    fun doError(emitter: FlowableEmitter<T>, error: Throwable) {
        if (!emitter.isCancelled) {
            emitter.onError(error)
        }
    }

    fun doComplete(emitter: FlowableEmitter<T>) {
        if (!emitter.isCancelled) {
            emitter.onComplete()
        }
    }
}