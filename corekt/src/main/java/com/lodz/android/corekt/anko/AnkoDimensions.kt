package com.lodz.android.corekt.anko

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

/**
 * 单位转换扩展类
 * Created by zhouL on 2018/6/27.
 */

/** 单位转换，需要转换的参数[param]，转换公式[block] */
inline fun <reified T> changeDimensions(param: T, block: (param: Float) -> Float): T {
    val paramF: Float = when (T::class) {
        Float::class -> param as Float
        Int::class -> (param as Int).toFloat()
        Long::class -> (param as Long).toFloat()
        Double::class -> (param as Double).toFloat()
        String::class -> if ((param as String) == "") 0f else (param as String).toFloat()
        else -> throw IllegalArgumentException("Unsupported type")
    }

    val result = block(paramF)

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

/** px转sp */
inline fun <reified T> Context.px2sp(px: T): T = changeDimensions(px) { it / resources.displayMetrics.scaledDensity }

/** dp转px */
inline fun <reified T> Context.dp2px(dp: T): T = changeDimensions(dp) { TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, it, resources.displayMetrics) }

/** sp转px */
inline fun <reified T> Context.sp2px(sp: T): T = changeDimensions(sp) { TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, it, resources.displayMetrics) }

/** dimen转px */
fun Context.dimen(@DimenRes resource: Int): Float = resources.getDimension(resource)


/** px转dp */
inline fun <reified T> View.px2dp(px: T): T = context.px2dp(px)
/** px转sp */
inline fun <reified T> View.px2sp(px: T): T = context.px2sp(px)
/** dp转px */
inline fun <reified T> View.dp2px(dp: T): T = context.dp2px(dp)
/** sp转px */
inline fun <reified T> View.sp2px(sp: T): T = context.sp2px(sp)
/** dimen转px */
fun View.dimen(@DimenRes resource: Int): Float = context.dimen(resource)


/** px转dp */
inline fun <reified T> Fragment.px2dp(px: T): T = requireContext().px2dp(px)
/** px转sp */
inline fun <reified T> Fragment.px2sp(px: T): T = requireContext().px2sp(px)
/** dp转px */
inline fun <reified T> Fragment.dp2px(dp: T): T = requireContext().dp2px(dp)
/** sp转px */
inline fun <reified T> Fragment.sp2px(sp: T): T = requireContext().sp2px(sp)
/** dimen转px */
fun Fragment.dimen(@DimenRes resource: Int): Float = requireContext().dimen(resource)


/** px转dp */
inline fun <reified T> RecyclerView.ViewHolder.px2dp(px: T): T = itemView.px2dp(px)
/** px转sp */
inline fun <reified T> RecyclerView.ViewHolder.px2sp(px: T): T = itemView.px2sp(px)
/** dp转px */
inline fun <reified T> RecyclerView.ViewHolder.dp2px(dp: T): T = itemView.dp2px(dp)
/** sp转px */
inline fun <reified T> RecyclerView.ViewHolder.sp2px(sp: T): T = itemView.sp2px(sp)
/** dimen转px */
fun RecyclerView.ViewHolder.dimen(@DimenRes resource: Int): Float = itemView.dimen(resource)