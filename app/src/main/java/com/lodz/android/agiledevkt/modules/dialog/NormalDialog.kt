package com.lodz.android.agiledevkt.modules.dialog

import android.content.Context
import android.view.View
import com.lodz.android.agiledevkt.databinding.DialogNormalBinding
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.dialog.BaseDialog

/**
 * 普通弹框测试类
 * Created by zhouL on 2018/11/1.
 */
class NormalDialog(context: Context) : BaseDialog(context) {

    private val mBinding : DialogNormalBinding by bindingLayout(DialogNormalBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root
}