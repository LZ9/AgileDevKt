package com.lodz.android.pandora.rx.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
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
    fun decodePathToBase64(path: String, widthPx: Int, heightPx: Int): Observable<String> {
        return Observable.create(object : RxObservableOnSubscribe<String>(path, widthPx, heightPx) {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                val pathArg = getArgs()[0] as String
                val widthPxArg = getArgs()[1] as Int
                val heightPxArg = getArgs()[2] as Int
                if (pathArg.isEmpty()) {
                    emitter.onError(IllegalArgumentException("path is empty"))
                    return
                }
                try {
                    val bitmap = BitmapUtils.compressBitmap(pathArg, widthPxArg, heightPxArg)
                    if (emitter.isDisposed) {
                        return
                    }
                    if (bitmap == null) {
                        emitter.onError(IllegalArgumentException("decode bitmap fail"))
                        return
                    }
                    val base64 = BitmapUtils.bitmapToBase64(bitmap)
                    if (emitter.isDisposed) {
                        return
                    }
                    if (base64.isEmpty()) {
                        emitter.onError(IllegalArgumentException("decode base64 fail"))
                        return
                    }
                    emitter.onNext(base64)
                    emitter.onComplete()
                } catch (e: Exception) {
                    e.printStackTrace()
                    emitter.onError(e)
                }

            }
        })
    }

    /** 把批量图片路径[paths]转为指定宽[widthPx]高[heightPx]的base64 */
    @JvmStatic
    fun decodePathToBase64(paths: ArrayList<String>, widthPx: Int, heightPx: Int): Observable<ArrayList<String>> {
        return Observable.create(object : RxObservableOnSubscribe<ArrayList<String>>(paths, widthPx, heightPx) {
            override fun subscribe(emitter: ObservableEmitter<ArrayList<String>>) {
                val pathsArg = getArgs()[0] as ArrayList<*>
                val widthPxArg = getArgs()[1] as Int
                val heightPxArg = getArgs()[2] as Int

                if (pathsArg.size == 0) {
                    emitter.onError(IllegalArgumentException("paths size is 0"))
                    return
                }

                try {
                    val base64s = ArrayList<String>()
                    for (p in pathsArg) {
                        if (emitter.isDisposed) {
                            return
                        }
                        if (p !is String) {
                            continue
                        }
                        val bitmap = BitmapUtils.compressBitmap(p, widthPxArg, heightPxArg)
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
                        return
                    }
                    emitter.onNext(base64s)
                    emitter.onComplete()
                } catch (e: Exception) {
                    e.printStackTrace()
                    emitter.onError(e)
                }

            }
        })
    }

    /** [view]防抖点击，在时长[duration]默认1，单位[unit]默认秒内，只回调一次 */
    @JvmStatic
    @JvmOverloads
    fun viewClick(view: View, duration: Long = 1, unit: TimeUnit = TimeUnit.SECONDS): Observable<View> {
        return Observable.create(object : RxObservableOnSubscribe<View>(view) {
            override fun subscribe(emitter: ObservableEmitter<View>) {
                val viewArg = getArgs()[0] as View
                viewArg.setOnClickListener { v ->
                    emitter.onNext(v)
                }
            }

        }).throttleFirst(duration, unit)
    }

    /** 文本[textView]每次变动都延迟[duration]默认500，单位[unit]默认毫秒后回调 */
    @JvmStatic
    @JvmOverloads
    fun textChanges(textView: TextView, duration: Long = 500, unit: TimeUnit = TimeUnit.MILLISECONDS): Observable<CharSequence> {
        return Observable.create(object : RxObservableOnSubscribe<CharSequence>(textView) {
            override fun subscribe(emitter: ObservableEmitter<CharSequence>) {
                val editTextArg = getArgs()[0] as EditText
                editTextArg.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val text = s?.toString() ?: ""
                        emitter.onNext(text)
                    }
                })
            }
        }).debounce(duration, unit)
    }
}