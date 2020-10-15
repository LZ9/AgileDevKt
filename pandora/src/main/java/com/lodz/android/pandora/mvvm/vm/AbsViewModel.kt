package com.lodz.android.pandora.mvvm.vm

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lodz.android.pandora.base.application.BaseApplication
import com.trello.rxlifecycle4.LifecycleProvider
import com.trello.rxlifecycle4.LifecycleTransformer
import com.trello.rxlifecycle4.RxLifecycle
import com.trello.rxlifecycle4.android.ActivityEvent
import com.trello.rxlifecycle4.android.RxLifecycleAndroid
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

/**
 * 基础ViewModel
 * @author zhouL
 * @date 2019/11/29
 */
open class AbsViewModel : ViewModel(), LifecycleProvider<ActivityEvent> {

    var mPdrLongToastMsg = MutableLiveData<String>()
    var mPdrShortToastMsg = MutableLiveData<String>()
    var isPdrFinish = MutableLiveData<Boolean>()

    var mPdrLifecycleSubject = BehaviorSubject.create<ActivityEvent>()

    /** 显示短提示，[msg]消息 */
    protected fun toastShort(msg: String) { mPdrShortToastMsg.value = msg }

    /** 显示长提示，[msg]消息 */
    protected fun toastLong(msg: String) { mPdrLongToastMsg.value = msg }

    /** 显示短提示，[id]文字资源id */
    protected fun toastShort(@StringRes id: Int) { mPdrShortToastMsg.value = BaseApplication.get()?.getString(id) ?: "" }

    /** 显示长提示，[id]文字资源id */
    protected fun toastLong(@StringRes id: Int) { mPdrLongToastMsg.value = BaseApplication.get()?.getString(id) ?: "" }

    /** 关闭Activity */
    protected fun finish() { isPdrFinish.value = true }

    override fun lifecycle(): Observable<ActivityEvent> = mPdrLifecycleSubject.hide()

    override fun <T : Any?> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> = RxLifecycle.bindUntilEvent(mPdrLifecycleSubject, event)

    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> = RxLifecycleAndroid.bindActivity(mPdrLifecycleSubject)

    /** 绑定Activity的Destroy生命周期 */
    protected fun <T> bindDestroyEvent(): LifecycleTransformer<T> = bindUntilEvent(ActivityEvent.DESTROY)
    protected fun bindAnyDestroyEvent(): LifecycleTransformer<Any> = bindUntilEvent(ActivityEvent.DESTROY)
}