package com.lodz.android.corekt.threadpool

import android.os.Process
import java.util.concurrent.ThreadFactory

/**
 * 优先级线程工厂
 * Created by zhouL on 2018/7/6.
 */
internal object PriorityThreadFactory {

    /** 创建一个优先级最高线程 */
    internal fun createHighestPriorityThread(): ThreadFactory = ThreadFactory { r ->
        val thread = PriorityThread(r)
        thread.setOSPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY)
        thread.priority = Thread.MAX_PRIORITY
        return@ThreadFactory thread
    }

    /** 创建一个普通线程 */
    internal fun createNormPriorityThread(): ThreadFactory = ThreadFactory { r ->
        val thread = PriorityThread(r)
        thread.setOSPriority(Process.THREAD_PRIORITY_DEFAULT)
        thread.priority = Thread.NORM_PRIORITY
        return@ThreadFactory thread
    }

    /** 创建一个优先级最低线程 */
    internal fun createLowestPriorityThread(): ThreadFactory = ThreadFactory { r ->
        val thread = PriorityThread(r)
        thread.setOSPriority(Process.THREAD_PRIORITY_LOWEST)
        thread.priority = Thread.MIN_PRIORITY
        return@ThreadFactory thread
    }
}