package com.lodz.android.agiledevkt.modules.dialog

import android.content.Context
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.createColorDrawable
import com.lodz.android.pandora.widget.dialog.BaseRightDialog

/**
 * 右侧弹框测试类
 * Created by zhouL on 2018/11/1.
 */
class RightDialog(context: Context) : BaseRightDialog(context, R.style.NoDimDialog) {
    override fun getLayoutId(): Int = R.layout.dialog_right
    override fun findViews() {
        setElevation(16f, context.createColorDrawable(R.color.white))
    }

    override fun initData() {
        super.initData()
        setCanceledOnTouchOutside(false)
    }

}