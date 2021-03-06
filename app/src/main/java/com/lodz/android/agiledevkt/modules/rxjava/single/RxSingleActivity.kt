package com.lodz.android.agiledevkt.modules.rxjava.single

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.runOnMainDelay
import com.lodz.android.corekt.anko.then
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.subscribe.single.BaseSingleObserver
import com.lodz.android.pandora.rx.subscribe.single.ProgressSingleObserver
import com.lodz.android.pandora.rx.subscribe.single.RxSingleObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.rx.utils.doError
import com.lodz.android.pandora.rx.utils.doSuccess
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.GlobalScope

/**
 * RxSingle订阅测试
 * 发送单个事件，以发送成功或错误事件终止
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
    private val mFailSwitch by bindView<SwitchMaterial>(R.id.fail_switch)
    /** Single转Observable订阅按钮 */
    private val mSingleToObservableBtn by bindView<MaterialButton>(R.id.single_to_observable_btn)
    /** 原始订阅接口按钮 */
    private val mOriginalBtn by bindView<MaterialButton>(R.id.original_btn)
    /** 基础封装按钮 */
    private val mBaseBtn by bindView<MaterialButton>(R.id.base_btn)
    /** 返回键关闭开关 */
    private val mCancelableSwitch by bindView<SwitchMaterial>(R.id.cancelable_switch)
    /** 空白处关闭开关 */
    private val mCanceledOutsideSwitch by bindView<SwitchMaterial>(R.id.canceled_outside_switch)
    /** 响应数据封装按钮 */
    private val mRxBtn by bindView<MaterialButton>(R.id.rx_btn)
    /** 进度条封装按钮 */
    private val mProgressBtn by bindView<MaterialButton>(R.id.progress_btn)

    override fun getLayoutId(): Int = R.layout.activity_rx_single

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

        // Single转Observable订阅按钮
        mSingleToObservableBtn.setOnClickListener {
            cleanLog()
            createSingle()
                    .compose(RxUtils.ioToMainSingle())
                    .compose(bindDestroyEvent())
                    .toObservable()
                    .subscribe(BaseObserver.action({ any ->
                        printLog("onBaseNext data : $any")
                    }, { e ->
                        printLog("onBaseError message : ${e.message}")
                    }))
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
                    .subscribe(BaseSingleObserver.action({ any ->
                        printLog("onBaseSuccess data : $any")
                    }, { e ->
                        printLog("onBaseError message : ${e.message}")
                    }))
        }

        // 响应数据封装按钮
        mRxBtn.setOnClickListener {
            cleanLog()
            createSingle(false)
                    .compose(RxUtils.ioToMainSingle())
                    .compose(bindDestroyEvent())
                    .subscribe(RxSingleObserver.action({ any ->
                        printLog("onRxNext num : ${any.data}")
                    }, { e, isNetwork ->
                        printLog("onRxError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")
                    }))
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
                    .subscribe(ProgressSingleObserver.action(getContext(),"loading", mCancelableSwitch.isChecked, mCanceledOutsideSwitch.isChecked,
                        { any -> printLog("onPgSuccess num : ${any.data}")},
                        { e, isNetwork -> printLog("onPgError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")}))
        }
    }

    private fun createSingle(): Single<String> = Single.create { emitter ->
        val data = mFailSwitch.isChecked.then { "" } ?: "data"
        if (emitter.isDisposed) {
            return@create
        }
        try {
            if (data.isNotEmpty()) {
                emitter.doSuccess(data)
            } else {
                emitter.doError(NullPointerException("data empty"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emitter.doError(e)
        }
    }

    private fun createSingle(isDelay: Boolean): Single<ResponseBean<String>> =
        Single.create { emitter ->
            val delayTime = isDelay.then { 3 } ?: 0
            if (emitter.isDisposed) {
                return@create
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
                emitter.doSuccess(responseBean)
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.doError(e)
            }
        }

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
        GlobalScope.runOnMainDelay(100) {
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }
}