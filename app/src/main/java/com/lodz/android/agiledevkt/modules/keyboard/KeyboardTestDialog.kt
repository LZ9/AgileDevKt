package com.lodz.android.agiledevkt.modules.keyboard

import android.content.Context
import android.view.View
import com.lodz.android.agiledevkt.databinding.DialogKeyboardTestBinding
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.dialog.BaseDialog
import com.lodz.android.pandora.widget.keyboard.collect.checkInputMethodOnFocusChange
import com.lodz.android.pandora.widget.keyboard.collect.setOnClickFinishListener

/**
 * 显示自定义键盘测试弹窗
 * @author zhouL
 * @date 2025/12/17
 */
class KeyboardTestDialog(context: Context) : BaseDialog(context) {

    private val mBinding: DialogKeyboardTestBinding by bindingLayout(DialogKeyboardTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews() {
        super.findViews()
        mBinding.idcardCltedt.getEditText().checkInputMethodOnFocusChange()
        mBinding.carplateCltedt.getEditText().checkInputMethodOnFocusChange()
        mBinding.commonCertCltedt.getEditText().checkInputMethodOnFocusChange()
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.idcardCltedt.getEditText().setOnClickFinishListener {
            toastShort(it)
        }

        mBinding.carplateCltedt.getEditText().setOnClickFinishListener {
            toastShort(it)
        }

        mBinding.commonCertCltedt.getEditText().setOnClickFinishListener {
            toastShort(it)
        }
    }
}