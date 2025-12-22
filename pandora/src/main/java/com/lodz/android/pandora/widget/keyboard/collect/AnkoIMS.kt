package com.lodz.android.pandora.widget.keyboard.collect

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.lodz.android.corekt.anko.checkInputMethodMode
import com.lodz.android.corekt.anko.goInputMethodSetting
import com.lodz.android.corekt.anko.isInputMethodEnabled
import com.lodz.android.corekt.anko.showInputMethodPicker
import com.lodz.android.pandora.R

/**
 * 输入法服务扩展类
 * @author zhouL
 * @date 2025/12/22
 */

/** 在输入框获取焦点时校验和提醒用户是否切换采集专用输入法 */
fun EditText.checkInputMethodOnFocusChange(listener: ((view: View, hasFocus: Boolean) -> Unit)? = null) {
    this.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if (hasFocus) {
            if (context.isInputMethodEnabled(CollectInputMethodService::class.java.name)) {
                if (!context.checkInputMethodMode(CollectInputMethodService::class.java.name)) {
                    context.showInputMethodPicker()
                }
            } else {
                AlertDialog.Builder(context)
                    .setTitle(R.string.pandora_im_go_setting_title)
                    .setMessage(R.string.pandora_im_go_setting_message)
                    .setPositiveButton(R.string.pandora_im_go) { dialog, which ->
                        context.goInputMethodSetting()
                        dialog.dismiss()
                        this.clearFocus()
                    }
                    .setNegativeButton(R.string.pandora_im_cancel) { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
        listener?.invoke(v, hasFocus)
    }
}

/** 设置输入完成监听器[listener] */
fun EditText.setOnClickFinishListener(listener: (text: String) -> Unit) {
    this.setOnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            listener.invoke(v.text.toString())
            return@setOnEditorActionListener true
        }
        return@setOnEditorActionListener false
    }
}