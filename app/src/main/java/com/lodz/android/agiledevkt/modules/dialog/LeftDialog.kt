package com.lodz.android.agiledevkt.modules.dialog

import android.content.Context
import android.view.View
import com.lodz.android.agiledevkt.databinding.DialogLeftBinding
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.dialog.BaseLeftDialog

/**
 * 左侧弹框测试类
 * Created by zhouL on 2018/11/1.
 */
class LeftDialog(context: Context) : BaseLeftDialog(context) {

    private val mBinding : DialogLeftBinding by bindingLayout(DialogLeftBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

}