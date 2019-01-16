package com.lodz.android.agiledevkt.modules.rxjava.observable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.base.ResponseBean
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.then
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.subscribe.observer.ProgressObserver
import com.lodz.android.pandora.rx.subscribe.observer.RxObserver
import com.lodz.android.pandora.rx.utils.RxObservableOnSubscribe
import com.lodz.android.pandora.rx.utils.RxUtils
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * RxObservable订阅测试
 * 能够发射0或n个数据，并以成功或错误事件终止。
 * Created by zhouL on 2019/1/14.
 */
class RxObservableActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RxObservableActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 订阅失败开关 */
    private val mFailSwitch by bindView<Switch>(R.id.fail_switch)
    /** 返回键关闭开关 */
    private val mCancelableSwitch by bindView<Switch>(R.id.cancelable_switch)
    /** 空白处关闭开关 */
    private val mCanceledOutsideSwitch by bindView<Switch>(R.id.canceled_outside_switch)
    /** 原始订阅接口 */
    private val mOriginalBtn by bindView<MaterialButton>(R.id.original_btn)
    /** 基础封装 */
    private val mBaseBtn by bindView<MaterialButton>(R.id.base_btn)
    /** 响应数据封装 */
    private val mRxBtn by bindView<MaterialButton>(R.id.rx_btn)
    /** 进度条封装 */
    private val mProgressBtn by bindView<MaterialButton>(R.id.progress_btn)
    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)

    override fun getLayoutId(): Int = R.layout.activity_observable

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 返回键关闭开关
        mCancelableSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                mCanceledOutsideSwitch.isChecked = false
            }
            mCanceledOutsideSwitch.isEnabled = isChecked
        }

        // 原始订阅接口
        mOriginalBtn.setOnClickListener {
            cleanLog()
            createObservable()
                    .compose(RxUtils.ioToMainObservable())
                    .compose(bindDestroyEvent())
                    .subscribe(object : Observer<String> {
                        override fun onSubscribe(d: Disposable) {
                            printLog("onSubscribe")
                        }

                        override fun onNext(num: String) {
                            printLog("onNext num : $num")
                        }

                        override fun onError(e: Throwable) {
                            printLog("onError message : ${e.message}")
                        }

                        override fun onComplete() {
                            printLog("onComplete")
                        }
                    })
        }

        // 基础封装
        mBaseBtn.setOnClickListener {
            cleanLog()
            createObservable()
                    .compose(RxUtils.ioToMainObservable())
                    .compose(bindDestroyEvent())
                    .subscribe(object : BaseObserver<String>() {
                        override fun onBaseNext(any: String) {
                            printLog("onBaseNext num : $any")
                        }

                        override fun onBaseError(e: Throwable) {
                            printLog("onBaseError message : ${e.message}")
                        }
                    })
        }

        // 响应数据封装
        mRxBtn.setOnClickListener {
            cleanLog()
            createObservable(false)
                    .compose(RxUtils.ioToMainObservable())
                    .compose(bindDestroyEvent())
                    .subscribe(object : RxObserver<ResponseBean<String>>() {
                        override fun onRxNext(any: ResponseBean<String>) {
                            printLog("onRxNext num : ${any.data}")
                        }

                        override fun onRxError(e: Throwable, isNetwork: Boolean) {
                            printLog("onRxError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")
                        }
                    })
        }

        // 进度条封装
        mProgressBtn.setOnClickListener {
            cleanLog()
            createObservable(true)
                    .compose(RxUtils.ioToMainObservable())
                    .compose(bindDestroyEvent())
                    .subscribe(object : ProgressObserver<ResponseBean<String>>() {
                        override fun onPgNext(any: ResponseBean<String>) {
                            printLog("onPgNext num : ${any.data}")
                        }

                        override fun onPgError(e: Throwable, isNetwork: Boolean) {
                            printLog("onPgError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")
                        }
                    }.create(getContext(), "loading", mCancelableSwitch.isChecked, mCanceledOutsideSwitch.isChecked))
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    /** 创建自定义Observable */
    private fun createObservable(): Observable<String> {
        return Observable.create(object : RxObservableOnSubscribe<String>(3, 10) {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                val start = getArgs()[0] as Int// 起始大小
                val length = getArgs()[1] as Int// 长度
                val end = start + length
                if (emitter.isDisposed) {
                    return
                }
                if (mFailSwitch.isChecked) {
                    doError(emitter, RuntimeException("create fail"))
                    return
                }
                try {
                    for (i in start..end) {
                        Thread.sleep(200)
                        doNext(emitter, i.toString())
                    }
                    doComplete(emitter)
                } catch (e: Exception) {
                    e.printStackTrace()
                    doError(emitter, e)
                }
            }
        })
    }

    /** 创建自定义Observable，[isDelay]是否延时 */
    private fun createObservable(isDelay: Boolean): Observable<ResponseBean<String>> {
        return Observable.create(object : RxObservableOnSubscribe<ResponseBean<String>>(isDelay.then { 3 }
                ?: 0) {
            override fun subscribe(emitter: ObservableEmitter<ResponseBean<String>>) {
                val delayTime = getArgs()[0] as Int
                if (emitter.isDisposed) {
                    return
                }
                val responseBean: ResponseBean<String> = if (mFailSwitch.isChecked) {
                    val bean: ResponseBean<String> = ResponseBean.createFail()
                    bean.msg = "data empty"
                    bean
                } else {
                    val bean: ResponseBean<String> = ResponseBean.createSuccess()
                    bean.data = "i am data"
                    bean
                }
                try {
                    Thread.sleep(delayTime * 1000L)
                    doNext(emitter, responseBean)
                    doComplete(emitter)
                } catch (e: Exception) {
                    e.printStackTrace()
                    doError(emitter, e)
                }
            }
        })
    }

    /** 清空日志 */
    private fun cleanLog() {
        mResultTv.text = ""
    }

    /** 打印日志 */
    private fun printLog(text: String) {
        val log = "${mResultTv.text}\n$text"
        mResultTv.text = log
    }
}