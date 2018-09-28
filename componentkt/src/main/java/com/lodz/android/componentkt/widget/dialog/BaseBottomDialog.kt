package com.lodz.android.componentkt.widget.dialog

import android.content.Context
import android.support.annotation.StyleRes
import android.view.Gravity
import android.view.WindowManager
import com.lodz.android.componentkt.R

/**
 * 底部弹框基类
 * Created by zhouL on 2018/9/28.
 */
abstract class BaseBottomDialog : BaseDialog {

    constructor(context: Context) : super(context)

    constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId)

    override fun getAnimations(): Int {
        return R.style.animation_bottom_in_bottom_out
    }

    override fun show() {
        val wd = window
        if (wd != null) {
            wd.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
            if (isMatchWidth()) {
                val layoutParams = wd.attributes
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                wd.attributes = layoutParams
            }
        }
        super.show()
    }

    /** 是否需要填满宽度 */
    protected open fun isMatchWidth() = true

}