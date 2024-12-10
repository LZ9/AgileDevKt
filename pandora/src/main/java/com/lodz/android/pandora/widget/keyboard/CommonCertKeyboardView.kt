package com.lodz.android.pandora.widget.keyboard

import android.content.Context
import android.inputmethodservice.Keyboard
import android.util.AttributeSet
import com.lodz.android.pandora.R
import java.util.*

/**
 * 通用证件键盘
 * @author zhouL
 * @date 2021/2/23
 */
open class CommonCertKeyboardView : BaseKeyboardView {

    /** 通用证件键盘 */
    private val mCommonCertKeyboard by lazy {
        Keyboard(context, R.xml.pandora_keyboard_common_number_letter)
    }

    /** 是否大写 */
    private var isUperCase = true

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        keyboard = mCommonCertKeyboard
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

        if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 切换到大小写
            uperCase()
            keyboard = mCommonCertKeyboard
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

    /** 键盘大小写切换 */
    private fun uperCase(){
        val keylist = mCommonCertKeyboard.keys
        if (isUperCase) {
            //大写转小写
            for (key in keylist) {
                if (key.label != null && isWord(key.label.toString())){
                    key.label = key.label.toString().lowercase(Locale.CHINA)
                    key.codes[0] = key.codes[0] + 32
                }
            }
            isUperCase = false
        } else {
            // 小写转大写
            for (key in keylist) {
                if (key.label != null && isWord(key.label.toString())){
                    key.label = key.label.toString().uppercase(Locale.CHINA)
                    key.codes[0] = key.codes[0] - 32
                }
            }
            isUperCase = true
        }
    }

    /** 是否是文字 */
    private fun isWord(str: String): Boolean = "abcdefghijklmnopqrstuvwxyz".contains(str.lowercase(Locale.CHINA))

}