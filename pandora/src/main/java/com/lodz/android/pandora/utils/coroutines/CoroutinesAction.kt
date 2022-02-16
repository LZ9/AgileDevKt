package com.lodz.android.pandora.utils.coroutines

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lodz.android.corekt.anko.IoScope
import com.lodz.android.corekt.anko.getMetaData
import com.lodz.android.corekt.anko.runOnMainCatch
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.action.ApiAction
import com.lodz.android.pandora.action.ApiPgAction
import com.lodz.android.pandora.base.application.BaseApplication
import com.lodz.android.pandora.rx.exception.ExceptionFactory
import com.lodz.android.pandora.rx.status.ResponseStatus
import com.lodz.android.pandora.utils.progress.ProgressDialogHelper
import kotlinx.coroutines.*

/**
 * 协程请求操作类
 * @author zhouL
 * @date 2022/2/16
 */
class CoroutinesAction<T>(
    private val mViewModel: ViewModel? = null,
    private val mRequestFun: (suspend () -> T)? = null,
    private val mRequestDeferred: Deferred<T>? = null
) {

    /** 执行协程操作[action] */
    fun action(action: ApiAction<T>.() -> Unit): Job {
        if (mViewModel != null) {
            if (mRequestFun != null) {
                return execute(mViewModel.viewModelScope, action) { mRequestFun.invoke() }
            }
            if (mRequestDeferred != null) {
                return execute(mViewModel.viewModelScope, action) { mRequestDeferred.await() }
            }
            throw NullPointerException("request parameter is null")
        }

        if (mRequestFun != null) {
            return execute(IoScope(), action) { mRequestFun.invoke() }
        }
        if (mRequestDeferred != null) {
            return execute(IoScope(), action) { mRequestDeferred.await() }
        }
        throw NullPointerException("request parameter is null")
    }

    private fun execute(
        scope: CoroutineScope,
        action: ApiAction<T>.() -> Unit,
        request: suspend () -> T
    ): Job = scope.launch(Dispatchers.IO) {
        val listener = ApiAction<T>().also(action)
        launch(Dispatchers.Main) { listener.mStartAction?.invoke() }
        try {
            val res = request.invoke()
            resHandle(this, res, listener)
        } catch (e: Exception) {
            errorHandle(this, e, listener)
        }
    }

    /** 执行带加载框[progressDialog]的协程操作[action] */
    fun actionPg(progressDialog: AlertDialog, action: ApiPgAction<T>.() -> Unit): Job {
        if (mViewModel != null) {
            if (mRequestFun != null) {
                return execute(mViewModel.viewModelScope, progressDialog, action) { mRequestFun.invoke() }
            }
            if (mRequestDeferred != null) {
                return execute(mViewModel.viewModelScope, progressDialog, action) { mRequestDeferred.await() }
            }
            throw NullPointerException("request parameter is null")
        }

        if (mRequestFun != null) {
            return execute(IoScope(), progressDialog, action) { mRequestFun.invoke() }
        }
        if (mRequestDeferred != null) {
            return execute(IoScope(), progressDialog, action) { mRequestDeferred.await() }
        }
        throw NullPointerException("request parameter is null")
    }

    /** 执行默认加载框的协程操作[action] */
    fun actionPg(
        context: Context,
        msg: String = "正在加载，请稍候",
        cancelable: Boolean = true,
        canceledOnTouchOutside: Boolean = false,
        action: ApiPgAction<T>.() -> Unit
    ): Job {
        val progressDialog = ProgressDialogHelper.get()
            .setCanceledOnTouchOutside(canceledOnTouchOutside)
            .setCancelable(cancelable)
            .setMsg(msg)
            .create(context)
        return actionPg(progressDialog, action)
    }

    private fun execute(
        scope: CoroutineScope,
        progressDialog: AlertDialog,
        action: ApiPgAction<T>.() -> Unit,
        request: suspend () -> T,
    ): Job {
        val listener = ApiPgAction<T>().also(action)
        runOnMainCatch({
            progressDialog.show()
            listener.mStartAction?.invoke()
        })

        val job = scope.launch(Dispatchers.IO) {
            try {
                val res = request.invoke()
                resHandle(this, res, listener)
            } catch (e: Exception) {
                errorHandle(this, e, listener)
            } finally {
                runOnMainCatch({ progressDialog.dismiss() })
            }
        }

        progressDialog.setOnCancelListener {
            job.cancel()
            runOnMainCatch({
                it.dismiss()
                listener.mPgCancelAction?.invoke()
            })
        }
        return job
    }

    /** 处理异步结果 */
    private fun <T> resHandle(scope: CoroutineScope, res: T, action: ApiAction<T>) {
        scope.launch(Dispatchers.Main) {
            if (res is ResponseStatus) {
                if (res.isTokenUnauth()) {
                    action.mTokenUnauthAction?.invoke(res)
                    return@launch
                }
                if (res.isSuccess()) {
                    action.mSuccessAction?.invoke(res)
                    action.mCompleteAction?.invoke()
                    return@launch
                }
                errorHandle(scope, ExceptionFactory.createDataException(res), action)
                return@launch
            }
            action.mSuccessAction?.invoke(res)
            action.mCompleteAction?.invoke()
        }
    }

    /** 处理异常逻辑 */
    private fun <T> errorHandle(scope: CoroutineScope, e: Exception, action: ApiAction<T>) {
        e.printStackTrace()
        printErrorLog(e)
        if (e !is CancellationException) {
            scope.launch(Dispatchers.Main) { action.mErrorAction?.invoke(e, ExceptionFactory.isNetworkError(e)) }
        }
    }

    /** 根据配置标签打印异常[t]日志 */
    private fun printErrorLog(t: Throwable) {
        val app = BaseApplication.get() ?: return
        val tag = app.getMetaData(BaseApplication.ERROR_TAG)
        if (tag != null && tag is String) {
            if (tag.isNotEmpty()) {
                PrintLog.e(tag, t.toString(), t)
            }
        }
    }
}