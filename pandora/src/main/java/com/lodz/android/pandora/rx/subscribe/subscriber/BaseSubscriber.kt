package com.lodz.android.pandora.rx.subscribe.subscriber

import com.lodz.android.corekt.anko.getMetaData
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.application.BaseApplication
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

/**
 * 基类订阅者（带背压）
 * 能够发射0或n个数据，并以完成或错误事件终止。 支持Backpressure，可以控制数据源发射的速度。
 * Created by zhouL on 2018/7/6.
 */
abstract class BaseSubscriber<T> : Subscriber<T> {

    private val ERROR_TAG = "error_tag"

    private var mSubscription: Subscription? = null

    final override fun onSubscribe(s: Subscription?) {
        mSubscription = s
        if (isAutoSubscribe()) {
            request(Long.MAX_VALUE)
        }
        onBaseSubscribe(s)
    }

    final override fun onNext(any: T) {
        onBaseNext(any)
    }

    final override fun onError(t: Throwable?) {
        if (t == null) {
            return
        }
        t.printStackTrace()
        printTagLog(t)
        onBaseError(t)
    }

    /** 打印标签日志 */
    private fun printTagLog(t: Throwable) {
        val app = BaseApplication.get() ?: return
        val tag = app.getMetaData(ERROR_TAG)
        if (tag != null && tag is String) {
            if (tag.isNotEmpty()) {
                PrintLog.e(tag, t.toString(), t)
            }
        }
    }

    final override fun onComplete() {
        onBaseComplete()
    }

    fun getSubscription(): Subscription? = mSubscription

    fun clearSubscription() {
        mSubscription = null
    }

    /** 停止订阅 */
    fun cancel() {
        if (mSubscription != null) {
            mSubscription?.cancel()
            onCancel()
        }
    }

    /** 请求订阅 */
    fun request(n: Long) {
        mSubscription?.request(n)
    }

    open fun onBaseSubscribe(s: Subscription?) {}

    abstract fun onBaseNext(any: T)

    abstract fun onBaseError(e: Throwable)

    open fun onBaseComplete() {}
    /** 取消订阅时回调 */
    open fun onCancel() {}

    /** 是否自动订阅，默认是，否的时候需要自己调用request()方法订阅 */
    open fun isAutoSubscribe(): Boolean = true

    companion object {
        /** 创建空调用 */
        @JvmStatic
        fun <T> empty(): Subscriber<T> = object : Subscriber<T> {
            override fun onComplete() {}
            override fun onSubscribe(s: Subscription?) {}
            override fun onNext(t: T) {}
            override fun onError(t: Throwable?) {}
        }

        /** 创建lambda调用 */
        @JvmStatic
        @JvmOverloads
        fun <T> action(
            next: (any: T) -> Unit,
            error: (e: Throwable) -> Unit = {}
        ): BaseSubscriber<T> = object : BaseSubscriber<T>() {
            override fun onBaseNext(any: T) {
                next(any)
            }

            override fun onBaseError(e: Throwable) {
                error(e)
            }
        }
    }
}