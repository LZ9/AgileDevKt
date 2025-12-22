package com.lodz.android.corekt.anko

import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.lang.reflect.Method

/**
 * 软键盘控制类
 * Created by zhouL on 2018/11/5.
 */

/** 软键盘是否打开 */
fun Context.isKeyBordShow(): Boolean = (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).isActive

/** 软键盘是否打开 */
fun View.isKeyBordShow(): Boolean = (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).isActive(this)

/** 显示软键盘 */
fun View.showInputMethod() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.RESULT_SHOWN)
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

/** 隐藏软键盘 */
fun View.hideInputMethod() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/** 替换原始键盘 */
fun EditText.replaceOriginalKeyboard(window: Window) {
    try {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val cls = EditText::class.java
        val methods = cls.methods
        var setShowSoftInputOnFocus: Method? = null
        // 版本不同获取不同焦点
        for (method in methods) {
            if (method.name.equals("setShowSoftInputOnFocus")) {
                setShowSoftInputOnFocus = method
            } else if (method.name.equals("setSoftInputShownOnFocus")) {
                setShowSoftInputOnFocus = method
            }
        }
        if (setShowSoftInputOnFocus != null) {
            setShowSoftInputOnFocus.isAccessible = true
            setShowSoftInputOnFocus.invoke(this, false)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/** 自定义输入法[modeName]是否启用 */
fun Context.isInputMethodEnabled(modeName: String): Boolean{
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val enabledImeList = imm.enabledInputMethodList
    // 遍历列表，判断是否包含自定义输入法
    for (imeInfo in enabledImeList) {
        if (imeInfo.id.contains(modeName)) {
            return true
        }
    }
    return false
}

/** 核对当前输入法是否为自定义输入法[modeName] */
fun Context.checkInputMethodMode(modeName: String): Boolean {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentModeName = imm.currentInputMethodSubtype?.mode ?: "" // 获取当前输入法ID
    if (currentModeName.isEmpty()){
        return false
    }
    return currentModeName == modeName
}

/** 显示系统输入法选择器 */
fun Context.showInputMethodPicker() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showInputMethodPicker()
}