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
fun runOnMain(scope: CoroutineScope = MainScope(), action: () -> Unit): Job = scope.launch(Dispatchers.Main)  { action() }

/** 主线程执行（ViewModel） */
fun ViewModel.runOnMain(action: () -> Unit): Job = viewModelScope.launch(Dispatchers.Main) { action() }

/** 主线程执行（AppCompatActivity） */
fun AppCompatActivity.runOnMain(action: () -> Unit): Job = lifecycleScope.launch(Dispatchers.Main) { action() }

/** 主线程执行（Fragment） */
fun Fragment.runOnMain(action: () -> Unit): Job = lifecycleScope.launch(Dispatchers.Main) { action() }

/** 主线程执行捕获异常 */
@JvmOverloads
fun runOnMainCatch(
    action: () -> Unit,
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
    action: () -> Unit,
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
    action: () -> Unit,
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
    action: () -> Unit,
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
    action: () -> Unit
): Job = scope.launch(Dispatchers.Main) {
    delay(timeMillis)
    launch(Dispatchers.Main) { action() }
}

/** 主线程延迟[timeMillis]毫秒执行（ViewModel） */
fun ViewModel.runOnMainDelay(
    timeMillis: Long,
    action: () -> Unit
): Job = viewModelScope.launch(Dispatchers.IO) {
    delay(timeMillis)
    launch(Dispatchers.Main) { action() }
}

/** 主线程延迟[timeMillis]毫秒执行（AppCompatActivity） */
fun AppCompatActivity.runOnMainDelay(
    timeMillis: Long,
    action: () -> Unit
): Job = lifecycleScope.launch(Dispatchers.IO) {
    delay(timeMillis)
    launch(Dispatchers.Main) { action() }
}

/** 主线程延迟[timeMillis]毫秒执行（Fragment） */
fun Fragment.runOnMainDelay(
    timeMillis: Long,
    action: () -> Unit
): Job = lifecycleScope.launch(Dispatchers.IO) {
    delay(timeMillis)
    launch(Dispatchers.Main) { action() }
}

/** 异步线程执行 */
@JvmOverloads
fun runOnIO(scope: CoroutineScope = IoScope(), actionIO: () -> Unit): Job = scope.launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行（ViewModel） */
fun ViewModel.runOnIO(actionIO: () -> Unit): Job = viewModelScope.launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行（AppCompatActivity） */
fun AppCompatActivity.runOnIO(actionIO: () -> Unit): Job = lifecycleScope.launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行（Fragment） */
fun Fragment.runOnIO(actionIO: () -> Unit): Job = lifecycleScope.launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行捕获异常 */
@JvmOverloads
fun runOnIOCatch(
    actionIO: () -> Unit,
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
    actionIO: () -> Unit,
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
    actionIO: () -> Unit,
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
    actionIO: () -> Unit,
    error: (e: Exception) -> Unit = {}
): Job = lifecycleScope.launch(Dispatchers.IO) {
    try {
        actionIO()
    } catch (e: Exception) {
        e.printStackTrace()
        launch(Dispatchers.Main) { error(e) }
    }
}

/** 异步线程执行挂起函数 */
@JvmOverloads
fun runOnSuspendIO(scope: CoroutineScope = IoScope(), actionIO: suspend () -> Unit): Job = scope.launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行挂起函数（ViewModel） */
fun ViewModel.runOnSuspendIO(actionIO: suspend () -> Unit): Job = viewModelScope.launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行挂起函数（AppCompatActivity） */
fun AppCompatActivity.runOnSuspendIO(actionIO: suspend () -> Unit): Job = lifecycleScope.launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行挂起函数（Fragment） */
fun Fragment.runOnSuspendIO(actionIO: suspend () -> Unit): Job = lifecycleScope.launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行挂起函数捕获异常 */
@JvmOverloads
fun runOnSuspendIOCatch(
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

/** 异步线程执行挂起函数捕获异常（ViewModel） */
@JvmOverloads
fun ViewModel.runOnSuspendIOCatch(
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

/** 异步线程执行挂起函数捕获异常（AppCompatActivity） */
@JvmOverloads
fun AppCompatActivity.runOnSuspendIOCatch(
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

/** 异步线程执行挂起函数捕获异常（Fragment） */
@JvmOverloads
fun Fragment.runOnSuspendIOCatch(
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

suspend fun <T> awaitOrNull(action: suspend () -> T?): T? =
    withContext(Dispatchers.IO) {
        action()
    }

suspend fun <T> await(action: suspend () -> T): T =
    withContext(Dispatchers.IO) {
        action()
    }