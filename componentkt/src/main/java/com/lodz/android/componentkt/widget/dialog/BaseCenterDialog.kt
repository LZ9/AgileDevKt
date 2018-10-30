package com.lodz.android.componentkt.widget.dialog

import android.content.Context
import android.view.Gravity
import androidx.annotation.StyleRes
import com.lodz.android.componentkt.R

/**
 * 中间弹框基类（缩放动画）
 * Created by zhouL on 2018/9/28.
 */
abstract class BaseCenterDialog : BaseDialog {

    constructor(context: Context) : super(context)

    constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId)

    override fun getAnimations(): Int {
        if (isNeedAnimations()) {
            return R.style.animation_center_in_center_out
        }
        return super.getAnimations()
    }

    /** 是否使用动画 */
    open fun isNeedAnimations() = true

    override fun show() {
        val wd = window
        if (wd != null) {
            wd.setGravity(Gravity.CENTER)
        }
        super.show()
    }
}