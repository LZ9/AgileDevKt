package com.lodz.android.corekt.threadpool

import androidx.annotation.IntRange
import java.util.concurrent.*

/**
 * 线程池管理器
 * Created by zhouL on 2018/7/6.
 */
class ThreadPoolManager private constructor() {

    companion object {
        private val sInstance = ThreadPoolManager()
        @JvmStatic
        fun get(): ThreadPoolManager = sInstance
    }

    /** 最高优先级线程池（界面数据加载）  */
    private var mHighestExecutor: ExecutorService? = null

    /** 正常优先级线程池（下载等）  */
    private var mNormalExecutor: ExecutorService? = null

    /** 最低优先级线程池（数据同步等）  */
    private var mLowestExecutor: ExecutorService? = null

    /** 配置内容  */
    private val mBuilder: Builder

    init {
        mBuilder = Builder()
    }

    /** 重新配置 */
    fun newBuilder(): Builder = mBuilder

    /** 执行一个高优先级线程[runnable] */
    fun executeHighest(runnable: Runnable) {
        if (mHighestExecutor == null) {
            mHighestExecutor = ThreadPoolExecutor(
                mBuilder.getCorePoolSize(), mBuilder.getMaximumPoolSize(),
                mBuilder.getKeepAliveTime(), mBuilder.getKeepAliveTimeUnit(), LinkedBlockingQueue<Runnable>(),
                PriorityThreadFactory.createHighestPriorityThread(), mBuilder.getRejectedExecutionHandler()
            )
        }
        mHighestExecutor?.execute(runnable)
    }

    /** 执行一个普通优先级线程[runnable] */
    fun executeNormal(runnable: Runnable) {
        if (mNormalExecutor == null) {
            mNormalExecutor = ThreadPoolExecutor(
                mBuilder.getCorePoolSize(), mBuilder.getMaximumPoolSize(),
                mBuilder.getKeepAliveTime(), mBuilder.getKeepAliveTimeUnit(), LinkedBlockingQueue<Runnable>(),
                PriorityThreadFactory.createNormPriorityThread(), mBuilder.getRejectedExecutionHandler()
            )
        }
        mNormalExecutor?.execute(runnable)
    }

    /** 执行一个低优先级线程[runnable] */
    fun executeLowest(runnable: Runnable) {
        if (mLowestExecutor == null) {
            mLowestExecutor = ThreadPoolExecutor(
                mBuilder.getCorePoolSize(), mBuilder.getMaximumPoolSize(),
                mBuilder.getKeepAliveTime(), mBuilder.getKeepAliveTimeUnit(), LinkedBlockingQueue<Runnable>(),
                PriorityThreadFactory.createLowestPriorityThread(), mBuilder.getRejectedExecutionHandler()
            )
        }
        mLowestExecutor?.execute(runnable)
    }

    /** 释放所有线程池 */
    fun releaseAll() {
        releaseHighestExecutor()
        releaseNormalExecutor()
        releaseLowestExecutor()
    }

    /** 释放高优先级线程池 */
    fun releaseHighestExecutor() {
        if (mHighestExecutor == null) {
            return
        }
        shutdown(mHighestExecutor!!)
        mHighestExecutor = null
    }

    /** 释放普通优先级线程池 */
    fun releaseNormalExecutor() {
        if (mNormalExecutor == null) {
            return
        }
        shutdown(mNormalExecutor!!)
        mNormalExecutor = null
    }

    /** 释放低优先级线程池 */
    fun releaseLowestExecutor() {
        if (mLowestExecutor == null) {
            return
        }
        shutdown(mLowestExecutor!!)
        mLowestExecutor = null
    }

    /** 结束线程池[executorService] */
    private fun shutdown(executorService: ExecutorService) {
        try {
            // 发出结束通知
            executorService.shutdown()
            // 设置等待时间，所有的任务都结束的时候，返回TRUE
            if (!executorService.awaitTermination(mBuilder.getAwaitTime(), mBuilder.getAwaitTimeUnit())) {
                // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                executorService.shutdownNow()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
            executorService.shutdownNow()
        }
    }

    /** 队列满时线程池的拒绝策略（自定义） */
    private class DefaultRejectedExecutionHandler : RejectedExecutionHandler {
        override fun rejectedExecution(r: Runnable, executor: ThreadPoolExecutor) {
            if (executor.isShutdown) {
                return
            }
            //移除队头元素
            executor.queue.poll()
            //再尝试入队
            executor.execute(r)
        }
    }

    inner class Builder {
        /** 线程数  */
        private var corePoolSize = 0
        /** 最大线程数  */
        private var maximumPoolSize = 0
        /** 线程数空闲时间  */
        private var keepAliveTime: Long = 0
        /** 线程数空闲时间单位  */
        private var keepAliveTimeUnit: TimeUnit? = null
        /** 拒绝策略  */
        private var handler: RejectedExecutionHandler? = null
        /** 线程结束等待时间  */
        private var awaitTime: Long = 0
        /** 线程结束等待时间单位  */
        private var awaitTimeUnit: TimeUnit? = null

        internal fun getCorePoolSize(): Int {
            if (corePoolSize <= 0) {
                val processors = Runtime.getRuntime().availableProcessors()
                corePoolSize = if (processors > 0) processors else 0
            }
            return corePoolSize
        }

        internal fun getMaximumPoolSize(): Int {
            if (maximumPoolSize <= 0 || maximumPoolSize < getCorePoolSize()) {
                if (getCorePoolSize() > 0) {
                    maximumPoolSize = getCorePoolSize() * 2
                    return maximumPoolSize
                }
                maximumPoolSize = 2
                return maximumPoolSize
            }
            return maximumPoolSize
        }

        internal fun getKeepAliveTime(): Long {
            if (keepAliveTime == 0L) {
                keepAliveTime = 1
            }
            return keepAliveTime
        }

        internal fun getKeepAliveTimeUnit(): TimeUnit {
            if (keepAliveTimeUnit == null) {
                keepAliveTimeUnit = TimeUnit.SECONDS
            }
            return keepAliveTimeUnit!!
        }

        internal fun getRejectedExecutionHandler(): RejectedExecutionHandler {
            if (handler == null) {
                handler = ThreadPoolExecutor.DiscardPolicy()
            }
            return handler!!
        }

        internal fun getAwaitTime(): Long {
            if (awaitTime == 0L) {
                awaitTime = 50
            }
            return awaitTime
        }

        internal fun getAwaitTimeUnit(): TimeUnit {
            if (awaitTimeUnit == null) {
                awaitTimeUnit = TimeUnit.MILLISECONDS
            }
            return awaitTimeUnit!!
        }

        /** 设置线程数[corePoolSize] */
        fun setCorePoolSize(@IntRange(from = 0) corePoolSize: Int): Builder {
            this.corePoolSize = corePoolSize
            return this
        }

        /** 设置最大线程数[maximumPoolSize]  */
        fun setMaximumPoolSize(@IntRange(from = 1) maximumPoolSize: Int): Builder {
            this.maximumPoolSize = maximumPoolSize
            return this
        }

        /** 设置线程数空闲时间[keepAliveTime]  */
        fun setKeepAliveTime(@IntRange(from = 0) keepAliveTime: Long): Builder {
            this.keepAliveTime = keepAliveTime
            return this
        }

        /** 设置线程数空闲时间单位[unit]  */
        fun setKeepAliveTimeUnit(unit: TimeUnit): Builder {
            this.keepAliveTimeUnit = unit
            return this
        }

        /** 设置拒绝策略[handler]  */
        fun setRejectedExecutionHandler(handler: RejectedExecutionHandler): Builder {
            this.handler = handler
            return this
        }

        /** 设置线程结束等待时间[awaitTime]  */
        fun setAwaitTime(awaitTime: Long): Builder {
            this.awaitTime = awaitTime
            return this
        }

        /** 设置线程结束等待时间单位[unit]  */
        fun setAwaitTimeUnit(unit: TimeUnit): Builder {
            this.awaitTimeUnit = unit
            return this
        }

        /** 设置完成配置  */
        fun build(): ThreadPoolManager = this@ThreadPoolManager
    }

}