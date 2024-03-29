package com.lodz.android.pandora.rx.subscribe.subscriber

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.lodz.android.corekt.anko.runOnMainCatch
import com.lodz.android.pandora.utils.progress.ProgressDialogHelper
import org.reactivestreams.Subscription

/**
 * 展示加载框的订阅者（带背压）
 * Created by zhouL on 2018/7/6.
 */
abstract class ProgressSubscriber<T> : RxSubscriber<T>() {

    final override fun onRxSubscribe(s: Subscription?) {
        super.onRxSubscribe(s)
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
        super.onRxComplete()
        dismissProgress()
        onPgComplete()
    }

    /** 加载框 */
    private var mProgressDialog: AlertDialog? = null

    /** 创建自定义加载框[dialog] */
    fun create(dialog: AlertDialog): ProgressSubscriber<T> {
        try {
            mProgressDialog = dialog
            mProgressDialog?.setOnCancelListener {
                cancelDialog()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    /** 创建加载框，配置提示文字资源[strResId]和取消参数[cancelable] */
    @JvmOverloads
    fun create(context: Context, @StringRes strResId: Int, cancelable: Boolean = false, canceledOnTouchOutside: Boolean = false): ProgressSubscriber<T> =
            create(context, context.getString(strResId), cancelable, canceledOnTouchOutside)

    /** 创建加载框，配置提示文字[msg]和取消参数[cancelable] */
    @JvmOverloads
    fun create(context: Context, msg: String = "", cancelable: Boolean = false, canceledOnTouchOutside: Boolean = false): ProgressSubscriber<T> {
        try {
            mProgressDialog = getProgressDialog(context, msg, cancelable, canceledOnTouchOutside)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    /** 获取一个加载框 */
    private fun getProgressDialog(context: Context, msg: String, cancelable: Boolean, canceledOnTouchOutside: Boolean): AlertDialog =
        ProgressDialogHelper.get()
            .setCanceledOnTouchOutside(canceledOnTouchOutside)
            .setCancelable(cancelable)
            .setOnCancelListener(DialogInterface.OnCancelListener {
                cancelDialog()
            })
            .setMsg(msg)
            .create(context)

    /** 取消加载框 */
    private fun cancelDialog() {// 用户关闭
        cancel()
        onPgCancel()
        dismissProgress()
    }

    /** 显示加载框 */
    private fun showProgress() {
        runOnMainCatch({ mProgressDialog?.show() })
    }

    /** 关闭加载框 */
    private fun dismissProgress() {
        runOnMainCatch({
            mProgressDialog?.dismiss()
            mProgressDialog = null
        })
    }

    override fun onErrorEnd() {// 抛异常关闭
        super.onErrorEnd()
        dismissProgress()
    }

    override fun onCancel() {// 开发者关闭
        super.onCancel()
        dismissProgress()
    }

    open fun onPgSubscribe(s: Subscription?) {}

    abstract fun onPgNext(any: T)

    abstract fun onPgError(e: Throwable, isNetwork: Boolean)

    open fun onPgComplete() {}

    /** 用户取消回调 */
    open fun onPgCancel() {}

    companion object {
        /** 创建lambda调用 */
        @JvmStatic
        @JvmOverloads
        fun <T> action(
            context: Context,
            msg: String = "",
            cancelable: Boolean = true,
            canceledOnTouchOutside: Boolean = false,
            next: (any: T) -> Unit,
            error: (e: Throwable, isNetwork: Boolean) -> Unit = { _, _ -> }
        ): ProgressSubscriber<T> = object : ProgressSubscriber<T>() {
            override fun onPgNext(any: T) {
                next(any)
            }

            override fun onPgError(e: Throwable, isNetwork: Boolean) {
                error(e, isNetwork)
            }
        }.create(context, msg, cancelable, canceledOnTouchOutside)
    }
}