package com.lodz.android.pandora.widget.bottomsheets.dialogfragment

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lodz.android.corekt.anko.getNavigationBarHeight
import com.lodz.android.corekt.anko.getRealScreenHeight
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
        val dg = dialog ?: return
        val cls = ReflectUtils.getClassForName("com.google.android.material.bottomsheet.BottomSheetDialog") ?: return
        val behavior = ReflectUtils.getFieldValue(cls, dg, "behavior")
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
        val ctx = context ?: return super.onCreateDialog(savedInstanceState)
        return BottomSheetDialog(ctx, R.style.TransparentBottomSheetStyle)//使用自定义style创建Dialog
    }

    override fun onStart() {
        super.onStart()
        setStatusBar()
    }

    /** 配置状态栏 */
    private fun setStatusBar() {
        val wd = dialog?.window ?: return
        wd.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)// 设置底部展示
        val realScreenHeight = requireContext().getRealScreenHeight(wd)// 屏幕真实高度
        val screenHeight = requireContext().getScreenHeight()//可用高度
        val statusBarHeight = requireContext().getStatusBarHeight()//状态栏高度
        val navigationBarHeight = requireContext().getNavigationBarHeight()// 导航栏高度
        val dialogHeight = realScreenHeight - statusBarHeight - configTopOffsetPx()

        wd.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, if (dialogHeight == 0) ViewGroup.LayoutParams.MATCH_PARENT else dialogHeight)
    }

    /** 配置布局高度偏移量（可重写，默认0） */
    protected open fun configTopOffsetPx(): Int = 0

}