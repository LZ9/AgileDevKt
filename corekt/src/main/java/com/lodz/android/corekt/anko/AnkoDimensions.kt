package com.lodz.android.corekt.anko

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment

/**
 * 单位转换扩展类
 * Created by zhouL on 2018/6/27.
 */

/** px转dp */
fun Context.px2dp(px: Int): Float = px.toFloat() / resources.displayMetrics.density

/** px转sp */
fun Context.px2sp(px: Int): Float = px.toFloat() / resources.displayMetrics.scaledDensity

/** dp转px */
fun Context.dp2px(dp: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

/** dp转px */
fun Context.dp2px(dp: Int): Float = dp2px(dp.toFloat())

/** sp转px */
fun Context.sp2px(sp: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)

/** sp转px */
fun Context.sp2px(sp: Int): Float = sp2px(sp.toFloat())

/** dimen转px */
fun Context.dimen(@DimenRes resource: Int): Float = resources.getDimension(resource)

fun View.px2dp(px: Int): Float = context.px2dp(px)
fun View.px2sp(px: Int): Float = context.px2sp(px)
fun View.dp2px(dp: Float): Float = context.dp2px(dp)
fun View.dp2px(dp: Int): Float = dp2px(dp.toFloat())
fun View.sp2px(sp: Float): Float = context.sp2px(sp)
fun View.sp2px(sp: Int): Float = sp2px(sp.toFloat())
fun View.dimen(@DimenRes resource: Int): Float = context.dimen(resource)

fun Fragment.px2dp(px: Int): Float = requireContext().px2dp(px)
fun Fragment.px2sp(px: Int): Float = requireContext().px2sp(px)
fun Fragment.dp2px(dp: Float): Float = requireContext().dp2px(dp)
fun Fragment.dp2px(dp: Int): Float = dp2px(dp.toFloat())
fun Fragment.sp2px(sp: Float): Float = requireContext().sp2px(sp)
fun Fragment.sp2px(sp: Int): Float = sp2px(sp.toFloat())
fun Fragment.dimen(@DimenRes resource: Int): Float = requireContext().dimen(resource)








