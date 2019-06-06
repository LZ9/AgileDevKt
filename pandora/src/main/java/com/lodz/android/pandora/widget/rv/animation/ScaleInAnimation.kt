package com.lodz.android.pandora.widget.rv.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

/**
 * 缩放效果
 * Created by zhouL on 2018/6/28.
 */
class ScaleInAnimation @JvmOverloads constructor(private val from: Float = 0.5f, private val duration: Int = 300) : BaseAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", from, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", from, 1f)
        return arrayOf(scaleX, scaleY)
    }

    override fun getDuration(): Int = duration
}