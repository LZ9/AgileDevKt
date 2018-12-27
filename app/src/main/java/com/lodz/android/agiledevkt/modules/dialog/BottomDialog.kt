package com.lodz.android.agiledevkt.modules.dialog

import android.content.Context
import android.os.Build
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.widget.dialog.BaseBottomDialog

/**
 * 底部弹框测试类
 * Created by zhouL on 2018/11/1.
 */
class BottomDialog(context: Context) : BaseBottomDialog(context) {

    /** 提示语 */
    private val mTipsTv by bindView<TextView>(R.id.tips_tv)
    /** 确定按钮 */
    private val mSureBtn by bindView<TextView>(R.id.sure_btn)


    override fun getLayoutId(): Int = R.layout.dialog_bottom

    override fun findViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTipsTv.elevation = 16f
            mSureBtn.elevation = 16f
        }
    }

    override fun setListeners() {
        super.setListeners()
        mSureBtn.setOnClickListener {
            dismiss()
        }
    }
}