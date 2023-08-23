package com.lodz.android.corekt.anko

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
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
@JvmOverloads
fun runOnMain(scope: CoroutineScope = MainScope(), action: suspend () -> Unit): Job = scope.launch(Dispatchers.Main)  { action() }

/** 主线程执行（ViewModel） */
fun ViewModel.runOnMain(action: suspend () -> Unit): Job = viewModelScope.launch(Dispatchers.Main) { action() }

/** 主线程执行（AppCompatActivity） */
fun AppCompatActivity.runOnMain(action: suspend () -> Unit): Job = lifecycleScope.launch(Dispatchers.Main) { action() }

/** 主线程执行（Fragment） */
fun Fragment.runOnMain(action: suspend () -> Unit): Job = lifecycleScope.launch(Dispatchers.Main) { action() }

/** 主线程执行捕获异常 */
@JvmOverloads
fun runOnMainCatch(
    action: suspend () -> Unit,
    error: (e: Exception) -> Unit = {},
    scope: CoroutineScope = MainScope()
): Job = scope.launch(Dispatchers.Main) {
    try {
        action()
    } catch (e: Exception) {
        e.printStackTrace()
        error(e)
    }
}

/** 主线程执行捕获异常（ViewModel） */
@JvmOverloads
fun ViewModel.runOnMainCatch(
    action: suspend () -> Unit,
    error: (e: Exception) -> Unit = {}
): Job = viewModelScope.launch(Dispatchers.Main) {
    try {
        action()
    } catch (e: Exception) {
        e.printStackTrace()
        error(e)
    }
}

/** 主线程执行捕获异常（AppCompatActivity） */
@JvmOverloads
fun AppCompatActivity.runOnMainCatch(
    action: suspend () -> Unit,
    error: (e: Exception) -> Unit = {}
): Job = lifecycleScope.launch(Dispatchers.Main) {
    try {
        action()
    } catch (e: Exception) {
        e.printStackTrace()
        error(e)
    }
}

/** 主线程执行捕获异常（Fragment） */
@JvmOverloads
fun Fragment.runOnMainCatch(
    action: suspend () -> Unit,
    error: (e: Exception) -> Unit = {}
): Job = lifecycleScope.launch(Dispatchers.Main) {
    try {
        action()
    } catch (e: Exception) {
        e.printStackTrace()
        error(e)
    }
}

/** 主线程延迟[timeMillis]毫秒执行 */
@JvmOverloads
fun runOnMainDelay(
    timeMillis: Long,
    scope: CoroutineScope = IoScope(),
    action: suspend () -> Unit
): Job = scope.launch(Dispatchers.Main) {
    delay(timeMillis)
    launch(Dispatchers.Main) { action() }
}

/** 主线程延迟[timeMillis]毫秒执行（ViewModel） */
fun ViewModel.runOnMainDelay(
    timeMillis: Long,
    action: suspend () -> Unit
): Job = viewModelScope.launch(Dispatchers.IO) {
    delay(timeMillis)
    launch(Dispatchers.Main) { action() }
}

/** 主线程延迟[timeMillis]毫秒执行（AppCompatActivity） */
fun AppCompatActivity.runOnMainDelay(
    timeMillis: Long,
    action: suspend () -> Unit
): Job = lifecycleScope.launch(Dispatchers.IO) {
    delay(timeMillis)
    launch(Dispatchers.Main) { action() }
}

/** 主线程延迟[timeMillis]毫秒执行（Fragment） */
fun Fragment.runOnMainDelay(
    timeMillis: Long,
    action: suspend () -> Unit
): Job = lifecycleScope.launch(Dispatchers.IO) {
    delay(timeMillis)
    launch(Dispatchers.Main) { action() }
}

/** 异步线程执行 */
@JvmOverloads
fun runOnIO(scope: CoroutineScope = IoScope(), actionIO: suspend () -> Unit): Job = scope.launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行（ViewModel） */
fun ViewModel.runOnIO(actionIO: suspend () -> Unit): Job = viewModelScope.launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行（AppCompatActivity） */
fun AppCompatActivity.runOnIO(actionIO: suspend () -> Unit): Job = lifecycleScope.launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行（Fragment） */
fun Fragment.runOnIO(actionIO: suspend () -> Unit): Job = lifecycleScope.launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行捕获异常 */
@JvmOverloads
fun runOnIOCatch(
    actionIO: suspend () -> Unit,
    error: (e: Exception) -> Unit = {},
    scope: CoroutineScope = IoScope()
): Job = scope.launch(Dispatchers.IO) {
    try {
        actionIO()
    } catch (e: Exception) {
        e.printStackTrace()
        launch(Dispatchers.Main) { error(e) }
    }
}

/** 异步线程执行捕获异常（ViewModel） */
@JvmOverloads
fun ViewModel.runOnIOCatch(
    actionIO: suspend () -> Unit,
    error: (e: Exception) -> Unit = {}
): Job = viewModelScope.launch(Dispatchers.IO) {
    try {
        actionIO()
    } catch (e: Exception) {
        e.printStackTrace()
        launch(Dispatchers.Main) { error(e) }
    }
}

/** 异步线程执行捕获异常（AppCompatActivity） */
@JvmOverloads
fun AppCompatActivity.runOnIOCatch(
    actionIO: suspend () -> Unit,
    error: (e: Exception) -> Unit = {}
): Job = lifecycleScope.launch(Dispatchers.IO) {
    try {
        actionIO()
    } catch (e: Exception) {
        e.printStackTrace()
        launch(Dispatchers.Main) { error(e) }
    }
}

/** 异步线程执行捕获异常（Fragment） */
@JvmOverloads
fun Fragment.runOnIOCatch(
    actionIO: suspend () -> Unit,
    error: (e: Exception) -> Unit = {}
): Job = lifecycleScope.launch(Dispatchers.IO) {
    try {
        actionIO()
    } catch (e: Exception) {
        e.printStackTrace()
        launch(Dispatchers.Main) { error(e) }
    }
}

/** 在主线程执行挂起函数[action]（如果挂起函数耗时则会阻断主线程） */
fun <T> awaitOrNull(action: suspend () -> T?): T? = runBlocking { action() }

/** 在主线程执行挂起函数[action]（如果挂起函数耗时则会阻断主线程） */
fun <T> await(action: suspend () -> T): T = runBlocking { action() }