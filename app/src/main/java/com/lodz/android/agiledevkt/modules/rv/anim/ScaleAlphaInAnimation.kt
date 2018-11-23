package com.lodz.android.agiledevkt.modules.rv.anim

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import com.lodz.android.componentkt.widget.rv.animation.BaseAnimation

/**
 * 缩放淡入
 * Created by zhouL on 2018/11/23.
 */
class ScaleAlphaInAnimation(private val alphaFrom: Float = 0f, private val scaleFrom: Float = 0.5f, private val duration: Int = 300) : BaseAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", scaleFrom, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", scaleFrom, 1f)
        val alpha = ObjectAnimator.ofFloat(view, "alpha", alphaFrom, 1f)
        return arrayOf(scaleX, scaleY, alpha)
    }

    override fun getDuration(): Int = duration
}