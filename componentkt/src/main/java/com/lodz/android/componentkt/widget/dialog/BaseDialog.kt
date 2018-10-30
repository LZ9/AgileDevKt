package com.lodz.android.componentkt.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import com.lodz.android.componentkt.R

/**
 * 弹框基类
 * Created by zhouL on 2018/9/28.
 */
abstract class BaseDialog : Dialog {

    constructor(context: Context) : super(context, R.style.BaseDialog) {
        initDialog(context)
    }

    constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId) {
        initDialog(context)
    }

    private fun initDialog(context: Context) {
        onStartInit(context)
        setContentView(getLayoutId())
        findViews()
        setListeners()
        initData()
        setWindowAnimations()
    }

    protected open fun onStartInit(context: Context) {}

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun findViews()

    protected open fun setListeners() {}

    protected open fun initData() {}

    @StyleRes
    protected open fun getAnimations() = -1

    /** 设置弹出动画 */
    private fun setWindowAnimations() {
        val wd = window
        if (wd != null && getAnimations() != -1) {
            wd.setWindowAnimations(getAnimations()) //设置窗口弹出动画
        }
    }

    /** 设置阴影值[elevation]和背景[background]（需要设置背景才能设置阴影） */
    protected fun setElevation(elevation: Float, background: Drawable) {
        val wd = window
        if (wd == null) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wd.decorView.elevation = elevation
            wd.decorView.background = background
        }
    }

    /** 获取dialog接口对象 */
    protected fun getDialogInterface(): DialogInterface = this
}