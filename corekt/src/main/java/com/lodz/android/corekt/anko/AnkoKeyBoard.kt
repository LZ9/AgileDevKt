package com.lodz.android.corekt.anko

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * 软键盘控制类
 * Created by zhouL on 2018/11/5.
 */

/** 软键盘是否打开 */
fun Context.isKeybordShow(): Boolean = (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).isActive

/** 软键盘是否打开 */
fun View.isKeybordShow(): Boolean = (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).isActive(this)

/** 显示软键盘 */
fun View.showInputMethod(){
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.RESULT_SHOWN)
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

/** 隐藏软键盘 */
fun View.hideInputMethod(){
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}