package com.lodz.android.agiledevkt.modules.dialog

import android.content.Context
import com.lodz.android.agiledevkt.R
import com.lodz.android.pandora.widget.dialog.BaseDialog

/**
 * 普通弹框测试类
 * Created by zhouL on 2018/11/1.
 */
class NormalDialog(context: Context) : BaseDialog(context) {
    override fun getLayoutId(): Int = R.layout.dialog_normal
}