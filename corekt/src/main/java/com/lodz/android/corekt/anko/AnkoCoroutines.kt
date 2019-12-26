package com.lodz.android.corekt.anko

import kotlinx.coroutines.*

/**
 * 协程扩展类
 * @author zhouL
 * @date 2019/5/15
 */

/** 主线程执行 */
fun GlobalScope.runOnMain(action: () -> Unit): Job = launch(Dispatchers.Main) { action() }

/** 主线程执行捕获异常 */
@JvmOverloads
fun GlobalScope.runOnMainCatch(action: () -> Unit, error: (e: Exception) -> Unit = {}): Job =
    launch(Dispatchers.Main) {
        try {
            action()
        } catch (e: Exception) {
            e.printStackTrace()
            error(e)
        }
    }

/** 主线程延迟[timeMillis]毫秒执行 */
fun GlobalScope.runOnMainDelay(timeMillis: Long, action: () -> Unit): Job =
    launch(Dispatchers.IO) {
        delay(timeMillis)
        GlobalScope.launch(Dispatchers.Main) {
            action()
        }
    }

/** 异步线程执行 */
fun GlobalScope.runOnIO(actionIO: () -> Unit): Job = launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行捕获异常 */
@JvmOverloads
fun GlobalScope.runOnIOCatch(actionIO: () -> Unit, error: (e: Exception) -> Unit = {}): Job =
    launch(Dispatchers.IO) {
        try {
            actionIO()
        } catch (e: Exception) {
            e.printStackTrace()
            GlobalScope.runOnMain { error(e) }
        }
    }

/** 异步线程执行挂起函数 */
fun GlobalScope.runOnSuspendIO(actionIO: suspend () -> Unit): Job = launch(Dispatchers.IO) { actionIO() }

/** 异步线程执行挂起函数捕获异常 */
@JvmOverloads
fun GlobalScope.runOnSuspendIOCatch(
    actionIO: suspend () -> Unit,
    error: (e: Exception) -> Unit = {}
): Job =
    launch(Dispatchers.IO) {
        try {
            actionIO()
        } catch (e: Exception) {
            e.printStackTrace()
            GlobalScope.runOnMain { error(e) }
        }
    }

suspend fun <T> GlobalScope.awaitOrNull(action: () -> T?): T? =
    withContext(Dispatchers.IO) {
        action()
    }

suspend fun <T> GlobalScope.await(action: () -> T): T =
    withContext(Dispatchers.IO) {
        action()
    }