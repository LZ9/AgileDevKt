package com.lodz.android.pandora.widget.dialogfragment

import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.lodz.android.pandora.R

/**
 * 顶部DialogFragment基类
 * Created by zhouL on 2018/12/13.
 */
abstract class BaseTopDialogFragment() : BaseDialogFragment() {

    override fun configAnimations(): Int = R.style.animation_top_in_top_out

    override fun configDialogWindow(window: Window) {
        super.configDialogWindow(window)
        window.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
        if (isMatchWidth()) {
            val layoutParams = window.attributes
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = layoutParams
        }
    }

    /** 是否需要填满宽度 */
    protected open fun isMatchWidth(): Boolean = true
}