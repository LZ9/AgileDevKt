package com.lodz.android.pandora.mvp.contract.abs

import android.content.Context
import com.trello.rxlifecycle4.LifecycleTransformer
import com.trello.rxlifecycle4.android.ActivityEvent
import com.trello.rxlifecycle4.android.FragmentEvent

/**
 * Presenter接口
 * @author zhouL
 * @date 2020/12/1
 */
interface PresenterContract<VC : ViewContract> {

    /** 关联，上下文[context],View接口[view] */
    fun attach(context: Context, view: VC)

    /** 分离 */
    fun detach()

    /** 是否已分离 */
    fun isDetach(): Boolean

    /** 在Activity里绑定Rx生命周期 */
    fun <T> bindUntilActivityEvent(event: ActivityEvent): LifecycleTransformer<T>?

    /** 在Fragment里绑定Rx生命周期 */
    fun <T> bindUntilFragmentEvent(event: FragmentEvent): LifecycleTransformer<T>?

    /** 自动绑定Rx生命周期 */
    fun <T> bindUntilDetachEvent(): LifecycleTransformer<T>?

}