package com.lodz.android.corekt.anko

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

/**
 * 协程扩展类
 * @author zhouL
 * @date 2019/5/15
 */

@Suppress("FunctionName")
fun IoScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

/** 主线程执行 */
fun CoroutineScope.runOnMain(action: () -> Unit): Job = launch(Dispatchers.Main) { action() }

/** 主线程执行（ViewModel） */
fun ViewModel.runOnMain(action: () -> Unit): Job = viewModelScope.launch(Dispatchers.Main) { action() }

/** 主线程执行捕获异常 */
@JvmOverloads
fun CoroutineScope.runOnMainCatch(action: () -> Unit, error: (e: Exception) -> Unit = {}): Job =
    launch(Dispatchers.Main) {
        try {
            action()
        } catch (e: Exception) {
            e.printStackTrace()
            error(e)
        }
    }

/** 主线程执行捕获异常（ViewModel） */
@JvmOverloads
fun ViewModel.runOnMainCatch(action: () -> Unit, error: (e: Exception) -> Unit = {}): Job =
    viewModelScope.launch(Dispatchers.Main) {
        try {
            action()
        } catch (e: Exception) {
            e.printStackTrace()
            error(e)
        }
    }

/** 主线程延迟[timeMillis]毫秒执行 */
fun CoroutineScope.runOnMainDelay(timeMillis: Long, action: () -> Unit): Job =
    launch(Dispatchers.IO) {
        delay(timeMillis)
        launch(Dispatchers.Main) {
            action()
        }
    }

/** 主线程延迟[timeMillis]毫秒执行（ViewModel） */
fun ViewModel.runOnMainDelay(timeMillis: Long, action: () -> Unit): Job =
    viewModelScope.launch(Dispatchers.IO) {
        delay(timeMillis)
        launch(Dispatchers.Main) {
            action()
        }
    }

/** 异步线程执行 */
fun CoroutineScope.runOnIO(actionIO: () -> Unit): Job = launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行（ViewModel） */
fun ViewModel.runOnIO(actionIO: () -> Unit): Job = viewModelScope.launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行捕获异常 */
@JvmOverloads
fun CoroutineScope.runOnIOCatch(actionIO: () -> Unit, error: (e: Exception) -> Unit = {}): Job =
    launch(Dispatchers.IO) {
        try {
            actionIO()
        } catch (e: Exception) {
            e.printStackTrace()
            runOnMain { error(e) }
        }
    }

/** 异步线程执行捕获异常（ViewModel） */
@JvmOverloads
fun ViewModel.runOnIOCatch(actionIO: () -> Unit, error: (e: Exception) -> Unit = {}): Job =
    viewModelScope.launch(Dispatchers.IO) {
        try {
            actionIO()
        } catch (e: Exception) {
            e.printStackTrace()
            runOnMain { error(e) }
        }
    }

/** 异步线程执行挂起函数 */
fun CoroutineScope.runOnSuspendIO(actionIO: suspend () -> Unit): Job = launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行挂起函数（ViewModel） */
fun ViewModel.runOnSuspendIO(actionIO: suspend () -> Unit): Job = viewModelScope.launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行挂起函数捕获异常 */
@JvmOverloads
fun CoroutineScope.runOnSuspendIOCatch(
    actionIO: suspend () -> Unit,
    error: (e: Exception) -> Unit = {}
): Job =
    launch(Dispatchers.IO) {
        try {
            actionIO()
        } catch (e: Exception) {
            e.printStackTrace()
            runOnMain { error(e) }
        }
    }

/** 异步线程执行挂起函数捕获异常（ViewModel） */
@JvmOverloads
fun ViewModel.runOnSuspendIOCatch(
    actionIO: suspend () -> Unit,
    error: (e: Exception) -> Unit = {}
): Job =
    viewModelScope.launch(Dispatchers.IO) {
        try {
            actionIO()
        } catch (e: Exception) {
            e.printStackTrace()
            runOnMain { error(e) }
        }
    }

suspend fun <T> CoroutineScope.awaitOrNull(action: () -> T?): T? =
    withContext(Dispatchers.IO) {
        action()
    }

suspend fun <T> CoroutineScope.await(action: () -> T): T =
    withContext(Dispatchers.IO) {
        action()
    }