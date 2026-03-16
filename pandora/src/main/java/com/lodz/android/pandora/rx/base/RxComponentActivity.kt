package com.lodz.android.pandora.rx.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.activity.ComponentActivity
import androidx.annotation.CheckResult
import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import com.trello.rxlifecycle4.LifecycleProvider
import com.trello.rxlifecycle4.LifecycleTransformer
import com.trello.rxlifecycle4.RxLifecycle
import com.trello.rxlifecycle4.android.ActivityEvent
import com.trello.rxlifecycle4.android.RxLifecycleAndroid
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

/**
 * RX生命周期实现基类
 * @author zhouL
 * @date 2026/3/16
 */
class RxComponentActivity : ComponentActivity, LifecycleProvider<ActivityEvent> {

    private val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()

    constructor() : super()

    @ContentView
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    @CheckResult
    override fun lifecycle(): Observable<ActivityEvent> = lifecycleSubject.hide()

    @CheckResult
    override fun <T> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> = RxLifecycle.bindUntilEvent(lifecycleSubject, event)

    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> = RxLifecycleAndroid.bindActivity(lifecycleSubject)

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleSubject.onNext(ActivityEvent.CREATE)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(ActivityEvent.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(ActivityEvent.RESUME)
    }

    @CallSuper
    override fun onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE)
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP)
        super.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY)
        super.onDestroy()
    }
}