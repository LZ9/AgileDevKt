package com.lodz.android.pandora.utils.coroutines

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lodz.android.corekt.anko.IoScope
import com.lodz.android.corekt.anko.getMetaData
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.corekt.anko.runOnMainCatch
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.application.BaseApplication
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
    action: (t: T) -> Unit,
    actionTokenUnauth: (t: T) -> Unit = {},
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> }
): Job = IoScope().launch {
    try {
        val res = request.invoke()
        resHandle(action, actionTokenUnauth, res)
    } catch (e: Exception) {
        errorHandle(e, error)
    }
}

/** 网络接口使用的协程挂起方法，主要对接口进行判断处理（ViewModel） */
fun <T> ViewModel.runOnSuspendIORes(
    request: suspend () -> T,
    action: (t: T) -> Unit,
    actionTokenUnauth: (t: T) -> Unit = {},
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> }
): Job = viewModelScope.launch(Dispatchers.IO) {
    try {
        val res = request.invoke()
        resHandle(action, actionTokenUnauth, res)
    } catch (e: Exception) {
        errorHandle(e, error)
    }
}

/** 网络接口使用的协程方法，主要对接口进行判断处理 */
fun <T> runOnIORes(
    request: Deferred<T>,
    action: (t: T) -> Unit,
    actionTokenUnauth: (t: T) -> Unit = {},
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> }
): Job = IoScope().launch {
    try {
        val res = request.await()
        resHandle(action, actionTokenUnauth, res)
    } catch (e: Exception) {
        errorHandle(e, error)
    }
}

/** 网络接口使用的协程方法，主要对接口进行判断处理（ViewModel） */
fun <T> ViewModel.runOnIORes(
    request: Deferred<T>,
    action: (t: T) -> Unit,
    actionTokenUnauth: (t: T) -> Unit = {},
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> }
): Job = viewModelScope.launch(Dispatchers.IO) {
    try {
        val res = request.await()
        resHandle(action, actionTokenUnauth, res)
    } catch (e: Exception) {
        errorHandle(e, error)
    }
}

/** 带加载框的协程挂起方法 */
fun <T> runOnSuspendIOPg(
    progressDialog: AlertDialog,
    request: suspend () -> T,
    action: (t: T) -> Unit,
    actionTokenUnauth: (t: T) -> Unit = {},
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> },
    pgCancel: () -> Unit = {}
) {
    runOnMainCatch({ progressDialog.show() })

    val job = IoScope().launch {
        try {
            val res = request.invoke()
            resHandle(action, actionTokenUnauth, res)
        } catch (e: Exception) {
            errorHandle(e, error)
        } finally {
            runOnMainCatch({ progressDialog.dismiss() })
        }
    }

    progressDialog.setOnCancelListener {
        job.cancel()
        runOnMainCatch({
            progressDialog.dismiss()
            pgCancel()
        })
    }
}

/** 带加载框的协程挂起方法（ViewModel） */
fun <T> ViewModel.runOnSuspendIOPg(
    progressDialog: AlertDialog,
    request: suspend () -> T,
    action: (t: T) -> Unit,
    actionTokenUnauth: (t: T) -> Unit = {},
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> },
    pgCancel: () -> Unit = {}
) {
    runOnMainCatch({ progressDialog.show() })

    val job = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = request.invoke()
            resHandle(action, actionTokenUnauth, res)
        } catch (e: Exception) {
            errorHandle(e, error)
        } finally {
            runOnMainCatch({ progressDialog.dismiss() })
        }
    }

    progressDialog.setOnCancelListener {
        job.cancel()
        runOnMainCatch({
            progressDialog.dismiss()
            pgCancel()
        })
    }
}

/** 带加载框的协程方法 */
fun <T> runOnIOPg(
    progressDialog: AlertDialog,
    request: Deferred<T>,
    action: (t: T) -> Unit,
    actionTokenUnauth: (t: T) -> Unit = {},
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> },
    pgCancel: () -> Unit = {}
) {
    runOnMainCatch({ progressDialog.show() })

    val job = IoScope().launch {
        try {
            val res = request.await()
            resHandle(action, actionTokenUnauth, res)
        } catch (e: Exception) {
            errorHandle(e, error)
        } finally {
            runOnMainCatch({ progressDialog.dismiss() })
        }
    }

    progressDialog.setOnCancelListener {
        job.cancel()
        runOnMainCatch({
            progressDialog.dismiss()
            pgCancel()
        })
    }
}

/** 带加载框的协程方法（ViewModel） */
fun <T> ViewModel.runOnIOPg(
    progressDialog: AlertDialog,
    request: Deferred<T>,
    action: (t: T) -> Unit,
    actionTokenUnauth: (t: T) -> Unit = {},
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> },
    pgCancel: () -> Unit = {}
) {
    runOnMainCatch({ progressDialog.show() })

    val job = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = request.await()
            resHandle(action, actionTokenUnauth, res)
        } catch (e: Exception) {
            errorHandle(e, error)
        } finally {
            runOnMainCatch({ progressDialog.dismiss() })
        }
    }

    progressDialog.setOnCancelListener {
        job.cancel()
        runOnMainCatch({
            progressDialog.dismiss()
            pgCancel()
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
    action: (t: T) -> Unit,
    actionTokenUnauth: (t: T) -> Unit = {},
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> },
    pgCancel: () -> Unit = {}
) {
    val progressDialog = ProgressDialogHelper.get()
        .setCanceledOnTouchOutside(canceledOnTouchOutside)
        .setCancelable(cancelable)
        .setMsg(msg)
        .create(context)

    runOnMainCatch({ progressDialog.show() })

    val job = IoScope().launch {
        try {
            val res = request.invoke()
            resHandle(action, actionTokenUnauth, res)
        } catch (e: Exception) {
            errorHandle(e, error)
        } finally {
            runOnMainCatch({ progressDialog.dismiss() })
        }
    }

    progressDialog.setOnCancelListener {
        job.cancel()
        runOnMainCatch({
            progressDialog.dismiss()
            pgCancel()
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
    actionIO: (t: T) -> Unit,
    actionTokenUnauth: (t: T) -> Unit = {},
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> },
    pgCancel: () -> Unit = {}
) {
    val progressDialog = ProgressDialogHelper.get()
        .setCanceledOnTouchOutside(canceledOnTouchOutside)
        .setCancelable(cancelable)
        .setMsg(msg)
        .create(context)

    runOnMainCatch({ progressDialog.show() })

    val job = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = request.invoke()
            resHandle(actionIO, actionTokenUnauth, res)
        } catch (e: Exception) {
            errorHandle(e, error)
        } finally {
            runOnMainCatch({ progressDialog.dismiss() })
        }
    }

    progressDialog.setOnCancelListener {
        job.cancel()
        runOnMainCatch({
            progressDialog.dismiss()
            pgCancel()
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
    actionIO: (t: T) -> Unit,
    actionTokenUnauth: (t: T) -> Unit = {},
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> },
    pgCancel: () -> Unit = {}
) {
    val progressDialog = ProgressDialogHelper.get()
        .setCanceledOnTouchOutside(canceledOnTouchOutside)
        .setCancelable(cancelable)
        .setMsg(msg)
        .create(context)

    runOnMainCatch({ progressDialog.show() })

    val job = IoScope().launch {
        try {
            val res = request.await()
            resHandle(actionIO, actionTokenUnauth, res)
        } catch (e: Exception) {
            errorHandle(e, error)
        } finally {
            runOnMainCatch({ progressDialog.dismiss() })
        }
    }

    progressDialog.setOnCancelListener {
        job.cancel()
        runOnMainCatch({
            progressDialog.dismiss()
            pgCancel()
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
    actionIO: (t: T) -> Unit,
    actionTokenUnauth: (t: T) -> Unit = {},
    error: (e: Exception, isNetwork: Boolean) -> Unit = { e, isNetwork -> },
    pgCancel: () -> Unit = {}
) {
    val progressDialog = ProgressDialogHelper.get()
        .setCanceledOnTouchOutside(canceledOnTouchOutside)
        .setCancelable(cancelable)
        .setMsg(msg)
        .create(context)

    runOnMainCatch({ progressDialog.show() })

    val job = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = request.await()
            resHandle(actionIO, actionTokenUnauth, res)
        } catch (e: Exception) {
            errorHandle(e, error)
        } finally {
            runOnMainCatch({ progressDialog.dismiss() })
        }
    }

    progressDialog.setOnCancelListener {
        job.cancel()
        runOnMainCatch({
            progressDialog.dismiss()
            pgCancel()
        })
    }
}

/** 处理异步结果 */
private fun <T> resHandle(action: (t: T) -> Unit, actionTokenUnauth: (t: T) -> Unit, res: T) {
    if (res is ResponseStatus) {
        if (res.isTokenUnauth()) {
            runOnMain { actionTokenUnauth(res) }
            return
        }
        if (res.isSuccess()) {
            runOnMain { action(res) }
            return
        }
        throw ExceptionFactory.createDataException(res)
    }
    runOnMain { action(res) }
}

/** 处理异常逻辑 */
private fun errorHandle(e: Exception, error: (e: Exception, isNetwork: Boolean) -> Unit) {
    e.printStackTrace()
    printTagLog(e)
    if (e !is CancellationException) {
        runOnMain { error(e, ExceptionFactory.isNetworkError(e)) }
    }
}

/** 根据配置标签打印异常[t]日志 */
private fun printTagLog(t: Throwable) {
    val app = BaseApplication.get() ?: return
    val tag = app.getMetaData(BaseApplication.ERROR_TAG)
    if (tag != null && tag is String) {
        if (tag.isNotEmpty()) {
            PrintLog.e(tag, t.toString(), t)
        }
    }
}