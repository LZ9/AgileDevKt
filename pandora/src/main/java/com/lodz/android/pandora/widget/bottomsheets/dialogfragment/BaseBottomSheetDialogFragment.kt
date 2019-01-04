package com.lodz.android.pandora.widget.bottomsheets.dialogfragment

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lodz.android.corekt.anko.getNavigationBarHeight
import com.lodz.android.corekt.anko.getScreenHeight
import com.lodz.android.corekt.anko.getStatusBarHeight
import com.lodz.android.corekt.utils.ReflectUtils
import com.lodz.android.pandora.R

/**
 * BottomSheetDialogFragment基类
 * Created by zhouL on 2018/12/11.
 */
abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCreate()
        findViews(view, savedInstanceState)
        setListeners(view)
        initData(view)
        endCreate()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val cls = ReflectUtils.getClassForName("com.google.android.material.bottomsheet.BottomSheetDialog")
        if (cls == null) {
            return
        }
        val behavior = ReflectUtils.getFieldValue(cls, dialog, "behavior")
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            onBehaviorInit(behavior)
        }
    }

    protected open fun startCreate() {}

    protected open fun findViews(view: View, savedInstanceState: Bundle?) {}

    protected open fun setListeners(view: View) {}

    protected open fun initData(view: View) {}

    protected open fun endCreate() {}

    protected abstract fun onBehaviorInit(behavior: BottomSheetBehavior<*>)

    /** 设置背景蒙版透明度[value] */
    protected fun setDim(@FloatRange(from = 0.0, to = 1.0) value: Float) {
        dialog?.window?.setDimAmount(value)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val ctx = context
        if (ctx == null) {
            return super.onCreateDialog(savedInstanceState)
        }
        return BottomSheetDialog(ctx, R.style.TransparentBottomSheetStyle)//使用自定义style创建Dialog
    }

    override fun onStart() {
        super.onStart()
        setStatusBar()
    }

    /** 配置状态栏 */
    private fun setStatusBar() {
        val window = dialog?.window
        if (window == null) {
            return
        }
        if (!configTransparentStatusBar()) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            return
        }
        window.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)// 设置底部展示
        val screenHeight = requireContext().getScreenHeight()
        val statusBarHeight = requireContext().getStatusBarHeight()
        val navigationBarHeight = requireContext().getNavigationBarHeight(window)
        val dialogHeight = screenHeight - statusBarHeight + navigationBarHeight - configTopOffsetPx()//屏幕高度 - 状态栏高度 + 导航栏 - 偏移量高度
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, if (dialogHeight == 0) ViewGroup.LayoutParams.MATCH_PARENT else dialogHeight)
    }

    /** 配置是否透明状态栏（可重写，默认是） */
    protected open fun configTransparentStatusBar(): Boolean = true

    /** 配置布局高度偏移量（可重写，默认0） */
    protected open fun configTopOffsetPx(): Int = 0

}