package com.lodz.android.agiledevkt.modules.dialog

import android.content.Context
import com.lodz.android.agiledevkt.R
import com.lodz.android.pandora.widget.dialog.BaseLeftDialog

/**
 * 左侧弹框测试类
 * Created by zhouL on 2018/11/1.
 */
class LeftDialog(context: Context) : BaseLeftDialog(context) {
    override fun getLayoutId(): Int = R.layout.dialog_left
}