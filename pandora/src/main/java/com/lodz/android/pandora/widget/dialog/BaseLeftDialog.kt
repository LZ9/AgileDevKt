package com.lodz.android.pandora.widget.dialog

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.StyleRes
import com.lodz.android.pandora.R

/**
 * 左侧弹框基类
 * Created by zhouL on 2018/9/28.
 */
abstract class BaseLeftDialog : BaseDialog {

    constructor(context: Context) : super(context)

    constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId)

    @StyleRes
    override fun configAnimations(): Int = R.style.animation_left_in_left_out

    override fun show() {
        super.show()
        val wd = window
        if (wd != null) {
            wd.setGravity(Gravity.START or Gravity.CENTER_VERTICAL)
            if (isMatchHeight()) {
                val layoutParams = wd.attributes
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                wd.attributes = layoutParams
            }
        }
    }

    /** 是否需要填满高度 */
    protected open fun isMatchHeight(): Boolean = true
}