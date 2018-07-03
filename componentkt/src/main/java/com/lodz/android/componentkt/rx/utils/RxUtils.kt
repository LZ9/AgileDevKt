package com.lodz.android.componentkt.rx.utils

import com.lodz.android.componentkt.rx.exception.DataException
import com.lodz.android.componentkt.rx.exception.RxException
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Rx帮助类
 * Created by zhouL on 2018/7/3.
 */
object RxUtils {

    /** 在异步线程发起，在主线程订阅 */
    fun <T> ioToMainObservable() = object : ObservableTransformer<T, T> {
        override fun apply(upstream: Observable<T>) = upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /** 在异步线程发起，在主线程订阅 */
    fun <T> ioToMainFlowable() = object : FlowableTransformer<T, T> {
        override fun apply(upstream: Flowable<T>) = upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /** 在异步线程发起，在主线程订阅 */
    fun <T> ioToMainMaybe() = object : MaybeTransformer<T, T> {
        override fun apply(upstream: Maybe<T>) = upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /** 在异步线程发起，在主线程订阅 */
    fun <T> ioToMainSingle() = object : SingleTransformer<T, T> {
        override fun apply(upstream: Single<T>) = upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
    
    /** 获取异常[e]的提示语（配合订阅者使用），是否网络异常[isNetwork]，默认提示语[defaultTips] */
    fun getExceptionTips(e: Throwable, isNetwork: Boolean, defaultTips: String): String {
        if (isNetwork && e is RxException){
            return e.getErrorMsg()
        }
        if (e is DataException){
            if (e.getData() != null && !e.getData()!!.getMsg().isEmpty()){
                return e.getData()!!.getMsg()
            }
        }
        return defaultTips
    }

    /** 获取[e]的网络异常提示语（配合订阅者使用），是否网络异常[isNetwork]，默认提示语[defaultTips] */
    fun getNetworkExceptionTips(e: Throwable, isNetwork: Boolean, defaultTips: String): String {
        if (isNetwork && e is RxException) {
            return e.getErrorMsg()
        }
        return defaultTips
    }

    // todo 待完善


}