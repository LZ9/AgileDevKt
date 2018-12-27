package com.lodz.android.pandora.widget.dialogfragment

import android.view.Gravity
import android.view.Window
import com.lodz.android.pandora.R

/**
 * 中间DialogFragment基类（缩放动画）
 * Created by zhouL on 2018/12/13.
 */
abstract class BaseCenterDialogFragment : BaseDialogFragment() {

    override fun configAnimations(): Int {
        if (hasAnimations()) {
            return R.style.animation_center_in_center_out
        }
        return super.configAnimations()
    }

    override fun configDialogWindow(window: Window) {
        super.configDialogWindow(window)
        window.setGravity(Gravity.CENTER)
    }

    /** 是否有动画 */
    open fun hasAnimations(): Boolean = true

}