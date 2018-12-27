package com.lodz.android.pandora.widget.bottomsheets.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lodz.android.corekt.anko.getNavigationBarHeight
import com.lodz.android.corekt.anko.getScreenHeight
import com.lodz.android.corekt.anko.getStatusBarHeight
import com.lodz.android.corekt.utils.ReflectUtils

/**
 * BottomSheetDialog基类
 * Created by zhouL on 2018/12/11.
 */
abstract class BaseBottomSheetDialog : BottomSheetDialog {

    constructor(context: Context) : super(context)
    constructor(context: Context, theme: Int) : super(context, theme)
    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startCreate()
        createContentView(getLayoutId())
        findViews()
        setListeners()
        initData()
        endCreate()
        configStatusBar()
        configBehavior()
    }

    protected open fun startCreate() {}

    private fun createContentView(@LayoutRes layoutId: Int) {
        val view = layoutInflater.inflate(layoutId, null)
        setContentView(view)
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected open fun findViews() {}

    protected open fun setListeners() {}

    protected open fun initData() {}

    protected open fun endCreate() {}

    protected abstract fun onBehaviorInit(behavior: BottomSheetBehavior<*>)

    protected fun getDialogInterface(): DialogInterface = this

    /** 设置背景蒙版透明度[value] */
    protected fun setDim(@FloatRange(from = 0.0, to = 1.0) value: Float) {
        window?.setDimAmount(value)
    }

    /** 配置状态栏 */
    private fun configStatusBar() {
        val wd = window
        if (wd == null) {
            return
        }
        if (!configTransparentStatusBar()) {
            wd.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            return
        }
        wd.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)// 设置底部展示
        val screenHeight = context.getScreenHeight()
        val statusBarHeight = context.getStatusBarHeight()
        val navigationBarHeight = context.getNavigationBarHeight(wd)
        val dialogHeight = screenHeight - statusBarHeight + navigationBarHeight - configTopOffsetPx()//屏幕高度 - 状态栏高度 + 导航栏 - 偏移量高度
        wd.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, if (dialogHeight == 0) ViewGroup.LayoutParams.MATCH_PARENT else dialogHeight)
    }

    /** 配置BottomSheetBehavior */
    private fun configBehavior() {
        val cls = ReflectUtils.getClassForName("com.google.android.material.bottomsheet.BottomSheetDialog")
        if (cls == null) {
            return
        }
        val behavior = ReflectUtils.getFieldValue(cls, this, "behavior")
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            onBehaviorInit(behavior)
        }
    }

    /** 配置是否透明状态栏（可重写，默认是） */
    protected open fun configTransparentStatusBar(): Boolean = true

    /** 配置布局高度偏移量（可重写，默认0） */
    protected open fun configTopOffsetPx(): Int = 0
}