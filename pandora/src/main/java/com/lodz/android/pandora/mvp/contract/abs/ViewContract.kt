package com.lodz.android.pandora.mvp.contract.abs

import androidx.annotation.StringRes
import com.trello.rxlifecycle4.LifecycleTransformer
import com.trello.rxlifecycle4.android.ActivityEvent
import com.trello.rxlifecycle4.android.FragmentEvent

/**
 * View接口
 * @author zhouL
 * @date 2020/12/1
 */
interface ViewContract {

    /** 显示短Toast */
    fun showShortToast(tips: String)

    /** 显示短Toast */
    fun showShortToast(@StringRes resId: Int)

    /** 显示长Toast */
    fun showLongToast(tips: String)

    /** 显示长Toast */
    fun showLongToast(@StringRes resId: Int)

    /** 关闭页面 */
    fun finish()

    /** 在Activity里绑定Rx生命周期 */
    fun <T> bindUntilActivityEvent(event: ActivityEvent): LifecycleTransformer<T>

    /** 在Fragment里绑定Rx生命周期 */
    fun <T> bindUntilFragmentEvent(event: FragmentEvent): LifecycleTransformer<T>

    /** 自动绑定Rx生命周期 */
    fun <T> bindUntilDetachEvent(): LifecycleTransformer<T>

}