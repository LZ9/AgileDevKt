package com.lodz.android.pandora.widget.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import com.lodz.android.pandora.R
import com.trello.rxlifecycle4.LifecycleTransformer
import com.trello.rxlifecycle4.android.FragmentEvent

/**
 * 弹框基类
 * Created by zhouL on 2018/9/28.
 */
abstract class BaseDialog : RxDialog {

    constructor(context: Context) : super(context, R.style.BaseDialog){
        init()
    }

    constructor(context: Context, themeResId: Int) : super(context, themeResId){
        init()
    }

    private fun init() {
        val layoutId = getLayoutId()
        if (layoutId != 0){
            setContentView(layoutId)
        }
        setWindowAnimations()
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getLayoutId() == 0) {
            val view = getViewBindingLayout()
            if (view != null) {
                setContentView(view)
            } else {
                throw NullPointerException("please override getLayoutId() or getViewBindingLayout() to set layout")
            }
        }
        startCreate()
        findViews()
        setListeners()
        initData()
        endCreate()
    }

    protected open fun startCreate() {}

    @LayoutRes
    protected open fun getLayoutId(): Int = 0

    protected open fun getViewBindingLayout(): View? = null

    protected open fun findViews() {}

    protected open fun setListeners() {}

    protected open fun initData() {}

    protected open fun endCreate() {}

    @StyleRes
    protected open fun configAnimations(): Int = -1

    /** 设置弹出动画 */
    private fun setWindowAnimations() {
        val animations = configAnimations()
        if (animations != -1) {
            window?.setWindowAnimations(animations) //设置窗口弹出动画
        }
    }

    /** 设置阴影值[elevation]和背景[background]（需要设置背景才能设置阴影） */
    protected fun setElevation(elevation: Float, background: Drawable) {
        val wd = window ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wd.decorView.elevation = elevation
            wd.decorView.background = background
        }
    }

    /** 获取dialog接口对象 */
    protected fun getDialogInterface(): DialogInterface = this

    /** 绑定Activity的Destroy生命周期 */
    protected fun <T> bindStopEvent(): LifecycleTransformer<T> = bindUntilEvent(FragmentEvent.STOP)
    protected fun bindAnyStopEvent(): LifecycleTransformer<Any> = bindUntilEvent(FragmentEvent.STOP)
}