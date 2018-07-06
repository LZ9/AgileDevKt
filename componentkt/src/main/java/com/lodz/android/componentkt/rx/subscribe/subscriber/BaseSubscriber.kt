package com.lodz.android.componentkt.rx.subscribe.subscriber

import com.lodz.android.componentkt.base.application.BaseApplication
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.utils.getMetaData
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

/**
 * 基类订阅者（带背压）
 * Created by zhouL on 2018/7/6.
 */
abstract class BaseSubscriber<T> : Subscriber<T> {

    private val ERROR_TAG = "error_tag"

    private var mSubscription: Subscription? = null

    final override fun onSubscribe(s: Subscription) {
        mSubscription = s
        if (isAutoSubscribe()) {
            request(1)
        }
        onBaseSubscribe(s)
    }

    final override fun onNext(any: T) {
        onBaseNext(any)
    }

    final override fun onError(t: Throwable) {
        t.printStackTrace()
        printTagLog(t)
        onBaseError(t)
    }

    /** 打印标签日志 */
    private fun printTagLog(t: Throwable) {
        if (BaseApplication.get() == null) {
            return
        }
        val tag = BaseApplication.get()!!.getMetaData(ERROR_TAG)
        if (tag != null && tag is String) {
            if (!tag.isEmpty()) {
                PrintLog.e(tag, t.toString(), t)
            }
        }
    }

    final override fun onComplete() {
        onBaseComplete()
    }

    fun getSubscription() = mSubscription

    fun clearSubscription() {
        mSubscription = null
    }

    /** 停止订阅  */
    fun cancel() {
        if (mSubscription != null) {
            mSubscription!!.cancel()
            onCancel()
        }
    }

    /** 请求订阅  */
    fun request(n: Long) {
        if (mSubscription != null) {
            mSubscription!!.request(n)
        }
    }

    open fun onBaseSubscribe(s: Subscription) {}

    abstract fun onBaseNext(any: T)

    abstract fun onBaseError(e: Throwable)

    open fun onBaseComplete() {}
    /** 取消订阅时回调  */
    open fun onCancel() {}

    /** 是否自动订阅，默认是，否的时候需要自己调用request()方法订阅 */
    open fun isAutoSubscribe() = true
}