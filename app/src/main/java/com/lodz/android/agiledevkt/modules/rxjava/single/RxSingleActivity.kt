package com.lodz.android.agiledevkt.modules.rxjava.single

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ScrollView
import android.widget.Switch
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.base.ResponseBean
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.then
import com.lodz.android.corekt.utils.UiHandler
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.subscribe.single.BaseSingleObserver
import com.lodz.android.pandora.rx.subscribe.single.ProgressSingleObserver
import com.lodz.android.pandora.rx.subscribe.single.RxSingleObserver
import com.lodz.android.pandora.rx.utils.RxSingleOnSubscribe
import com.lodz.android.pandora.rx.utils.RxUtils
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

/**
 * RxSingle订阅测试
 * 只发射单个数据或错误事件
 * Created by zhouL on 2019/1/18.
 */
class RxSingleActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RxSingleActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 滚动控件 */
    private val mScrollView by bindView<NestedScrollView>(R.id.scroll_view)
    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)
    /** 订阅失败开关 */
    private val mFailSwitch by bindView<Switch>(R.id.fail_switch)
    /** Single转Observable订阅按钮 */
    private val mSingleToObservableBtn by bindView<MaterialButton>(R.id.single_to_observable_btn)
    /** 原始订阅接口按钮 */
    private val mOriginalBtn by bindView<MaterialButton>(R.id.original_btn)
    /** 基础封装按钮 */
    private val mBaseBtn by bindView<MaterialButton>(R.id.base_btn)
    /** 返回键关闭开关 */
    private val mCancelableSwitch by bindView<Switch>(R.id.cancelable_switch)
    /** 空白处关闭开关 */
    private val mCanceledOutsideSwitch by bindView<Switch>(R.id.canceled_outside_switch)
    /** 响应数据封装按钮 */
    private val mRxBtn by bindView<MaterialButton>(R.id.rx_btn)
    /** 进度条封装按钮 */
    private val mProgressBtn by bindView<MaterialButton>(R.id.progress_btn)

    override fun getLayoutId(): Int = R.layout.activity_rx_single

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

        // Single转Observable订阅按钮
        mSingleToObservableBtn.setOnClickListener {
            cleanLog()
            createSingle()
                    .compose(RxUtils.ioToMainSingle())
                    .compose(bindDestroyEvent())
                    .toObservable()
                    .subscribe(object : BaseObserver<String>() {
                        override fun onBaseNext(any: String) {
                            printLog("onBaseNext data : $any")
                        }

                        override fun onBaseError(e: Throwable) {
                            printLog("onBaseError message : ${e.message}")
                        }
                    })
        }

        // 原始订阅接口按钮
        mOriginalBtn.setOnClickListener {
            cleanLog()
            createSingle()
                    .compose(RxUtils.ioToMainSingle())
                    .compose(bindDestroyEvent())
                    .subscribe(object : SingleObserver<String> {
                        override fun onSubscribe(d: Disposable) {}

                        override fun onSuccess(t: String) {
                            printLog("onSuccess data : $t")
                        }

                        override fun onError(e: Throwable) {
                            printLog("onError message : ${e.message}")
                        }
                    })
        }

        // 基础封装按钮
        mBaseBtn.setOnClickListener {
            cleanLog()
            createSingle()
                    .compose(RxUtils.ioToMainSingle())
                    .compose(bindDestroyEvent())
                    .subscribe(object : BaseSingleObserver<String>() {
                        override fun onBaseSuccess(any: String) {
                            printLog("onBaseSuccess data : $any")
                        }

                        override fun onBaseError(e: Throwable) {
                            printLog("onBaseError message : ${e.message}")
                        }
                    })
        }

        // 响应数据封装按钮
        mRxBtn.setOnClickListener {
            cleanLog()
            createSingle(false)
                    .compose(RxUtils.ioToMainSingle())
                    .compose(bindDestroyEvent())
                    .subscribe(object : RxSingleObserver<ResponseBean<String>>() {
                        override fun onRxSuccess(any: ResponseBean<String>) {
                            printLog("onRxNext num : ${any.data}")
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
            createSingle(true)
                    .compose(RxUtils.ioToMainSingle())
                    .compose(bindDestroyEvent())
                    .subscribe(object : ProgressSingleObserver<ResponseBean<String>>() {
                        override fun onPgSuccess(any: ResponseBean<String>) {
                            printLog("onRxNext num : ${any.data}")
                        }

                        override fun onPgError(e: Throwable, isNetwork: Boolean) {
                            printLog("onRxError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")
                        }
                    }.create(getContext(), "loading", mCancelableSwitch.isChecked, mCanceledOutsideSwitch.isChecked))
        }
    }

    private fun createSingle(): Single<String> = Single.create(object : RxSingleOnSubscribe<String>("data") {
        override fun subscribe(emitter: SingleEmitter<String>) {
            val data = mFailSwitch.isChecked.then { "" } ?: getArgs()[0] as String
            if (emitter.isDisposed) {
                return
            }
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
    })

    private fun createSingle(isDelay: Boolean): Single<ResponseBean<String>> =
            Single.create(object : RxSingleOnSubscribe<ResponseBean<String>>(isDelay.then { 3 } ?: 0) {
                override fun subscribe(emitter: SingleEmitter<ResponseBean<String>>) {
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