package com.lodz.android.agiledevkt.modules.rxjava.flowable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import com.lodz.android.agiledevkt.databinding.ActivityRxFlowableBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.runOnMainDelay
import com.lodz.android.corekt.anko.then
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.subscriber.BaseSubscriber
import com.lodz.android.pandora.rx.subscribe.subscriber.ProgressSubscriber
import com.lodz.android.pandora.rx.subscribe.subscriber.RxSubscriber
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.rx.utils.doComplete
import com.lodz.android.pandora.rx.utils.doError
import com.lodz.android.pandora.rx.utils.doNext
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.GlobalScope
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

    /** 背压类型 */
    private var mBackpressureType = BackpressureStrategy.ERROR
    /** 背压订阅器 */
    private var mBpSubscription: Subscription? = null
    /** 数据拉取订阅器 */
    private var mDataSubscription: Subscription? = null

    private val mBinding: ActivityRxFlowableBinding by bindingLayout(ActivityRxFlowableBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

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

        // 背压类型组
        mBinding.bpRg.setOnCheckedChangeListener { group, checkedId ->
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
        mBinding.backpressureSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                toastShort(R.string.rx_flowable_bp_close_tips)
            }
        }

        // 停止订阅按钮
        mBinding.stopBtn.setOnClickListener {
            mBpSubscription?.cancel()
            mBpSubscription = null
        }

        // 背压测试
        mBinding.bpTestBtn.setOnClickListener {
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
                            GlobalScope.runOnMainDelay(100) {
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
        mBinding.autoRequestSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mBinding.requestBtn.isEnabled = !isChecked
        }

        // 数据拉取按钮
        mBinding.requestBtn.setOnClickListener {
            mDataSubscription?.request(Long.MAX_VALUE)
            mDataSubscription = null
        }

        // 数据拉取测试
        mBinding.requestTestBtn.setOnClickListener {
            cleanLog()
            val list = arrayListOf(4, 54, 8, 7, 4, 64, 187, 6, 314, 34, 6, 87)
            Flowable.fromIterable(list)
                    .onBackpressureBuffer()
                    .compose(RxUtils.ioToMainFlowable())
                    .compose(bindDestroyEvent())
                    .subscribe(object : BaseSubscriber<Int>() {
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
                            return mBinding.autoRequestSwitch.isChecked
                        }
                    })
        }

        // 返回键关闭开关
        mBinding.cancelableSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                mBinding.canceledOutsideSwitch.isChecked = false
            }
            mBinding.canceledOutsideSwitch.isEnabled = isChecked
        }

        // 响应数据封装
        mBinding.rxBtn.setOnClickListener {
            cleanLog()
            createFlowable(false)
                .compose(RxUtils.ioToMainFlowable())
                .compose(bindDestroyEvent())
                .subscribe(RxSubscriber.action(
                    { any ->
                        printLog("onRxNext num : ${any.data}")
                    }, { e, isNetwork ->
                        printLog("onRxError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")
                    })
                )
        }

        // 进度条封装
        mBinding.progressBtn.setOnClickListener {
            cleanLog()
            createFlowable(true)
                .compose(RxUtils.ioToMainFlowable())
                .compose(bindDestroyEvent())
                .subscribe(
                    ProgressSubscriber.action(
                        getContext(),
                        "loading",
                        mBinding.cancelableSwitch.isChecked,
                        mBinding.canceledOutsideSwitch.isChecked,
                        { any -> printLog("onPgNext num : ${any.data}") },
                        { e, isNetwork -> printLog("onPgError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")}))
        }
    }

    /** 创建自定义背压Flowable */
    private fun createBpFlowable(): Flowable<String> =
        Flowable.create({ emitter ->
            val fileName = "test.txt"
            var isr: InputStreamReader? = null
            var br: BufferedReader? = null
            try {
                isr = InputStreamReader(assets.open(fileName), StandardCharsets.UTF_8)
                br = BufferedReader(isr)

                var line: String?

                while (true) {
                    line = br.readLine() ?: break
                    emitter.requested()
                    while (!mBinding.backpressureSwitch.isChecked && emitter.requested() == 0L) {
                        //下游无法处理数据时循环等待
                        PrintLog.d("testtag", "等待下游")
                        if (emitter.isCancelled) {
                            break
                        }
                    }
                    if (emitter.isCancelled) {
                        break
                    }
                    PrintLog.i("testtag", "发送数据：$line")
                    emitter.doNext(line)
                }

                isr.close()
                br.close()
                PrintLog.v("testtag", "发送完成")
                emitter.doComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                isr?.close()
                br?.close()
                emitter.doError(e)
            }

        }, mBackpressureType)

    /** 创建自定义Flowable，[isDelay]是否延时 */
    private fun createFlowable(isDelay: Boolean): Flowable<ResponseBean<String>> =
        Flowable.create({ emitter ->
            val delayTime = isDelay.then { 3 } ?: 0
            val responseBean: ResponseBean<String> = if (mBinding.failSwitch.isChecked) {
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
        }, BackpressureStrategy.BUFFER)

    override fun initData() {
        super.initData()
        mBinding.errorRb.isChecked = true
        showStatusCompleted()
    }

    /** 清空日志 */
    private fun cleanLog() {
        mBinding.resultTv.text = ""
    }

    /** 打印日志 */
    private fun printLog(text: String) {
        val log = if (mBinding.resultTv.text.isEmpty()) {
            text
        } else {
            "${mBinding.resultTv.text}\n$text"
        }
        mBinding.resultTv.text = log
        GlobalScope.runOnMainDelay(100) {
            mBinding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }
}