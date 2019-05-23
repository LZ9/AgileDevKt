package com.lodz.android.agiledevkt.modules.rxjava.flowable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.core.widget.NestedScrollView
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.then
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.utils.UiHandler
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.subscriber.BaseSubscriber
import com.lodz.android.pandora.rx.subscribe.subscriber.ProgressSubscriber
import com.lodz.android.pandora.rx.subscribe.subscriber.RxSubscriber
import com.lodz.android.pandora.rx.utils.RxFlowableOnSubscribe
import com.lodz.android.pandora.rx.utils.RxUtils
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * RxFlowable订阅测试
 * 能够发射0或n个数据，并以完成或错误事件终止。 支持Backpressure，可以控制数据源发射的速度。
 * Created by zhouL on 2019/1/16.
 */
class RxFlowableActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RxFlowableActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 滚动控件 */
    private val mScrollView by bindView<NestedScrollView>(R.id.scroll_view)
    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)
    /** 背压类型组 */
    private val mRadioGroup by bindView<RadioGroup>(R.id.bp_rg)
    /** 单选按钮 */
    private val mErrorRb by bindView<RadioButton>(R.id.error_rb)
    /** 停止订阅按钮 */
    private val mStopBtn by bindView<MaterialButton>(R.id.stop_btn)
    /** 背压测试开关 */
    private val mBackpressureSwitch by bindView<Switch>(R.id.backpressure_switch)
    /** 背压测试 */
    private val mBpTestBtn by bindView<MaterialButton>(R.id.bp_test_btn)
    /** 数据自动拉取开关 */
    private val mAutoRequestSwitch by bindView<Switch>(R.id.auto_request_switch)
    /** 数据拉取按钮 */
    private val mRequestBtn by bindView<MaterialButton>(R.id.request_btn)
    /** 数据拉取测试 */
    private val mRequestTestBtn by bindView<MaterialButton>(R.id.request_test_btn)
    /** 订阅失败开关 */
    private val mFailSwitch by bindView<Switch>(R.id.fail_switch)
    /** 返回键关闭开关 */
    private val mCancelableSwitch by bindView<Switch>(R.id.cancelable_switch)
    /** 空白处关闭开关 */
    private val mCanceledOutsideSwitch by bindView<Switch>(R.id.canceled_outside_switch)
    /** 响应数据封装 */
    private val mRxBtn by bindView<MaterialButton>(R.id.rx_btn)
    /** 进度条封装 */
    private val mProgressBtn by bindView<MaterialButton>(R.id.progress_btn)

    /** 背压类型 */
    private var mBackpressureType = BackpressureStrategy.ERROR
    /** 背压订阅器 */
    private var mBpSubscription: Subscription? = null
    /** 数据拉取订阅器 */
    private var mDataSubscription: Subscription? = null

    override fun getLayoutId(): Int = R.layout.activity_rx_flowable

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

        // 背压类型组
        mRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            mBackpressureType = when (checkedId) {
                R.id.missing_rb -> BackpressureStrategy.MISSING
                R.id.error_rb -> BackpressureStrategy.ERROR
                R.id.buffer_rb -> BackpressureStrategy.BUFFER
                R.id.drop_rb -> BackpressureStrategy.DROP
                R.id.latest_rb -> BackpressureStrategy.LATEST
                else -> BackpressureStrategy.ERROR
            }
        }

        // 背压测试开关
        mBackpressureSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                toastShort(R.string.rx_flowable_bp_close_tips)
            }
        }

        // 停止订阅按钮
        mStopBtn.setOnClickListener {
            mBpSubscription?.cancel()
            mBpSubscription = null
        }

        // 背压测试
        mBpTestBtn.setOnClickListener {
            cleanLog()
            mBpSubscription = null
            createBpFlowable()
                    .compose(RxUtils.ioToMainFlowable())
                    .compose(bindDestroyEvent())
                    .subscribe(object : Subscriber<String> {
                        override fun onSubscribe(s: Subscription?) {
                            mBpSubscription = s
                            s?.request(1)
                        }

                        override fun onNext(str: String?) {
                            printLog(str ?: "")
                            UiHandler.postDelayed(100){
                                mBpSubscription?.request(1)
                            }
                        }

                        override fun onError(t: Throwable?) {
                            printLog("异常：${t?.message}")
                        }

                        override fun onComplete() {

                        }
                    })
        }

        // 数据自动拉取开关
        mAutoRequestSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mRequestBtn.isEnabled = !isChecked
        }

        // 数据拉取按钮
        mRequestBtn.setOnClickListener {
            mDataSubscription?.request(Long.MAX_VALUE)
            mDataSubscription = null
        }

        // 数据拉取测试
        mRequestTestBtn.setOnClickListener {
            cleanLog()
            val list = arrayListOf(4, 54, 8, 7, 4, 64, 187, 6, 314, 34, 6, 87)
            Flowable.fromIterable(list)
                    .onBackpressureBuffer()
                    .compose(RxUtils.ioToMainFlowable())
                    .compose(bindDestroyEvent())
                    .subscribe(object :BaseSubscriber<Int>(){
                        override fun onBaseSubscribe(s: Subscription?) {
                            super.onBaseSubscribe(s)
                            mDataSubscription = s
                        }
                        override fun onBaseNext(any: Int) {
                            printLog("num : $any")
                        }

                        override fun onBaseError(e: Throwable) {
                            printLog("异常：${e.message}")
                        }

                        override fun isAutoSubscribe(): Boolean {
                            return mAutoRequestSwitch.isChecked
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

        // 响应数据封装
        mRxBtn.setOnClickListener {
            cleanLog()
            createFlowable(false)
                    .compose(RxUtils.ioToMainFlowable())
                    .compose(bindDestroyEvent())
                    .subscribe(object :RxSubscriber<ResponseBean<String>>(){
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
            createFlowable(true)
                    .compose(RxUtils.ioToMainFlowable())
                    .compose(bindDestroyEvent())
                    .subscribe(object : ProgressSubscriber<ResponseBean<String>>() {
                        override fun onPgNext(any: ResponseBean<String>) {
                            printLog("onPgNext num : ${any.data}")
                        }

                        override fun onPgError(e: Throwable, isNetwork: Boolean) {
                            printLog("onPgError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")
                        }
                    }.create(getContext(), "loading", mCancelableSwitch.isChecked, mCanceledOutsideSwitch.isChecked))
        }
    }

    /** 创建自定义背压Flowable */
    private fun createBpFlowable(): Flowable<String> {
        return Flowable.create(object : RxFlowableOnSubscribe<String>("test.txt") {
            override fun subscribe(emitter: FlowableEmitter<String>) {
                val fileName = getArgs()[0] as String

                var isr: InputStreamReader? = null
                var br: BufferedReader? = null
                try {
                    isr = InputStreamReader(assets.open(fileName), StandardCharsets.UTF_8)
                    br = BufferedReader(isr)

                    var line: String?

                    while (true) {
                        line = br.readLine() ?: break
                        emitter.requested()
                        while (!mBackpressureSwitch.isChecked && emitter.requested() == 0L) {
                            //下游无法处理数据时循环等待
                            PrintLog.d("testtag", "等待下游")
                            if (emitter.isCancelled()) {
                                break
                            }
                        }
                        if (emitter.isCancelled()) {
                            break
                        }
                        PrintLog.i("testtag", "发送数据：$line")
                        doNext(emitter, line)
                    }

                    isr.close()
                    br.close()
                    PrintLog.v("testtag", "发送完成")
                    doComplete(emitter)
                } catch (e: Exception) {
                    e.printStackTrace()
                    isr?.close()
                    br?.close()
                    doError(emitter, e)
                }
            }
        }, mBackpressureType)
    }

    /** 创建自定义Flowable，[isDelay]是否延时 */
    private fun createFlowable(isDelay: Boolean): Flowable<ResponseBean<String>> {
        return Flowable.create(object :RxFlowableOnSubscribe<ResponseBean<String>>(isDelay.then { 3 } ?: 0){
            override fun subscribe(emitter: FlowableEmitter<ResponseBean<String>>) {
                val delayTime = getArgs()[0] as Int
                if (emitter.isCancelled){
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
                    doNext(emitter, responseBean)
                    doComplete(emitter)
                } catch (e: Exception) {
                    e.printStackTrace()
                    doError(emitter, e)
                }
            }
        }, BackpressureStrategy.BUFFER)
    }

    override fun initData() {
        super.initData()
        mErrorRb.isChecked = true
        showStatusCompleted()
    }

    /** 清空日志 */
    private fun cleanLog() {
        mResultTv.text = ""
    }

    /** 打印日志 */
    private fun printLog(text: String) {
        val log = if (mResultTv.text.isEmpty()){
            text
        }else{
            "${mResultTv.text}\n$text"
        }
        mResultTv.text = log
        UiHandler.postDelayed(100){
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }
}