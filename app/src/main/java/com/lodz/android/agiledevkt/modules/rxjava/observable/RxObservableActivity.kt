package com.lodz.android.agiledevkt.modules.rxjava.observable

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
import com.lodz.android.corekt.anko.runOnMainDelay
import com.lodz.android.corekt.anko.then
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.subscribe.observer.ProgressObserver
import com.lodz.android.pandora.rx.subscribe.observer.RxObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.rx.utils.doComplete
import com.lodz.android.pandora.rx.utils.doError
import com.lodz.android.pandora.rx.utils.doNext
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.GlobalScope

/**
 * RxObservable订阅测试
 * 能够发射0或n个数据，并以完成或错误事件终止。
 * Created by zhouL on 2019/1/14.
 */
class RxObservableActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RxObservableActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 滚动控件 */
    private val mScrollView by bindView<NestedScrollView>(R.id.scroll_view)
    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)
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

    override fun getLayoutId(): Int = R.layout.activity_rx_observable

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
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
                    .subscribe(BaseObserver.action({ any ->
                        printLog("onBaseNext num : $any")
                    }, { e ->
                        printLog("onBaseError message : ${e.message}")
                    }))
        }

        // 响应数据封装
        mRxBtn.setOnClickListener {
            cleanLog()
            createObservable(false)
                    .compose(RxUtils.ioToMainObservable())
                    .compose(bindDestroyEvent())
                    .subscribe(RxObserver.action({ any ->
                        printLog("onRxNext : ${any.data}")
                    }, { e, isNetwork ->
                        printLog("onRxError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")
                    }))
        }

        // 进度条封装
        mProgressBtn.setOnClickListener {
            cleanLog()
            createObservable(true)
                .compose(RxUtils.ioToMainObservable())
                .compose(bindDestroyEvent())
                .subscribe(ProgressObserver.action(getContext(),"loading", mCancelableSwitch.isChecked, mCanceledOutsideSwitch.isChecked,
                    { any -> printLog("onPgNext num : ${any.data}") },
                    { e, isNetwork -> printLog("onPgError message : ${RxUtils.getExceptionTips(e, isNetwork,"create fail")}")}))
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    /** 创建自定义Observable */
    private fun createObservable(): Observable<String> =
        Observable.create { emitter ->
            val start = 3// 起始大小
            val length = 10// 长度
            val end = start + length
            if (emitter.isDisposed) {
                return@create
            }
            if (mFailSwitch.isChecked) {
                emitter.doError(RuntimeException("create fail"))
                return@create
            }
            try {
                for (i in start..end) {
                    Thread.sleep(200)
                    emitter.doNext(i.toString())
                }
                emitter.doComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.doError(e)
            }
        }

    /** 创建自定义Observable，[isDelay]是否延时 */
    private fun createObservable(isDelay: Boolean): Observable<ResponseBean<String>> =
        Observable.create { emitter ->
            val delayTime = isDelay.then { 3 } ?: 0
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
                emitter.doNext(responseBean)
                emitter.doComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.doError(e)
            }
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
        GlobalScope.runOnMainDelay(100) {
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }
}