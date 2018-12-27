package com.lodz.android.pandora.widget.dialog

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.StyleRes
import com.lodz.android.pandora.R

/**
 * 右侧弹框基类
 * Created by zhouL on 2018/9/28.
 */
abstract class BaseRightDialog : BaseDialog {

    constructor(context: Context) : super(context)

    constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId)

    @StyleRes
    override fun configAnimations(): Int = R.style.animation_right_in_right_out

    override fun show() {
        val wd = window
        if (wd != null) {
            wd.setGravity(Gravity.END or Gravity.CENTER_VERTICAL)
            if (isMatchHeight()) {
                val layoutParams = wd.attributes
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                wd.attributes = layoutParams
            }
        }
        super.show()
    }

    /** 是否需要填满高度 */
    protected open fun isMatchHeight(): Boolean = true

}