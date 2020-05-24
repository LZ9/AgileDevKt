package com.lodz.android.pandora.rx.subscribe.observer

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.lodz.android.corekt.anko.runOnMainCatch
import com.lodz.android.pandora.utils.progress.ProgressDialogHelper
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.GlobalScope

/**
 * 展示加载框的订阅者（无背压）
 * Created by zhouL on 2018/7/5.
 */
abstract class ProgressObserver<T> : RxObserver<T>() {

    final override fun onRxSubscribe(d: Disposable) {
        super.onRxSubscribe(d)
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
        super.onRxComplete()
        dismissProgress()
        onPgComplete()
    }

    /** 加载框 */
    private var mProgressDialog: AlertDialog? = null

    /** 创建自定义加载框[dialog] */
    fun create(dialog: AlertDialog): ProgressObserver<T> {
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
    fun create(context: Context, @StringRes strResId: Int, cancelable: Boolean = true, canceledOnTouchOutside: Boolean = false): ProgressObserver<T> =
            create(context, context.getString(strResId), cancelable, canceledOnTouchOutside)

    /** 创建加载框，配置提示文字[msg]和返回键关闭[cancelable]默认true，点击空白关闭[canceledOnTouchOutside]默认false */
    @JvmOverloads
    fun create(context: Context, msg: String= "", cancelable: Boolean = true, canceledOnTouchOutside: Boolean = false): ProgressObserver<T> {
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
        dispose()
        onPgCancel()
        dismissProgress()
    }

    /** 显示加载框 */
    private fun showProgress() {
        GlobalScope.runOnMainCatch({ mProgressDialog?.show() })
    }

    /** 关闭加载框 */
    private fun dismissProgress() {
        GlobalScope.runOnMainCatch({
            mProgressDialog?.dismiss()
            mProgressDialog = null
        })
    }

    override fun onErrorEnd() {// 抛异常关闭
        super.onErrorEnd()
        dismissProgress()
    }

    override fun onDispose() {// 开发者取消
        super.onDispose()
        dismissProgress()
    }

    open fun onPgSubscribe(d: Disposable) {}

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
            error: (e: Throwable, isNetwork: Boolean) -> Unit = { _, _ -> },
            subscribe: (d: Disposable) -> Unit = {},
            errorEnd: () -> Unit = {},
            complete: () -> Unit = {},
            pgCancel: () -> Unit = {},
            dispose: () -> Unit = {}
        ): ProgressObserver<T> = object : ProgressObserver<T>() {

            override fun onPgSubscribe(d: Disposable) {
                super.onPgSubscribe(d)
                subscribe(d)
            }

            override fun onPgNext(any: T) {
                next(any)
            }

            override fun onPgError(e: Throwable, isNetwork: Boolean) {
                error(e, isNetwork)
            }

            override fun onErrorEnd() {
                super.onErrorEnd()
                errorEnd()
            }

            override fun onPgComplete() {
                super.onPgComplete()
                complete()
            }

            override fun onPgCancel() {
                super.onPgCancel()
                pgCancel()
            }

            override fun onDispose() {
                super.onDispose()
                dispose()
            }

        }.create(context, msg, cancelable, canceledOnTouchOutside)
    }
}