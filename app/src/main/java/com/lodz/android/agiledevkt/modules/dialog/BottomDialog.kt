package com.lodz.android.agiledevkt.modules.dialog

import android.content.Context
import android.view.View
import com.lodz.android.agiledevkt.databinding.DialogBottomBinding
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.dialog.BaseBottomDialog

/**
 * 底部弹框测试类
 * Created by zhouL on 2018/11/1.
 */
class BottomDialog(context: Context) : BaseBottomDialog(context) {

    private val mBinding : DialogBottomBinding by bindingLayout(DialogBottomBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews() {
        mBinding.tipsTv.elevation = 16f
        mBinding.sureBtn.elevation = 16f
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.sureBtn.setOnClickListener {
            dismiss()
        }
    }
}