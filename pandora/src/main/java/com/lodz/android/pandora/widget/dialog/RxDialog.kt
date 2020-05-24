package com.lodz.android.pandora.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import com.trello.rxlifecycle4.LifecycleProvider
import com.trello.rxlifecycle4.LifecycleTransformer
import com.trello.rxlifecycle4.RxLifecycle
import com.trello.rxlifecycle4.android.FragmentEvent
import com.trello.rxlifecycle4.android.RxLifecycleAndroid
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

/**
 * @author zhouL
 * @date 2019/4/2
 */
open class RxDialog : Dialog, LifecycleProvider<FragmentEvent> {

    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)
    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)

    private val lifecycleSubject = BehaviorSubject.create<FragmentEvent>()

    override fun lifecycle(): Observable<FragmentEvent> = lifecycleSubject.hide()

    override fun <T> bindUntilEvent(event: FragmentEvent): LifecycleTransformer<T> = RxLifecycle.bindUntilEvent(lifecycleSubject, event)

    override fun <T> bindToLifecycle(): LifecycleTransformer<T> = RxLifecycleAndroid.bindFragment(lifecycleSubject)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleSubject.onNext(FragmentEvent.CREATE)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lifecycleSubject.onNext(FragmentEvent.ATTACH)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lifecycleSubject.onNext(FragmentEvent.DETACH)
    }

    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(FragmentEvent.START)
    }

    override fun onStop() {
        super.onStop()
        lifecycleSubject.onNext(FragmentEvent.STOP)
    }

}