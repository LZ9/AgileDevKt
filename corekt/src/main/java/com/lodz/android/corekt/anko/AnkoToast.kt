package com.lodz.android.corekt.anko

import android.app.Dialog
import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.lodz.android.corekt.utils.ToastUtils

/**
 * Toast扩展类
 * Created by zhouL on 2019/2/1.
 */

/** 显示文字为[text]的短时间的Toast */
fun Context.toastShort(text: String) {
    ToastUtils.showShort(this, text)
}

/** 显示文字资源为[strResId]的短时间的Toast */
fun Context.toastShort(@StringRes strResId: Int) {
    ToastUtils.showShort(this, strResId)
}

/** 显示文字为[text]的长时间的Toast */
fun Context.toastLong(text: String) {
    ToastUtils.showLong(this, text)
}

/** 显示文字资源为[strResId]的长时间的Toast */
fun Context.toastLong(@StringRes strResId: Int) {
    ToastUtils.showLong(this, strResId)
}

/** 显示文字为[text]的短时间的Toast */
fun Fragment.toastShort(text: String) {
    context?.toastShort(text)
}

/** 显示文字资源为[strResId]的短时间的Toast */
fun Fragment.toastShort(@StringRes strResId: Int) {
    context?.toastShort(strResId)
}

/** 显示文字为[text]的长时间的Toast */
fun Fragment.toastLong(text: String) {
    context?.toastLong(text)
}

/** 显示文字资源为[strResId]的长时间的Toast */
fun Fragment.toastLong(@StringRes strResId: Int) {
    context?.toastLong(strResId)
}

/** 显示文字为[text]的短时间的Toast */
fun View.toastShort(text: String) {
    context.toastShort(text)
}

/** 显示文字资源为[strResId]的短时间的Toast */
fun View.toastShort(@StringRes strResId: Int) {
    context.toastShort(strResId)
}

/** 显示文字为[text]的长时间的Toast */
fun View.toastLong(text: String) {
    context.toastLong(text)
}

/** 显示文字资源为[strResId]的长时间的Toast */
fun View.toastLong(@StringRes strResId: Int) {
    context.toastLong(strResId)
}

/** 显示文字为[text]的短时间的Toast */
fun Dialog.toastShort(text: String) {
    context.toastShort(text)
}

/** 显示文字资源为[strResId]的短时间的Toast */
fun Dialog.toastShort(@StringRes strResId: Int) {
    context.toastShort(strResId)
}

/** 显示文字为[text]的长时间的Toast */
fun Dialog.toastLong(text: String) {
    context.toastLong(text)
}

/** 显示文字资源为[strResId]的长时间的Toast */
fun Dialog.toastLong(@StringRes strResId: Int) {
    context.toastLong(strResId)
}
