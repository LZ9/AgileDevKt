package com.lodz.android.corekt.anko

import kotlinx.coroutines.*

/**
 * 协程扩展类
 * @author zhouL
 * @date 2019/5/15
 */

/** 主线程执行 */
fun runOnMain(block: () -> Unit): Job = GlobalScope.launch(Dispatchers.Main) { block() }

/** 主线程执行捕获异常 */
@JvmOverloads
fun runOnMainCatch(block: () -> Unit, failure: (e: Exception) -> Unit = {}): Job =
    GlobalScope.launch(Dispatchers.Main) {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
            failure(e)
        }
    }

/** 主线程延迟[timeMillis]毫秒执行 */
fun runOnMainDelay(timeMillis: Long, block: () -> Unit): Job =
    GlobalScope.launch(Dispatchers.IO) {
        delay(timeMillis)
        GlobalScope.launch(Dispatchers.Main) {
            block()
        }
    }

/** 异步线程执行 */
fun runOnIO(block: () -> Unit): Job = GlobalScope.launch(Dispatchers.IO) { block() }

/** 异步线程执行捕获异常 */
@JvmOverloads
fun runOnIOCatch(block: () -> Unit, failure: (e: Exception) -> Unit = {}): Job =
    GlobalScope.launch(Dispatchers.IO) {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
            failure(e)
        }
    }

/** 异步线程执行挂起函数 */
fun runOnSuspendIO(block: suspend () -> Unit): Job = GlobalScope.launch(Dispatchers.IO) { block() }

/** 异步线程执行挂起函数捕获异常 */
@JvmOverloads
fun runOnSuspendIOCatch(block: suspend () -> Unit, failure: (e: Exception) -> Unit = {}): Job =
    GlobalScope.launch(Dispatchers.IO) {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
            failure(e)
        }
    }