package com.lodz.android.componentkt.widget.dialogfragment

import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.lodz.android.componentkt.R

/**
 * 右侧DialogFragment基类
 * Created by zhouL on 2018/12/13.
 */
abstract class BaseRightDialogFragment() :BaseDialogFragment(){

    override fun configAnimations(): Int = R.style.animation_right_in_right_out

    override fun configDialogWindow(window: Window) {
        super.configDialogWindow(window)
        window.setGravity(Gravity.END or Gravity.CENTER_VERTICAL)
        if (isMatchHeight()){
            val layoutParams = window.attributes
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = layoutParams
        }
    }

    /** 是否需要填满高度 */
    protected open fun isMatchHeight(): Boolean = true
}