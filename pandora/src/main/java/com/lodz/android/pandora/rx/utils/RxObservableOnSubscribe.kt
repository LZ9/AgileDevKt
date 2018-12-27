package com.lodz.android.pandora.rx.utils

import io.reactivex.ObservableOnSubscribe

/**
 * 带入参的ObservableOnSubscribe
 * Created by zhouL on 2018/7/4.
 */
abstract class RxObservableOnSubscribe<T>(vararg args: Any?) : ObservableOnSubscribe<T> {

    private var mArgs: Array<out Any?>

    init {
        mArgs = args
    }

    fun getArgs(): Array<out Any?> = mArgs
}