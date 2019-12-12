package com.lodz.android.pandora.rx.utils

import io.reactivex.*

/**
 * Rx扩展类
 * @author zhouL
 * @date 2019/12/12
 */

fun <T> ObservableEmitter<T>.doNext(t: T) {
    if (!isDisposed) {
        onNext(t)
    }
}

fun <T> ObservableEmitter<T>.doError(error: Throwable) {
    if (!isDisposed) {
        onError(error)
    }
}

fun <T> ObservableEmitter<T>.doComplete() {
    if (!isDisposed) {
        onComplete()
    }
}

fun <T> SingleEmitter<T>.doSuccess(t: T) {
    if (!isDisposed) {
        onSuccess(t)
    }
}

fun <T> SingleEmitter<T>.doError(error: Throwable) {
    if (!isDisposed) {
        onError(error)
    }
}

fun <T> MaybeEmitter<T>.doSuccess(t: T) {
    if (!isDisposed) {
        onSuccess(t)
    }
}

fun <T> MaybeEmitter<T>.doComplete() {
    if (!isDisposed) {
        onComplete()
    }
}

fun <T> MaybeEmitter<T>.doError(error: Throwable) {
    if (!isDisposed) {
        onError(error)
    }
}

fun <T> FlowableEmitter<T>.doNext(t: T) {
    if (!isCancelled) {
        onNext(t)
    }
}

fun <T> FlowableEmitter<T>.doError(error: Throwable) {
    if (!isCancelled) {
        onError(error)
    }
}

fun <T> FlowableEmitter<T>.doComplete() {
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