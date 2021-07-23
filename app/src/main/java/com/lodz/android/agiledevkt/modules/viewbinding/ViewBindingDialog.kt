package com.lodz.android.agiledevkt.modules.viewbinding

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.lodz.android.agiledevkt.databinding.DialogViewBindingBinding

/**
 * ViewBinding弹框测试
 * @author zhouL
 * @date 2021/7/21
 */
class ViewBindingDialog(context: Context) : Dialog(context) {

    private lateinit var mBinding: DialogViewBindingBinding

    init {
        init()
    }

    private fun init() {
        mBinding = DialogViewBindingBinding.inflate(layoutInflater)
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