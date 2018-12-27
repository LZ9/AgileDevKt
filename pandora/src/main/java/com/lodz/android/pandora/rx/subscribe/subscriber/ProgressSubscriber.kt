package com.lodz.android.pandora.rx.subscribe.subscriber

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.lodz.android.corekt.utils.UiHandler
import com.lodz.android.pandora.R
import org.reactivestreams.Subscription

/**
 * 展示加载框的订阅者（带背压）
 * Created by zhouL on 2018/7/6.
 */
abstract class ProgressSubscriber<T> : RxSubscriber<T>() {

    final override fun onRxSubscribe(s: Subscription) {
        showProgress()
        onPgSubscribe(s)
    }

    final override fun onRxNext(any: T) {
        onPgNext(any)
    }

    final override fun onRxError(e: Throwable, isNetwork: Boolean) {
        onPgError(e, isNetwork)
    }

    final override fun onRxComplete() {
        dismissProgress()
        onPgComplete()
    }

    /** 加载框 */
    var mProgressDialog: AlertDialog? = null

    /** 创建默认加载框 */
    fun create(context: Context): ProgressSubscriber<T> = create(context, "", true)

    /** 创建加载框，配置取消参数[cancelable] */
    fun create(context: Context, cancelable: Boolean): ProgressSubscriber<T> = create(context, "", cancelable)

    /** 创建加载框，配置提示文字资源[strResId]和取消参数[cancelable] */
    fun create(context: Context, @StringRes strResId: Int, cancelable: Boolean): ProgressSubscriber<T> = create(context, context.getString(strResId), cancelable)

    /** 创建自定义加载框[dialog] */
    fun create(dialog: AlertDialog): ProgressSubscriber<T> {
        try {
            mProgressDialog = dialog
            mProgressDialog!!.setOnCancelListener {
                cancelDialog()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    /** 创建加载框，配置提示文字[msg]和取消参数[cancelable] */
    fun create(context: Context, msg: String, cancelable: Boolean): ProgressSubscriber<T> {
        try {
            mProgressDialog = getProgressDialog(context, msg, cancelable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    /** 获取一个加载框 */
    @SuppressLint("InflateParams")
    private fun getProgressDialog(context: Context, msg: String, cancelable: Boolean): AlertDialog {
        val view = LayoutInflater.from(context).inflate(R.layout.pandora_view_progress, null)
        val progressDialog = AlertDialog.Builder(context, R.style.ProgressStyle)
                .setView(view)
                .create()
        if (!msg.isEmpty()) {
            val msgTv = view.findViewById<TextView>(R.id.msg)
            msgTv.visibility = View.VISIBLE
            msgTv.text = msg
        }
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setCancelable(cancelable)
        progressDialog.setOnCancelListener {
            cancelDialog()
        }
        val wd = progressDialog.window
        if (wd != null) {
            wd.setGravity(Gravity.CENTER)
        }
        return progressDialog
    }

    /** 取消加载框 */
    private fun cancelDialog() {// 用户关闭
        cancel()
        onPgCancel()
        dismissProgress()
    }

    /** 显示加载框 */
    private fun showProgress() {
        UiHandler.post(Runnable {
            try {
                if (mProgressDialog != null) {
                    mProgressDialog!!.show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    /** 关闭加载框 */
    private fun dismissProgress() {
        UiHandler.post(Runnable {
            try {
                if (mProgressDialog != null) {
                    mProgressDialog!!.dismiss()
                    mProgressDialog = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    override fun onErrorEnd() {// 抛异常关闭
        dismissProgress()
    }

    override fun onCancel() {// 开发者关闭
        dismissProgress()
    }

    open fun onPgSubscribe(s: Subscription) {}

    abstract fun onPgNext(any: T)

    abstract fun onPgError(e: Throwable, isNetwork: Boolean)

    open fun onPgComplete() {}

    /** 用户取消回调 */
    open fun onPgCancel() {}
}