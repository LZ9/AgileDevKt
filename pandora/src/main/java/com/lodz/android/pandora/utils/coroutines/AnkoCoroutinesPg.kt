package com.lodz.android.pandora.utils.coroutines

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.lodz.android.corekt.anko.getMetaData
import com.lodz.android.corekt.anko.runOnMain
import com.lodz.android.corekt.anko.runOnMainCatch
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.application.BaseApplication
import com.lodz.android.pandora.utils.progress.ProgressDialogHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 协程加载框扩展类
 * @author zhouL
 * @date 2019/11/27
 */

fun GlobalScope.runOnSuspendIOCatchPg(
    progressDialog: AlertDialog,
    actionIO: suspend () -> Unit,
    error: (e: Exception) -> Unit = {},
    pgCancel: () -> Unit = {}
) {
    runOnMainCatch({ progressDialog.show() })

    val job = launch(Dispatchers.IO) {
        try {
            actionIO()
        } catch (e: Exception) {
            e.printStackTrace()
            printTagLog(e)
            if (e !is CancellationException) {
                runOnMain { error(e) }
            }
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

fun GlobalScope.runOnSuspendIOCatchPg(
    context: Context,
    msg: String = "",
    cancelable: Boolean = true,
    canceledOnTouchOutside: Boolean = false,
    actionIO: suspend () -> Unit,
    error: (e: Exception) -> Unit = {},
    pgCancel: () -> Unit = {}
) {
    val progressDialog = ProgressDialogHelper.get()
        .setCanceledOnTouchOutside(canceledOnTouchOutside)
        .setCancelable(cancelable)
        .setMsg(msg)
        .create(context)

    runOnMainCatch({ progressDialog.show() })

    val job = launch(Dispatchers.IO) {
        try {
            actionIO()
        } catch (e: Exception) {
            e.printStackTrace()
            printTagLog(e)
            if (e !is CancellationException) {
                runOnMain { error(e) }
            }
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

/** 打印标签日志 */
private fun printTagLog(t: Throwable) {
    val app = BaseApplication.get() ?: return
    val tag = app.getMetaData(BaseApplication.ERROR_TAG)
    if (tag != null && tag is String) {
        if (tag.isNotEmpty()) {
            PrintLog.e(tag, t.toString(), t)
        }
    }
}