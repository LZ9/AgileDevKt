package com.lodz.android.corekt.anko

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import androidx.annotation.AnimRes

/**
 * 动画扩展类
 * Created by zhouL on 2018/7/2.
 */

/** 开始动画资源[animResId]，并在动画结束时指定控件显隐状态[visibility] */
fun View.startAnim(context: Context, @AnimRes animResId: Int, visibility: Int) {
    clearAnimation()
    val animation = AnimationUtils.loadAnimation(context, animResId)
    startAnimation(animation)
    setVisibility(visibility)
}

/** 旋转自身，起始角度[fromDegrees]，结束角度[toDegrees]，时间[duration]，动画转化结束后被应用[fillAfter] */
fun View.startRotateSelf(fromDegrees: Float, toDegrees: Float, duration: Long, fillAfter: Boolean) {
    clearAnimation()
    val animation = RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    animation.duration = duration
    animation.fillAfter = fillAfter//设置为true，动画转化结束后被应用
    setAnimation(animation)//开始动画
}

/** 放大自身，
 * X轴起始大小[fromX]（0.0~n）1.0表示控件原始大小。
 * X轴结束大小[toX]（0.0~n）1.0表示控件原始大小。
 * Y轴起始大小[fromY]（0.0~n）1.0表示控件原始大小。
 * Y轴结束大小[toY]（0.0~n）1.0表示控件原始大小。
 * 时间[duration]，动画转化结束后被应用[fillAfter]
 */
fun View.startScaleSelf(fromX: Float, toX: Float, fromY: Float, toY: Float, duration: Long, fillAfter: Boolean) {
    clearAnimation()
    val animation = ScaleAnimation(fromX, toX, fromY, toY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    animation.duration = duration
    animation.fillAfter = fillAfter//设置为true，动画转化结束后被应用
    startAnimation(animation)//开始动画
}