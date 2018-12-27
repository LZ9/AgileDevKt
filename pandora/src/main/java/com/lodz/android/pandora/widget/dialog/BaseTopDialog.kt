package com.lodz.android.pandora.widget.dialog

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.StyleRes
import com.lodz.android.pandora.R

/**
 * 顶部弹框基类
 * Created by zhouL on 2018/9/28.
 */
abstract class BaseTopDialog : BaseDialog {

    constructor(context: Context) : super(context)

    constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId)

    @StyleRes
    override fun configAnimations(): Int = R.style.animation_top_in_top_out

    override fun show() {
        val wd = window
        if (wd != null) {
            wd.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
            if (isMatchWidth()) {
                val layoutParams = wd.attributes
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                wd.attributes = layoutParams
            }
        }
        super.show()
    }

    /** 是否需要填满宽度 */
    protected open fun isMatchWidth(): Boolean = true
}