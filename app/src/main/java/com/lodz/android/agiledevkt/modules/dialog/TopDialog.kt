package com.lodz.android.agiledevkt.modules.dialog

import android.content.Context
import android.view.View
import com.lodz.android.agiledevkt.databinding.DialogTopBinding
import com.lodz.android.pandora.utils.viewbinding.bindingLayoutLazy
import com.lodz.android.pandora.widget.dialog.BaseTopDialog

/**
 * 顶部弹框测试类
 * Created by zhouL on 2018/11/2.
 */
class TopDialog(context: Context) : BaseTopDialog(context) {

    private val mBinding : DialogTopBinding by bindingLayoutLazy(DialogTopBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root
}