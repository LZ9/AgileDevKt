package com.lodz.android.agiledevkt.modules.viewbinding

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.lodz.android.agiledevkt.databinding.DialogViewBindingBinding
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * ViewBinding弹框测试
 * @author zhouL
 * @date 2021/7/21
 */
class ViewBindingDialog(context: Context) : Dialog(context) {

    private val mBinding: DialogViewBindingBinding by bindingLayout(DialogViewBindingBinding::inflate)

    init {
        init()
    }

    private fun init() {
        setContentView(mBinding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(false)
        mBinding.closeBtn.setOnClickListener {
            dismiss()
        }
    }
}