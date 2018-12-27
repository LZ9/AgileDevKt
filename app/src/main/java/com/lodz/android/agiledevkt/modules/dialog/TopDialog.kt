package com.lodz.android.agiledevkt.modules.dialog

import android.content.Context
import com.lodz.android.agiledevkt.R
import com.lodz.android.pandora.widget.dialog.BaseTopDialog

/**
 * 顶部弹框测试类
 * Created by zhouL on 2018/11/2.
 */
class TopDialog(context: Context) : BaseTopDialog(context) {
    override fun getLayoutId(): Int = R.layout.dialog_top
}