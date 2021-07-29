package com.lodz.android.agiledevkt.modules.dialog

import android.content.Context
import android.view.View
import com.lodz.android.agiledevkt.databinding.DialogCenterBinding
import com.lodz.android.pandora.utils.viewbinding.bindingLayoutLazy
import com.lodz.android.pandora.widget.dialog.BaseCenterDialog

/**
 * 居中弹框测试类
 * Created by zhouL on 2018/11/1.
 */
class CenterDialog(context: Context) : BaseCenterDialog(context) {

    private val mBinding : DialogCenterBinding by bindingLayoutLazy(DialogCenterBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root
}