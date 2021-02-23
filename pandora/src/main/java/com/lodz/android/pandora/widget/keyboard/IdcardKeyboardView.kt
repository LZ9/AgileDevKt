package com.lodz.android.pandora.widget.keyboard

import android.content.Context
import android.inputmethodservice.Keyboard
import android.util.AttributeSet
import com.lodz.android.pandora.R

/**
 * 大陆身份证键盘
 * @author zhouL
 * @date 2021/2/23
 */
open class IdcardKeyboardView : BaseKeyboardView {

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        keyboard =  Keyboard(context, R.xml.pandora_keyboard_idcard)
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
    }
}