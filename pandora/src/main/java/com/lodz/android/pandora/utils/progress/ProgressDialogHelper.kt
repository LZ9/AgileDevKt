package com.lodz.android.pandora.utils.progress

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import com.lodz.android.corekt.anko.getDrawableCompat
import com.lodz.android.pandora.R

/**
 * 加载框帮助类
 * Created by zhouL on 2018/7/6.
 */
class ProgressDialogHelper private constructor() {

    /** 返回键关闭 */
    private var mCancelable = true
    /** 点击空白处取消 */
    private var mCanceledOnTouchOutside = false
    /** 加载提示语 */
    private var mMsg = ""
    /** 取消监听器 */
    private var mListener: DialogInterface.OnCancelListener? = null
    /** 加载动画资源 */
    @DrawableRes
    private var mIndeterminateDrawable: Int = 0

    companion object {
        fun get(): ProgressDialogHelper = ProgressDialogHelper()
    }

    /** 设置返回键可关闭[cancelable] */
    fun setCancelable(cancelable: Boolean): ProgressDialogHelper {
        mCancelable = cancelable
        return this
    }

    /** 设置点击空白处可取消[canceledOnTouchOutside] */
    fun setCanceledOnTouchOutside(canceledOnTouchOutside: Boolean): ProgressDialogHelper {
        mCanceledOnTouchOutside = canceledOnTouchOutside
        return this
    }

    /** 设置加载提示语[msg] */
    fun setMsg(msg: String): ProgressDialogHelper {
        mMsg = msg
        return this
    }

    /** 设置取消监听器[listener] */
    fun setOnCancelListener(listener: DialogInterface.OnCancelListener): ProgressDialogHelper {
        mListener = listener
        return this
    }

    /** 设置加载动画资源[resId] */
    fun setIndeterminateDrawable(@DrawableRes resId: Int): ProgressDialogHelper {
        mIndeterminateDrawable = resId
        return this
    }

    /** 创建加载库，上下文[context] */
    @SuppressLint("InflateParams")
    fun create(context: Context): AlertDialog {
        val view = LayoutInflater.from(context).inflate(R.layout.pandora_view_progress, null)
        val progressDialog = AlertDialog.Builder(context, R.style.ProgressStyle)
                .setView(view)
                .create()
        if (mMsg.isNotEmpty()) {
            val tv = view.findViewById<TextView>(R.id.msg)
            tv.visibility = View.VISIBLE
            tv.text = mMsg
        }
        if (mIndeterminateDrawable != 0) {
            val pb = view.findViewById<ProgressBar>(R.id.progress_bar)
            pb.indeterminateDrawable = context.getDrawableCompat(mIndeterminateDrawable)
        }
        progressDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside)
        progressDialog.setCancelable(mCancelable)
        if (mListener != null) {
            progressDialog.setOnCancelListener(mListener)
        }
        val wd = progressDialog.window
        if (wd != null) {
            wd.setGravity(Gravity.CENTER)
        }
        return progressDialog
    }
}