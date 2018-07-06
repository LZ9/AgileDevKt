package com.lodz.android.componentkt.rx.subscribe.observer

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.lodz.android.componentkt.R
import com.lodz.android.corekt.utils.UiHandler
import io.reactivex.disposables.Disposable

/**
 * 展示加载框的订阅者（无背压）
 * Created by zhouL on 2018/7/5.
 */
abstract class ProgressObserver<T> : RxObserver<T>() {

    final override fun onRxSubscribe(d: Disposable) {
        showProgress()
        onPgSubscribe(d)
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
    fun create(context: Context): ProgressObserver<T> = create(context, "", true)

    /** 创建加载框，配置取消参数[cancelable] */
    fun create(context: Context, cancelable: Boolean): ProgressObserver<T> = create(context, "", cancelable)

    /** 创建加载框，配置提示文字资源[strResId]和取消参数[cancelable] */
    fun create(context: Context, @StringRes strResId: Int, cancelable: Boolean): ProgressObserver<T> = create(context, context.getString(strResId), cancelable)

    /** 创建自定义加载框[dialog] */
    fun create(dialog: AlertDialog): ProgressObserver<T> {
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
    fun create(context: Context, msg: String, cancelable: Boolean): ProgressObserver<T> {
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
        val view = LayoutInflater.from(context).inflate(R.layout.componentkt_view_progress_layout, null)
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
        return progressDialog
    }

    /** 取消加载框 */
    private fun cancelDialog() {// 用户关闭
        dispose()
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

    override fun onDispose() {// 开发者取消
        dismissProgress()
    }

    open fun onPgSubscribe(d: Disposable) {}

    abstract fun onPgNext(any: T)

    abstract fun onPgError(e: Throwable, isNetwork: Boolean)

    open fun onPgComplete() {}

    /** 用户取消回调 */
    open fun onPgCancel() {}
}