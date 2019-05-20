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

/** 单位转换，需要转换的参数[param]，转换公式[block] */
inline fun <reified T> changeDimensions(param: T, block: (original: Float) -> Float): T {
    val original: Float = when (T::class) {
        Float::class -> (param as Float).toFloat()
        Int::class -> param as Float
        Long::class -> (param as Long).toFloat()
        Double::class -> (param as Double).toFloat()
        String::class -> if ((param as String).equals("")) 0f else (param as String).toFloat()
        else -> throw IllegalArgumentException("Unsupported type")
    }

    val result = block(original)

    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        Long::class -> result.toLong() as T
        Double::class -> result.toDouble() as T
        String::class -> result.toString() as T
        else -> throw IllegalArgumentException("Unsupported type")
    }
}

/** px转dp */
inline fun <reified T> Context.px2dp(px: T): T = changeDimensions(px) { it / resources.displayMetrics.density }
/** px转dp */
fun Context.px2dp(px: Float): Float = px2dp<Float>(px)
/** px转dp */
fun Context.px2dp(px: Int): Int = px2dp(px.toFloat()).toInt()
/** px转dp */
fun Context.px2dpRF(px: Int): Float = px2dp(px.toFloat())
/** px转sp */
inline fun <reified T> Context.px2sp(px: T): T = changeDimensions(px) { it / resources.displayMetrics.scaledDensity }
/** px转sp */
fun Context.px2sp(px: Float): Float = px2sp<Float>(px)
/** px转sp */
fun Context.px2sp(px: Int): Int = px2sp(px.toFloat()).toInt()
/** px转sp */
fun Context.px2spRF(px: Int): Float = px2sp(px.toFloat())
/** dp转px */
inline fun <reified T> Context.dp2px(dp: T): T = changeDimensions(dp) { TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, it, resources.displayMetrics) }
/** dp转px */
fun Context.dp2px(dp: Float): Float = dp2px<Float>(dp)
/** dp转px */
fun Context.dp2px(dp: Int): Int = dp2px(dp.toFloat()).toInt()
/** dp转px */
fun Context.dp2pxRF(dp: Int): Float = dp2px(dp.toFloat())
/** sp转px */
inline fun <reified T> Context.sp2px(sp: T): T = changeDimensions(sp) { TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, it, resources.displayMetrics) }
/** sp转px */
fun Context.sp2px(sp: Float): Float = sp2px<Float>(sp)
/** sp转px */
fun Context.sp2px(sp: Int): Int = sp2px(sp.toFloat()).toInt()
/** sp转px */
fun Context.sp2pxRF(sp: Int): Float = sp2px(sp.toFloat())
/** dimen转px */
fun Context.dimen(@DimenRes resource: Int): Float = resources.getDimension(resource)


/** px转dp */
inline fun <reified T> View.px2dp(px: T): T = context.px2dp(px)
/** px转dp */
fun View.px2dp(px: Float): Float = context.px2dp(px)
/** px转dp */
fun View.px2dp(px: Int): Int = context.px2dp(px)
/** px转dp */
fun View.px2dpRF(px: Int): Float = context.px2dpRF(px)
/** px转sp */
inline fun <reified T> View.px2sp(px: T): T = context.px2sp(px)
/** px转sp */
fun View.px2sp(px: Float): Float = context.px2sp(px)
/** px转sp */
fun View.px2sp(px: Int): Int = context.px2sp(px)
/** px转sp */
fun View.px2spRF(px: Int): Float = context.px2spRF(px)
/** dp转px */
inline fun <reified T> View.dp2px(dp: T): T = context.dp2px(dp)
/** dp转px */
fun View.dp2px(dp: Float): Float = context.dp2px(dp)
/** dp转px */
fun View.dp2px(dp: Int): Int = context.dp2px(dp)
/** dp转px */
fun View.dp2pxRF(dp: Int): Float = context.dp2pxRF(dp)
/** sp转px */
inline fun <reified T> View.sp2px(sp: T): T = context.sp2px(sp)
/** sp转px */
fun View.sp2px(sp: Float): Float = context.sp2px(sp)
/** sp转px */
fun View.sp2px(sp: Int): Int = context.sp2px(sp)
/** sp转px */
fun View.sp2pxRF(sp: Int): Float = context.sp2pxRF(sp)
/** dimen转px */
fun View.dimen(@DimenRes resource: Int): Float = context.dimen(resource)


/** px转dp */
inline fun <reified T> Fragment.px2dp(px: T): T = requireContext().px2dp(px)
/** px转dp */
fun Fragment.px2dp(px: Float): Float = requireContext().px2dp(px)
/** px转dp */
fun Fragment.px2dp(px: Int): Int = requireContext().px2dp(px)
/** px转dp */
fun Fragment.px2dpRF(px: Int): Float = requireContext().px2dpRF(px)
/** px转sp */
inline fun <reified T> Fragment.px2sp(px: T): T = requireContext().px2sp(px)
/** px转sp */
fun Fragment.px2sp(px: Float): Float = requireContext().px2sp(px)
/** px转sp */
fun Fragment.px2sp(px: Int): Int = requireContext().px2sp(px)
/** px转sp */
fun Fragment.px2spRF(px: Int): Float = requireContext().px2spRF(px)
/** dp转px */
inline fun <reified T> Fragment.dp2px(dp: T): T = requireContext().dp2px(dp)
/** dp转px */
fun Fragment.dp2px(dp: Float): Float = requireContext().dp2px(dp)
/** dp转px */
fun Fragment.dp2px(dp: Int): Int = requireContext().dp2px(dp)
/** dp转px */
fun Fragment.dp2pxRF(dp: Int): Float = requireContext().dp2pxRF(dp)
/** sp转px */
inline fun <reified T> Fragment.sp2px(sp: T): T = requireContext().sp2px(sp)
/** sp转px */
fun Fragment.sp2px(sp: Float): Float = requireContext().sp2px(sp)
/** sp转px */
fun Fragment.sp2px(sp: Int): Int = requireContext().sp2px(sp)
/** sp转px */
fun Fragment.sp2pxRF(sp: Int): Float = requireContext().sp2pxRF(sp)
/** dimen转px */
fun Fragment.dimen(@DimenRes resource: Int): Float = requireContext().dimen(resource)