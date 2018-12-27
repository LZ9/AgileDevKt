package com.lodz.android.pandora.widget.rv.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

/**
 * 底部进入
 * Created by zhouL on 2018/6/28.
 */
class SlideInBottomAnimation(private val duration: Int = 300) : BaseAnimation {

    override fun getAnimators(view: View): Array<Animator> = arrayOf(ObjectAnimator.ofFloat(view, "translationY", view.measuredHeight.toFloat(), 0f))

    override fun getDuration(): Int = duration
}