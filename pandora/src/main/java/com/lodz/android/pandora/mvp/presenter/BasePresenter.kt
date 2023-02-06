package com.lodz.android.pandora.mvp.presenter

import android.content.Context
import com.lodz.android.pandora.mvp.contract.abs.PresenterContract
import com.lodz.android.pandora.mvp.contract.abs.ViewContract
import com.trello.rxlifecycle4.LifecycleTransformer
import com.trello.rxlifecycle4.android.ActivityEvent
import com.trello.rxlifecycle4.android.FragmentEvent

/**
 * 基类Presenter
 *
 * 在Activity里只操作UI、显示toast、显示dialog
 * 在ViewContract里定义UI的交互方法
 * 在Presenter里进行数据获取、数据组装、页面跳转、业务逻辑判断
 *
 * @author zhouL
 * @date 2020/12/1
 */
open class BasePresenter<VC : ViewContract> : PresenterContract<VC> {

    /** View接口 */
    private var mViewContract: VC? = null

    /** 上下文 */
    private var mContext: Context? = null

    fun getContext(): Context? = mContext

    override fun attach(context: Context, view: VC) {
        if (mViewContract != null) {
            mViewContract = null
        }
        mContext = context
        mViewContract = view
    }

    override fun detach() {
        mContext = null
        mViewContract = null
    }

    override fun isDetach(): Boolean = mContext == null || mViewContract == null

    override fun <T> bindUntilActivityEvent(event: ActivityEvent): LifecycleTransformer<T> {
        val view = mViewContract ?: throw NullPointerException("ViewContract is null")
        return view.bindUntilActivityEvent(event)
    }

    override fun <T> bindUntilFragmentEvent(event: FragmentEvent): LifecycleTransformer<T> {
        val view = mViewContract ?: throw NullPointerException("ViewContract is null")
        return view.bindUntilFragmentEvent(event)
    }

    override fun <T> bindUntilDetachEvent(): LifecycleTransformer<T> {
        val view = mViewContract ?: throw NullPointerException("ViewContract is null")
        return view.bindUntilDetachEvent()
    }

    fun getViewContract(): VC? = mViewContract

}