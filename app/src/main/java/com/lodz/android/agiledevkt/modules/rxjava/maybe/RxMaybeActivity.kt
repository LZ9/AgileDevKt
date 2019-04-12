package com.lodz.android.agiledevkt.modules.rxjava.maybe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ScrollView
import android.widget.Switch
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.then
import com.lodz.android.corekt.utils.UiHandler
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.maybe.BaseMaybeObserver
import com.lodz.android.pandora.rx.subscribe.maybe.ProgressMaybeObserver
import com.lodz.android.pandora.rx.subscribe.maybe.RxMaybeObserver
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.utils.RxMaybeOnSubscribe
import com.lodz.android.pandora.rx.utils.RxUtils
import io.reactivex.Maybe
import io.reactivex.MaybeEmitter
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable

/**
 * RxMaybe订阅测试
 * 发送单个事件，以成功或失败或完成结束（三选一）
 * Created by zhouL on 2019/1/18.
 */
class RxMaybeActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RxMaybeActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 滚动控件 */
    private val mScrollView by bindView<NestedScrollView>(R.id.scroll_view)
    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)
    /** 订阅失败开关 */
    private val mFailSwitch by bindView<Switch>(R.id.fail_switch)
    /** Maybe转Observable订阅按钮 */
    private val mMaybeToObservableBtn by bindView<MaterialButton>(R.id.maybe_to_observable_btn)
    /** 成功订阅按钮 */
    private val mSuccessBtn by bindView<MaterialButton>(R.id.success_btn)
    /** 完成订阅按钮 */
    private val mCompleteBtn by bindView<MaterialButton>(R.id.complete_btn)
    /** 返回键关闭开关 */
    private val mCancelableSwitch by bindView<Switch>(R.id.cancelable_switch)
    /** 空白处关闭开关 */
    private val mCanceledOutsideSwitch by bindView<Switch>(R.id.canceled_outside_switch)
    /** 响应数据封装按钮 */
    private val mRxBtn by bindView<MaterialButton>(R.id.rx_btn)
    /** 进度条封装按钮 */
    private val mProgressBtn by bindView<MaterialButton>(R.id.progress_btn)

    override fun getLayoutId(): Int = R.layout.activity_rx_maybe

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

        // Maybe转Observable订阅按钮
        mMaybeToObservableBtn.setOnClickListener {
            cleanLog()
            Maybe.create(object : RxMaybeOnSubscribe<String>() {
                override fun subscribe(emitter: MaybeEmitter<String>) {
                    if (mFailSwitch.isChecked) {
                        doError(emitter, NullPointerException("data empty"))
                    } else {
                        doSuccess(emitter, "data")
                    }
                }
            }).toObservable()
                    .compose(RxUtils.ioToMainObservable())
                    .compose(bindDestroyEvent())
                    .subscribe(object : BaseObserver<String>() {
                        override fun onBaseSubscribe(d: Disposable) {
                            super.onBaseSubscribe(d)
                            printLog("onBaseSubscribe")
                        }

                        override fun onBaseNext(any: String) {
                            printLog("onBaseNext : $any")
                        }

                        override fun onBaseError(e: Throwable) {
                            printLog("onBaseError : ${e.message}")
                        }

                        override fun onBaseComplete() {
                            super.onBaseComplete()
                            printLog("onBaseComplete")
                        }
                    })
        }

        // 成功订阅按钮
        mSuccessBtn.setOnClickListener {
            cleanLog()
            Maybe.create(object : RxMaybeOnSubscribe<String>("data") {
                override fun subscribe(emitter: MaybeEmitter<String>) {
                    val data = mFailSwitch.isChecked.then { "" } ?: getArgs()[0] as String
                    try {
                        if (data.isNotEmpty()) {
                            doSuccess(emitter, data)
                        } else {
                            doError(emitter, NullPointerException("data empty"))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        doError(emitter, e)
                    }
                }
            }).compose(RxUtils.ioToMainMaybe())
                    .compose(bindDestroyEvent())
                    .subscribe(object : BaseMaybeObserver<String>() {
                        override fun onBaseSuccess(any: String) {
                            printLog("onBaseSuccess : $any")
                        }

                        override fun onBaseComplete() {
                            printLog("onBaseComplete")
                        }

                        override fun onBaseError(e: Throwable) {
                            printLog("onBaseError : ${e.message}")
                        }
                    })
        }

        // 完成订阅按钮
        mCompleteBtn.setOnClickListener {
            cleanLog()
            Maybe.create(object : RxMaybeOnSubscribe<String>("data") {
                override fun subscribe(emitter: MaybeEmitter<String>) {
                    val data = mFailSwitch.isChecked.then { "" } ?: getArgs()[0] as String
                    try {
                        if (data.isNotEmpty()) {
                            doComplete(emitter)
                        } else {
                            doError(emitter, NullPointerException("data empty"))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        doError(emitter, e)
                    }
                }
            }).compose(RxUtils.ioToMainMaybe())
                    .compose(bindDestroyEvent())
                    .subscribe(object : MaybeObserver<String> {
                        override fun onSubscribe(d: Disposable) {
                            printLog("onSubscribe")
                        }

                        override fun onSuccess(t: String) {
                            printLog("onSuccess : $t")
                        }

                        override fun onComplete() {
                            printLog("onComplete")
                        }

                        override fun onError(e: Throwable) {
                            printLog("onError : ${e.message}")
                        }
                    })
        }

        // 响应数据封装按钮
        mRxBtn.setOnClickListener {
            cleanLog()
            createMaybe(false)
                    .compose(RxUtils.ioToMainMaybe())
                    .compose(bindDestroyEvent())
                    .subscribe(object : RxMaybeObserver<ResponseBean<String>>() {
                        override fun onRxSuccess(any: ResponseBean<String>) {
                            printLog("onRxSuccess num : ${any.data}")
                        }

                        override fun onRxComplete() {
                            printLog("onRxComplete")
                        }

                        override fun onRxError(e: Throwable, isNetwork: Boolean) {
                            printLog("onRxError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")
                        }
                    })
        }

        // 返回键关闭开关
        mCancelableSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                mCanceledOutsideSwitch.isChecked = false
            }
            mCanceledOutsideSwitch.isEnabled = isChecked
        }

        // 进度条封装按钮
        mProgressBtn.setOnClickListener {
            cleanLog()
            createMaybe(true)
                    .compose(RxUtils.ioToMainMaybe())
                    .compose(bindDestroyEvent())
                    .subscribe(object : ProgressMaybeObserver<ResponseBean<String>>() {
                        override fun onPgSuccess(any: ResponseBean<String>) {
                            printLog("onPgSuccess num : ${any.data}")
                        }

                        override fun onPgComplete() {
                            printLog("onPgComplete")
                        }

                        override fun onPgError(e: Throwable, isNetwork: Boolean) {
                            printLog("onPgError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")
                        }
                    }.create(getContext(), "loading", mCancelableSwitch.isChecked, mCanceledOutsideSwitch.isChecked))
        }
    }

    private fun createMaybe(isDelay: Boolean): Maybe<ResponseBean<String>> =
            Maybe.create(object : RxMaybeOnSubscribe<ResponseBean<String>>(isDelay.then { 3 }
                    ?: 0) {
                override fun subscribe(emitter: MaybeEmitter<ResponseBean<String>>) {
                    val delayTime = getArgs()[0] as Int
                    if (emitter.isDisposed) {
                        return
                    }
                    val responseBean: ResponseBean<String> = if (mFailSwitch.isChecked) {
                        val bean: ResponseBean<String> = ResponseBean.createFail()
                        bean.msg = "数据获取失败"
                        bean
                    } else {
                        val bean: ResponseBean<String> = ResponseBean.createSuccess()
                        bean.data = "数据获取成功"
                        bean
                    }
                    try {
                        Thread.sleep(delayTime * 1000L)
                        doSuccess(emitter, responseBean)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        doError(emitter, e)
                    }
                }
            })

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    /** 清空日志 */
    private fun cleanLog() {
        mResultTv.text = ""
    }

    /** 打印日志 */
    private fun printLog(text: String) {
        val log = if (mResultTv.text.isEmpty()) {
            text
        } else {
            "${mResultTv.text}\n$text"
        }
        mResultTv.text = log
        UiHandler.postDelayed(Runnable {
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }, 100)
    }

}