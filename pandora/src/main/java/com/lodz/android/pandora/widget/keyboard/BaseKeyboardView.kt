package com.lodz.android.pandora.widget.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.inputmethodservice.KeyboardView
import android.util.AttributeSet
import android.view.View
import android.view.Window
import android.widget.EditText
import com.lodz.android.corekt.anko.hideInputMethod
import com.lodz.android.corekt.anko.replaceOriginalKeyboard
import com.lodz.android.corekt.anko.startAnim
import com.lodz.android.pandora.R

/**
 * 基础键盘继承类
 * @author zhouL
 * @date 2021/2/23
 */
open class BaseKeyboardView : KeyboardView {

    /** 输入框控件 */
    internal var mEditText: EditText? = null

    /** 点击完成监听器 */
    internal var mOnClickFinishListener: OnClickFinishListener? = null
    /** 焦点监听器 */
    internal var mOnEditTextFocusChangeListener: OnFocusChangeListener? = null
    /** 触摸监听器 */
    internal var mOnEditTextTouchListener: OnTouchListener? = null


    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    /** 绑定输入框控件 */
    fun initView(window: Window, editText: EditText) {
        mEditText = editText

        isEnabled = true
        isPreviewEnabled = true
        onKeyboardActionListener = object : OnKeyboardActionListener {
            override fun onPress(primaryCode: Int) {
                onActionPress(primaryCode)
            }

            override fun onRelease(primaryCode: Int) {
                onActionRelease(primaryCode)
            }

            override fun onText(text: CharSequence?) {
                onActionText(text)
            }

            override fun swipeLeft() {
                onActionSwipeLeft()
            }

            override fun swipeRight() {
                onActionSwipeRight()
            }

            override fun swipeDown() {
                onActionSwipeDown()
            }

            override fun swipeUp() {
                onActionSwipeUp()
            }

            override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
                onActionKey(primaryCode, keyCodes)
            }
        }
        isPreviewEnabled = false
        mEditText?.replaceOriginalKeyboard(window)
        setKeyboardChange()
    }

    open fun onActionPress(primaryCode: Int) {}

    open fun onActionRelease(primaryCode: Int) {}

    open fun onActionText(text: CharSequence?) {}

    open fun onActionSwipeLeft() {}

    open fun onActionSwipeRight() {}

    open fun onActionSwipeDown() {}

    open fun onActionSwipeUp() {}

    open fun onActionKey(primaryCode: Int, keyCodes: IntArray?) {}

    @SuppressLint("ClickableViewAccessibility")
    fun setKeyboardChange() {
        mEditText?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showKeyboard()
                mEditText?.hideInputMethod()
            } else {
                goneKeyboard()
            }
            mOnEditTextFocusChangeListener?.onFocusChange(v, hasFocus)
        }

        mEditText?.setOnTouchListener { v, event ->
            if (mEditText?.hasFocus() == true) {
                showKeyboard()
                mEditText?.hideInputMethod()
            }
            mOnEditTextTouchListener?.onTouch(v, event) ?: false
        }
    }

    /** 显示键盘 */
    fun showKeyboard(){
        if (visibility != View.VISIBLE){
            startAnim(context, R.anim.pandora_anim_bottom_in, View.VISIBLE)
        }
    }

    /** 隐藏键盘 */
    fun goneKeyboard(){
        if (visibility == View.VISIBLE){
            startAnim(context, R.anim.pandora_anim_bottom_out, View.GONE)
        }
    }

    /** 占位隐藏键盘 */
    fun invisibleKeyboard(){
        if (visibility == View.VISIBLE){
            startAnim(context, R.anim.pandora_anim_bottom_out, View.INVISIBLE)
        }
    }

    /** 设置点击完成监听器[listener] */
    fun setOnClickFinishListener(listener: OnClickFinishListener?) {
        mOnClickFinishListener = listener
    }

    /** 设置EditText焦点监听器[listener] */
    fun setOnEditTextFocusChangeListener(listener: OnFocusChangeListener?) {
        mOnEditTextFocusChangeListener = listener
    }

    /** 设置EditText触摸监听器[listener] */
    fun setOnEditTextTouchListener(listener: OnTouchListener?) {
        mOnEditTextTouchListener = listener
    }

    fun interface OnClickFinishListener {
        fun onClick(content: String)
    }

}