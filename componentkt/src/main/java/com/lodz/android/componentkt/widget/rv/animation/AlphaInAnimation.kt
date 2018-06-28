package com.lodz.android.componentkt.widget.rv.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

/**
 * 淡入效果
 * Created by zhouL on 2018/6/28.
 */
class AlphaInAnimation(private val from: Float = 0f, private val duration: Int = 300) : BaseAnimation {

    override fun getAnimators(view: View) = arrayOf<Animator>(ObjectAnimator.ofFloat(view, "alpha", from, 1f))

    override fun getDuration() = duration
}