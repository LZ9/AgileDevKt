package com.lodz.android.agiledevkt.modules.splash

import android.content.Context
import android.content.DialogInterface
import android.support.annotation.StringRes
import android.view.View
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.widget.dialog.BaseDialog
import com.lodz.android.corekt.anko.bindView

/**
 * 核对弹框
 * Created by zhouL on 2018/9/28.
 */
class CheckDialog(context: Context) : BaseDialog(context) {

    private val mContentTv by bindView<TextView>(R.id.content_tv)

    private val mNegativeBtn by bindView<TextView>(R.id.negative_btn)

    private val mPositiveBtn by bindView<TextView>(R.id.positive_btn)

    override fun getLayoutId() = R.layout.dialog_check

    override fun findViews() {}

    /** 设置内容文本[content] */
    fun setContentMsg(content: String) {
        mContentTv.text = content
    }

    /** 设置内容文本资源id[resId] */
    fun setContentMsg(@StringRes resId: Int) {
        mContentTv.text = context.getString(resId)
    }

    /** 设置确认按钮文本[str]和点击监听器[listener] */
    fun setPositiveText(str: String, listener: DialogInterface.OnClickListener? = null) {
        setBtn(mPositiveBtn, str, listener)
    }

    /** 设置确认按钮文本资源id[resId]和点击监听器[listener] */
    fun setPositiveText(@StringRes resId: Int, listener: DialogInterface.OnClickListener? = null) {
        setBtn(mPositiveBtn, context.getString(resId), listener)
    }

    /** 设置确认按钮文本[str]和点击监听器[listener] */
    fun setNegativeText(str: String, listener: DialogInterface.OnClickListener? = null) {
        setBtn(mNegativeBtn, str, listener)
    }

    /** 设置确认按钮文本资源id[resId]和点击监听器[listener] */
    fun setNegativeText(@StringRes resId: Int, listener: DialogInterface.OnClickListener? = null) {
        setBtn(mNegativeBtn, context.getString(resId), listener)
    }

    private fun setBtn(btn: TextView, str: String, listener: DialogInterface.OnClickListener?) {
        btn.visibility = View.VISIBLE
        btn.text = str
        if (listener != null) {
            btn.setOnClickListener {
                listener.onClick(getDialogInterface(), btn.id)
            }
        }
    }

}