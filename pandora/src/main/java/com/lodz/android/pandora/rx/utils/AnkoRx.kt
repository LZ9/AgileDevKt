package com.lodz.android.pandora.rx.utils

import io.reactivex.rxjava3.core.*


/**
 * Rx扩展类
 * @author zhouL
 * @date 2019/12/12
 */

fun <T : Any> ObservableEmitter<T>.doNext(t: T) {
    if (!isDisposed) {
        onNext(t)
    }
}

fun <T : Any> ObservableEmitter<T>.doError(error: Throwable) {
    if (!isDisposed) {
        onError(error)
    }
}

fun <T : Any> ObservableEmitter<T>.doComplete() {
    if (!isDisposed) {
        onComplete()
    }
}

fun <T : Any> SingleEmitter<T>.doSuccess(t: T) {
    if (!isDisposed) {
        onSuccess(t)
    }
}

fun <T : Any> SingleEmitter<T>.doError(error: Throwable) {
    if (!isDisposed) {
        onError(error)
    }
}

fun <T : Any> MaybeEmitter<T>.doSuccess(t: T) {
    if (!isDisposed) {
        onSuccess(t)
    }
}

fun <T : Any> MaybeEmitter<T>.doComplete() {
    if (!isDisposed) {
        onComplete()
    }
}

fun <T : Any> MaybeEmitter<T>.doError(error: Throwable) {
    if (!isDisposed) {
        onError(error)
    }
}

fun <T : Any> FlowableEmitter<T>.doNext(t: T) {
    if (!isCancelled) {
        onNext(t)
    }
}

fun <T : Any> FlowableEmitter<T>.doError(error: Throwable) {
    if (!isCancelled) {
        onError(error)
    }
}

fun <T : Any> FlowableEmitter<T>.doComplete() {
    if (!isCancelled) {
        onComplete()
    }
}

fun CompletableEmitter.doComplete() {
    if (!isDisposed) {
        onComplete()
    }
}

fun CompletableEmitter.doError(error: Throwable) {
    if (!isDisposed) {
        onError(error)
    }
}