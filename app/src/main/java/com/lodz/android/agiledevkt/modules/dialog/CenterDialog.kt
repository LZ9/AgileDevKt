package com.lodz.android.agiledevkt.modules.dialog

import android.content.Context
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.widget.dialog.BaseCenterDialog

/**
 * 居中弹框测试类
 * Created by zhouL on 2018/11/1.
 */
class CenterDialog(context: Context) : BaseCenterDialog(context) {
    override fun getLayoutId(): Int = R.layout.dialog_center
    override fun findViews() {}
}