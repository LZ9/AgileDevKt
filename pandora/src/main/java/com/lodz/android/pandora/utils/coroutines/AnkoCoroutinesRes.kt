package com.lodz.android.pandora.utils.coroutines

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lodz.android.corekt.anko.IoScope
import com.lodz.android.corekt.anko.getMetaData
import com.lodz.android.corekt.anko.runOnMainCatch
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.application.BaseApplication
import com.lodz.android.pandora.action.ApiAction
import com.lodz.android.pandora.action.ApiPgAction
import com.lodz.android.pandora.rx.exception.ExceptionFactory
import com.lodz.android.pandora.rx.status.ResponseStatus
import com.lodz.android.pandora.utils.progress.ProgressDialogHelper
import kotlinx.coroutines.*

/**
 * 协程接口扩展类
 * @author zhouL
 * @date 2019/11/27
 */

/** 网络接口使用的协程挂起方法，主要对接口进行判断处理 */
fun <T> runOnSuspendIORes(
    request: suspend () -> T,
    action: ApiAction<T>.() -> Unit
): Job = IoScope().launch {
    val listener = ApiAction<T>().also(action)
    launch(Dispatchers.Main) { listener.mStartAction?.invoke() }
    try {
        val res = request.invoke()
        resHandle(this, res, listener)
    } catch (e: Exception) {
        errorHandle(this, e, listener)
    }
}

/** 网络接口使用的协程挂起方法，主要对接口进行判断处理（ViewModel） */
fun <T> ViewModel.runOnSuspendIORes(
    request: suspend () -> T,
    action: ApiAction<T>.() -> Unit
): Job = viewModelScope.launch(Dispatchers.IO) {
    val listener = ApiAction<T>().also(action)
    launch(Dispatchers.Main) { listener.mStartAction?.invoke() }
    try {
        val res = request.invoke()
        resHandle(this, res, listener)
    } catch (e: Exception) {
        errorHandle(this, e, listener)
    }
}

/** 网络接口使用的协程方法，主要对接口进行判断处理 */
fun <T> runOnIORes(
    request: Deferred<T>,
    action: ApiAction<T>.() -> Unit
): Job = IoScope().launch {
    val listener = ApiAction<T>().also(action)
    launch(Dispatchers.Main) { listener.mStartAction?.invoke() }
    try {
        val res = request.await()
        resHandle(this, res, listener)
    } catch (e: Exception) {
        errorHandle(this, e, listener)
    }
}

/** 网络接口使用的协程方法，主要对接口进行判断处理（ViewModel） */
fun <T> ViewModel.runOnIORes(
    request: Deferred<T>,
    action: ApiAction<T>.() -> Unit
): Job = viewModelScope.launch(Dispatchers.IO) {
    val listener = ApiAction<T>().also(action)
    launch(Dispatchers.Main) { listener.mStartAction?.invoke() }
    try {
        val res = request.await()
        resHandle(this, res, listener)
    } catch (e: Exception) {
        errorHandle(this, e, listener)
    }
}

/** 带加载框的协程挂起方法 */
fun <T> runOnSuspendIOPg(
    progressDialog: AlertDialog,
    request: suspend () -> T,
    action: ApiPgAction<T>.() -> Unit
) {
    val listener = ApiPgAction<T>().also(action)
    runOnMainCatch({ progressDialog.show() })

    val job = IoScope().launch {
        launch(Dispatchers.Main) { listener.mStartAction?.invoke() }
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
            progressDialog.dismiss()
            listener.mPgCancelAction?.invoke()
        })
    }
}

/** 带加载框的协程挂起方法（ViewModel） */
fun <T> ViewModel.runOnSuspendIOPg(
    progressDialog: AlertDialog,
    request: suspend () -> T,
    action: ApiPgAction<T>.() -> Unit
) {
    val listener = ApiPgAction<T>().also(action)
    runOnMainCatch({ progressDialog.show() })

    val job = viewModelScope.launch(Dispatchers.IO) {
        launch(Dispatchers.Main) { listener.mStartAction?.invoke() }
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
            progressDialog.dismiss()
            listener.mPgCancelAction?.invoke()
        })
    }
}

/** 带加载框的协程方法 */
fun <T> runOnIOPg(
    progressDialog: AlertDialog,
    request: Deferred<T>,
    action: ApiPgAction<T>.() -> Unit
) {
    val listener = ApiPgAction<T>().also(action)
    runOnMainCatch({ progressDialog.show() })

    val job = IoScope().launch {
        launch(Dispatchers.Main) { listener.mStartAction?.invoke() }
        try {
            val res = request.await()
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
            progressDialog.dismiss()
            listener.mPgCancelAction?.invoke()
        })
    }
}

/** 带加载框的协程方法（ViewModel） */
fun <T> ViewModel.runOnIOPg(
    progressDialog: AlertDialog,
    request: Deferred<T>,
    action: ApiPgAction<T>.() -> Unit
) {
    val listener = ApiPgAction<T>().also(action)
    runOnMainCatch({ progressDialog.show() })

    val job = viewModelScope.launch(Dispatchers.IO) {
        launch(Dispatchers.Main) { listener.mStartAction?.invoke() }
        try {
            val res = request.await()
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
            progressDialog.dismiss()
            listener.mPgCancelAction?.invoke()
        })
    }
}

/** 带加载框的协程挂起方法 */
fun <T> runOnSuspendIOPg(
    context: Context,
    msg: String = "正在加载，请稍候",
    cancelable: Boolean = true,
    canceledOnTouchOutside: Boolean = false,
    request: suspend () -> T,
    action: ApiPgAction<T>.() -> Unit
) {
    val listener = ApiPgAction<T>().also(action)
    val progressDialog = ProgressDialogHelper.get()
        .setCanceledOnTouchOutside(canceledOnTouchOutside)
        .setCancelable(cancelable)
        .setMsg(msg)
        .create(context)

    runOnMainCatch({ progressDialog.show() })

    val job = IoScope().launch {
        launch(Dispatchers.Main) { listener.mStartAction?.invoke() }
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
            progressDialog.dismiss()
            listener.mPgCancelAction?.invoke()
        })
    }
}

/** 带加载框的协程挂起方法（ViewModel） */
fun <T> ViewModel.runOnSuspendIOPg(
    context: Context,
    msg: String = "正在加载，请稍候",
    cancelable: Boolean = true,
    canceledOnTouchOutside: Boolean = false,
    request: suspend () -> T,
    action: ApiPgAction<T>.() -> Unit
) {
    val listener = ApiPgAction<T>().also(action)
    val progressDialog = ProgressDialogHelper.get()
        .setCanceledOnTouchOutside(canceledOnTouchOutside)
        .setCancelable(cancelable)
        .setMsg(msg)
        .create(context)

    runOnMainCatch({ progressDialog.show() })

    val job = viewModelScope.launch(Dispatchers.IO) {
        launch(Dispatchers.Main) { listener.mStartAction?.invoke() }
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
            progressDialog.dismiss()
            listener.mPgCancelAction?.invoke()
        })
    }
}

/** 带加载框的协程方法 */
fun <T> runOnIOPg(
    context: Context,
    msg: String = "正在加载，请稍候",
    cancelable: Boolean = true,
    canceledOnTouchOutside: Boolean = false,
    request: Deferred<T>,
    action: ApiPgAction<T>.() -> Unit
) {
    val listener = ApiPgAction<T>().also(action)
    val progressDialog = ProgressDialogHelper.get()
        .setCanceledOnTouchOutside(canceledOnTouchOutside)
        .setCancelable(cancelable)
        .setMsg(msg)
        .create(context)

    runOnMainCatch({ progressDialog.show() })

    val job = IoScope().launch {
        launch(Dispatchers.Main) { listener.mStartAction?.invoke() }
        try {
            val res = request.await()
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
            progressDialog.dismiss()
            listener.mPgCancelAction?.invoke()
        })
    }
}

/** 带加载框的协程方法（ViewModel） */
fun <T> ViewModel.runOnIOPg(
    context: Context,
    msg: String = "正在加载，请稍候",
    cancelable: Boolean = true,
    canceledOnTouchOutside: Boolean = false,
    request: Deferred<T>,
    action: ApiPgAction<T>.() -> Unit
) {
    val listener = ApiPgAction<T>().also(action)
    val progressDialog = ProgressDialogHelper.get()
        .setCanceledOnTouchOutside(canceledOnTouchOutside)
        .setCancelable(cancelable)
        .setMsg(msg)
        .create(context)

    runOnMainCatch({ progressDialog.show() })

    val job = viewModelScope.launch(Dispatchers.IO) {
        launch(Dispatchers.Main) { listener.mStartAction?.invoke() }
        try {
            val res = request.await()
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
            progressDialog.dismiss()
            listener.mPgCancelAction?.invoke()
        })
    }
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