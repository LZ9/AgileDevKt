package com.lodz.android.agiledevkt.modules.dialog

import android.content.Context
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.DialogRightBinding
import com.lodz.android.corekt.anko.createColorDrawable
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.dialog.BaseRightDialog

/**
 * 右侧弹框测试类
 * Created by zhouL on 2018/11/1.
 */
class RightDialog(context: Context) : BaseRightDialog(context, R.style.NoDimDialog) {

    private val mBinding : DialogRightBinding by bindingLayout(DialogRightBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews() {
        setElevation(16f, context.createColorDrawable(R.color.white))
    }

    override fun initData() {
        super.initData()
        setCanceledOnTouchOutside(false)
    }

}