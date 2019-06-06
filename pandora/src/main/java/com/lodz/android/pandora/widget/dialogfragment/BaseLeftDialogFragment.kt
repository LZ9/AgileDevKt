package com.lodz.android.pandora.widget.dialogfragment

import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.lodz.android.pandora.R

/**
 * 左侧DialogFragment基类
 * Created by zhouL on 2018/12/13.
 */
abstract class BaseLeftDialogFragment : BaseDialogFragment() {

    override fun configAnimations(): Int = R.style.animation_left_in_left_out

    override fun configDialogWindow(window: Window) {
        super.configDialogWindow(window)
        window.setGravity(Gravity.START or Gravity.CENTER_VERTICAL)
        if (isMatchHeight()) {
            val layoutParams = window.attributes
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = layoutParams
        }
    }

    /** 是否需要填满高度 */
    protected open fun isMatchHeight(): Boolean = true
}