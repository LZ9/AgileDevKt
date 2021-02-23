package com.lodz.android.agiledevkt.utils.keyboard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.View
import android.view.Window
import android.widget.EditText
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.hideInputMethod
import com.lodz.android.corekt.anko.replaceOriginalKeyboard
import com.lodz.android.corekt.anko.startAnim
import java.util.*

/**
 * 自定义键盘管理类
 * @author zhouL
 * @date 2021/2/22
 */
class KeyboardManager(
    private val mContext: Context,
    private val mWindow: Window,
    private val mKeyboardView: KeyboardView,
    private val mEditText: EditText,
    private val keyboardType: Int
) {

    companion object{
        /** 车牌键盘 */
        const val TYPE_CAR_NUM = 1
        /** 身份证键盘 */
        const val TYPE_ID_CARD = 2
        /** 通用证件键盘 */
        const val TYPE_COMMON_CERT = 3
    }

    /** 切换到车牌地区代码 */
    private val KEYCODE_CAR_AREA = -9
    /** 切换到车牌数字字母 */
    private val KEYCODE_CAR_NUM_LETTER = -8

    /** 车牌地区键盘 */
    private val mCarAreaKeyboard by lazy {
        Keyboard(mContext, R.xml.keyboard_car_area)
    }
    /** 车牌数字字母键盘 */
    private val mCarNumLetterKeyboard by lazy {
        Keyboard(mContext, R.xml.keyboard_car_number_letter)
    }
    /** 车牌数字字母键盘 */
    private val mIdcardKeyboard by lazy {
        Keyboard(mContext, R.xml.keyboard_idcard)
    }
    /** 通用全键盘 */
    private val mCommonFullKeyboard by lazy {
        Keyboard(mContext, R.xml.keyboard_common_number_letter)
    }

    /** 点击完成监听器 */
    private var mOnClickFinishListener: OnClickFinishListener? = null
    /** 是否大写 */
    private var isUperCase = true


    init {
        changeKeyboard(keyboardType)

        mKeyboardView.isEnabled = true
        mKeyboardView.isPreviewEnabled = true
        mKeyboardView.setOnKeyboardActionListener(object : KeyboardView.OnKeyboardActionListener {
            override fun onPress(primaryCode: Int) {}

            override fun onRelease(primaryCode: Int) {}

            override fun onText(text: CharSequence?) {}

            override fun swipeLeft() {}

            override fun swipeRight() {}

            override fun swipeDown() {}

            override fun swipeUp() {}

            override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
                val selectionStart = mEditText.selectionStart
                val selectionEnd = mEditText.selectionEnd

                if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                    goneKeyboard()
                    mOnClickFinishListener?.onClick(mEditText.text.toString())
                    return
                }

                if (primaryCode == Keyboard.KEYCODE_DELETE) {// 删除
                    if (mEditText.text.isEmpty()) {
                        return
                    }
                    if (selectionStart < selectionEnd) {//选中部分文字
                        mEditText.text.delete(selectionStart, selectionEnd)
                        return
                    }
                    if (selectionStart > 0) {//光标不在第一位
                        mEditText.text.delete(selectionStart - 1, selectionStart)
                    }
                    return
                }

                if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 切换到大小写
                    uperCase()
                    mKeyboardView.keyboard = mCommonFullKeyboard
                    return
                }

                if (primaryCode == KEYCODE_CAR_AREA) {// 车牌数字字母切到车牌地区
                    mKeyboardView.keyboard = mCarAreaKeyboard
                    return
                }

                if (primaryCode == KEYCODE_CAR_NUM_LETTER) {// 车牌数字字母切到车牌地区
                    mKeyboardView.keyboard = mCarNumLetterKeyboard
                    return
                }

                if (selectionStart < selectionEnd) { //先删除选中的文字，再插入
                    mEditText.text.delete(selectionStart, selectionEnd)
                }
                var text = ""
                try {
                    text = primaryCode.toChar().toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (text.isNotEmpty()) {
                    mEditText.text.insert(selectionStart, text)
                }
                if (mKeyboardView.keyboard == mCarAreaKeyboard) {//点击了车牌地区文字后转换到数字字母
                    onKey(KEYCODE_CAR_NUM_LETTER, null)
                }
            }
        })
        mKeyboardView.isPreviewEnabled = false
        mEditText.replaceOriginalKeyboard(mWindow)

        setKeyboardChange()
    }

    /** 根据键盘类型更改键盘[keyboardType] */
    fun changeKeyboard(keyboardType: Int) {
        when(keyboardType){
            TYPE_CAR_NUM -> mKeyboardView.keyboard = mCarNumLetterKeyboard // 车牌键盘
            TYPE_ID_CARD -> mKeyboardView.keyboard = mIdcardKeyboard // 身份证键盘
            TYPE_COMMON_CERT -> mKeyboardView.keyboard = mCommonFullKeyboard // 通用证件键盘
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setKeyboardChange() {
        mEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showKeyboard()
                mEditText.hideInputMethod()
            } else {
                goneKeyboard()
            }
        }

        mEditText.setOnTouchListener { v, event ->
            if (mEditText.hasFocus()){
                showKeyboard()
                mEditText.hideInputMethod()
            }
            false
        }
    }

    /** 键盘大小写切换 */
    private fun uperCase(){
        val keylist = mCommonFullKeyboard.keys
        if (isUperCase) {
            //大写转小写
            for (key in keylist) {
                if (key.label != null && isWord(key.label.toString())){
                    key.label = key.label.toString().toLowerCase(Locale.CHINA)
                    key.codes[0] = key.codes[0] + 32
                }
            }
            isUperCase = false
        } else {
            // 小写转大写
            for (key in keylist) {
                if (key.label != null && isWord(key.label.toString())){
                    key.label = key.label.toString().toUpperCase(Locale.CHINA)
                    key.codes[0] = key.codes[0] - 32
                }
            }
            isUperCase = true
        }
    }

    /** 是否是文字 */
    private fun isWord(str: String): Boolean = "abcdefghijklmnopqrstuvwxyz".contains(str.toLowerCase(Locale.CHINA))

    /** 显示键盘 */
    fun showKeyboard(){
        if (mKeyboardView.visibility != View.VISIBLE){
            mKeyboardView.startAnim(mContext, R.anim.pandora_anim_bottom_in, View.VISIBLE)
        }
    }

    /** 隐藏键盘 */
    fun goneKeyboard(){
        if (mKeyboardView.visibility == View.VISIBLE){
            mKeyboardView.startAnim(mContext, R.anim.pandora_anim_bottom_out, View.GONE)
        }
    }

    /** 占位隐藏键盘 */
    fun invisibleKeyboard(){
        if (mKeyboardView.visibility == View.VISIBLE){
            mKeyboardView.startAnim(mContext, R.anim.pandora_anim_bottom_out, View.INVISIBLE)
        }
    }

    /** 设置点击完成监听器[listener] */
    fun setOnClickFinishListener(listener: OnClickFinishListener?) {
        mOnClickFinishListener = listener
    }

    fun interface OnClickFinishListener {
        fun onClick(content: String)
    }
}