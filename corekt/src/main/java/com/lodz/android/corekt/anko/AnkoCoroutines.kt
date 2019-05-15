package com.lodz.android.corekt.anko

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * 协程扩展类
 * @author zhouL
 * @date 2019/5/15
 */

/** 主线程执行 */
fun runOnMain(block: () -> Unit): Job = GlobalScope.launch(Dispatchers.Main) { block() }

/** 主线程执行 */
fun runOnSuspendMain(block: suspend () -> Unit): Job = GlobalScope.launch(Dispatchers.Main) { block() }

/** 异步线程执行 */
fun runOnIO(block: () -> Unit): Job = GlobalScope.launch(Dispatchers.IO) { block() }

/** 异步线程执行 */
fun runOnSuspendIO(block: suspend () -> Unit): Job = GlobalScope.launch(Dispatchers.IO) { block() }