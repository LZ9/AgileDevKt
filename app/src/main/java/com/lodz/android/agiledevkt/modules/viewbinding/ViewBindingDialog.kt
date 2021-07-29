package com.lodz.android.agiledevkt.modules.viewbinding

import android.content.Context
import android.view.View
import com.lodz.android.agiledevkt.databinding.DialogViewBindingBinding
import com.lodz.android.pandora.utils.viewbinding.bindingLayoutLazy
import com.lodz.android.pandora.widget.dialog.BaseDialog

/**
 * ViewBinding弹框测试
 * @author zhouL
 * @date 2021/7/21
 */
class ViewBindingDialog(context: Context) : BaseDialog(context) {

    private val mBinding: DialogViewBindingBinding by bindingLayoutLazy(DialogViewBindingBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun setListeners() {
        super.setListeners()
        mBinding.closeBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun initData() {
        super.initData()
        setCanceledOnTouchOutside(false)
    }
}