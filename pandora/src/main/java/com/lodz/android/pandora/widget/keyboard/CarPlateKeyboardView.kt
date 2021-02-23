package com.lodz.android.pandora.widget.keyboard

import android.content.Context
import android.inputmethodservice.Keyboard
import android.util.AttributeSet
import com.lodz.android.pandora.R

/**
 * 车牌键盘
 * @author zhouL
 * @date 2021/2/23
 */
open class CarPlateKeyboardView : BaseKeyboardView {

    /** 切换到车牌地区代码 */
    private val KEYCODE_CAR_AREA = -9
    /** 切换到车牌数字字母 */
    private val KEYCODE_CAR_NUM_LETTER = -8

    /** 车牌地区键盘 */
    private val mCarAreaKeyboard by lazy {
        Keyboard(context, R.xml.pandora_keyboard_car_area)
    }
    /** 车牌数字字母键盘 */
    private val mCarNumLetterKeyboard by lazy {
        Keyboard(context, R.xml.pandora_keyboard_car_number_letter)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        keyboard = mCarAreaKeyboard
    }

    override fun onActionKey(primaryCode: Int, keyCodes: IntArray?) {
        super.onActionKey(primaryCode, keyCodes)
        val editText = mEditText ?: return

        val selectionStart = editText.selectionStart
        val selectionEnd = editText.selectionEnd

        if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
            goneKeyboard()
            mOnClickFinishListener?.onClick(editText.text.toString())
            return
        }

        if (primaryCode == Keyboard.KEYCODE_DELETE) {// 删除
            if (editText.text.isEmpty()) {
                return
            }
            if (selectionStart < selectionEnd) {//选中部分文字
                editText.text.delete(selectionStart, selectionEnd)
                return
            }
            if (selectionStart > 0) {//光标不在第一位
                editText.text.delete(selectionStart - 1, selectionStart)
            }
            return
        }

        if (primaryCode == KEYCODE_CAR_AREA) {// 车牌数字字母切到车牌地区
            keyboard = mCarAreaKeyboard
            return
        }

        if (primaryCode == KEYCODE_CAR_NUM_LETTER) {// 车牌数字字母切到车牌地区
            keyboard = mCarNumLetterKeyboard
            return
        }

        if (selectionStart < selectionEnd) { //先删除选中的文字，再插入
            editText.text.delete(selectionStart, selectionEnd)
        }
        var text = ""
        try {
            text = primaryCode.toChar().toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (text.isNotEmpty()) {
            editText.text.insert(selectionStart, text)
        }
        if (keyboard == mCarAreaKeyboard) {//点击了车牌地区文字后转换到数字字母
            onActionKey(KEYCODE_CAR_NUM_LETTER, null)
        }
    }

    /** 切换地区显示 */
    fun showCarArea(){
        onActionKey(KEYCODE_CAR_AREA, null)
    }

    /** 切换数字英文显示 */
    fun showCarNum(){
        onActionKey(KEYCODE_CAR_NUM_LETTER, null)
    }
}