package com.lodz.android.pandora.rx.utils

import android.view.View
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.lodz.android.corekt.utils.BitmapUtils
import com.lodz.android.pandora.rx.exception.DataException
import com.lodz.android.pandora.rx.exception.RxException
import com.lodz.android.pandora.rx.status.ResponseStatus
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Rx帮助类
 * Created by zhouL on 2018/7/3.
 */
object RxUtils {

    /** 在异步线程发起，在主线程订阅 */
    @JvmStatic
    fun <T> ioToMainObservable(): ObservableTransformer<T, T> = ObservableTransformer { upstream ->
        return@ObservableTransformer upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /** 在异步线程发起，在主线程订阅 */
    @JvmStatic
    fun <T> ioToMainFlowable(): FlowableTransformer<T, T> = FlowableTransformer { upstream ->
        return@FlowableTransformer upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /** 在异步线程发起，在主线程订阅 */
    @JvmStatic
    fun <T> ioToMainMaybe(): MaybeTransformer<T, T> = MaybeTransformer { upstream ->
        return@MaybeTransformer upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /** 在异步线程发起，在主线程订阅 */
    @JvmStatic
    fun <T> ioToMainSingle(): SingleTransformer<T, T> = SingleTransformer { upstream ->
        return@SingleTransformer upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /** 在异步线程发起，在主线程订阅 */
    @JvmStatic
    fun ioToMainCompletable(): CompletableTransformer = CompletableTransformer { upstream ->
        return@CompletableTransformer upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /** 获取异常[e]的提示语（配合订阅者使用），是否网络异常[isNetwork]，默认提示语[defaultTips] */
    @JvmStatic
    fun getExceptionTips(e: Throwable, isNetwork: Boolean, defaultTips: String): String {
        if (isNetwork && e is RxException) {
            return e.getErrorMsg()
        }
        if (e is DataException) {
            val status = e.getData()
            if (status != null && status.valueMsg().isNotEmpty()) {
                return status.valueMsg()
            }
        }
        return defaultTips
    }

    /** 获取[e]的网络异常提示语（配合订阅者使用），是否网络异常[isNetwork]，默认提示语[defaultTips] */
    @JvmStatic
    fun getNetworkExceptionTips(e: Throwable, isNetwork: Boolean, defaultTips: String): String {
        if (isNetwork && e is RxException) {
            return e.getErrorMsg()
        }
        return defaultTips
    }

    /** 获取接口数据状态，[e]异常 */
    @JvmStatic
    fun getResponseStatus(e: Throwable): ResponseStatus? = if (e is DataException) e.getData() else null

    /** 把图片路径[path]转为指定宽[widthPx]高[heightPx]的base64 */
    @JvmStatic
    fun decodePathToBase64(path: String, widthPx: Int, heightPx: Int): Observable<String> =
        Observable.create { emitter ->
            if (path.isEmpty()) {
                emitter.doError(IllegalArgumentException("path is empty"))
                return@create
            }
            try {
                val bitmap = BitmapUtils.compressBitmap(path, widthPx, heightPx)
                if (emitter.isDisposed) {
                    return@create
                }
                if (bitmap == null) {
                    emitter.doError(IllegalArgumentException("decode bitmap fail"))
                    return@create
                }
                val base64 = BitmapUtils.bitmapToBase64(bitmap)
                if (emitter.isDisposed) {
                    return@create
                }
                if (base64.isEmpty()) {
                    emitter.doError(IllegalArgumentException("decode base64 fail"))
                    return@create
                }
                emitter.doNext(base64)
                emitter.doComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.doError(e)
            }
        }

    /** 把批量图片路径[paths]转为指定宽[widthPx]高[heightPx]的base64 */
    @JvmStatic
    fun decodePathToBase64(paths: ArrayList<String>, widthPx: Int, heightPx: Int): Observable<ArrayList<String>> =
        Observable.create { emitter ->
            if (paths.size == 0) {
                emitter.doError(IllegalArgumentException("paths size is 0"))
                return@create
            }

            try {
                val base64s = ArrayList<String>()
                for (p in paths) {
                    if (emitter.isDisposed) {
                        return@create
                    }
                    val bitmap = BitmapUtils.compressBitmap(p, widthPx, heightPx)
                    if (bitmap == null) {
                        base64s.add("")
                        continue
                    }
                    val base64 = BitmapUtils.bitmapToBase64(bitmap)
                    if (base64.isEmpty()) {
                        base64s.add("")
                        continue
                    }
                    base64s.add(base64)
                }
                if (emitter.isDisposed) {
                    return@create
                }
                emitter.doNext(base64s)
                emitter.doComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.doError(e)
            }
        }

    /** [view]防抖点击，在时长[duration]默认1，单位[unit]默认秒内，只回调一次 */
    @JvmStatic
    @JvmOverloads
    fun viewClick(view: View, duration: Long = 1, unit: TimeUnit = TimeUnit.SECONDS): Observable<View> =
        Observable.create<View> { emitter ->
            view.setOnClickListener { v ->
                emitter.doNext(v)
            }
        }.throttleFirst(duration, unit)

    /** 文本[textView]每次变动都延迟[duration]默认500，单位[unit]默认毫秒后回调 */
    @JvmStatic
    @JvmOverloads
    fun textChanges(textView: TextView, duration: Long = 500, unit: TimeUnit = TimeUnit.MILLISECONDS): Observable<CharSequence> =
        Observable.create<CharSequence> { emitter ->
            textView.addTextChangedListener(onTextChanged = { s: CharSequence?, start: Int, before: Int, count: Int ->
                val text = s?.toString() ?: ""
                emitter.doNext(text)
            })
        }.debounce(duration, unit)
}