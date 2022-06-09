package com.lodz.android.agiledevkt.modules.rxjava.maybe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import com.lodz.android.agiledevkt.databinding.ActivityRxMaybeBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.runOnMainDelay
import com.lodz.android.corekt.anko.then
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.maybe.BaseMaybeObserver
import com.lodz.android.pandora.rx.subscribe.maybe.ProgressMaybeObserver
import com.lodz.android.pandora.rx.subscribe.maybe.RxMaybeObserver
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.rx.utils.doComplete
import com.lodz.android.pandora.rx.utils.doError
import com.lodz.android.pandora.rx.utils.doSuccess
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.MaybeObserver
import io.reactivex.rxjava3.disposables.Disposable

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

    private val mDataEmptyStr = "data empty"

    private val mBinding: ActivityRxMaybeBinding by bindingLayout(ActivityRxMaybeBinding::inflate)

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

        // Maybe转Observable订阅按钮
        mBinding.maybeToObservableBtn.setOnClickListener {
            cleanLog()

            Maybe.create<String> { emitter ->
                if (mBinding.failSwitch.isChecked) {
                    emitter.doError(NullPointerException(mDataEmptyStr))
                } else {
                    emitter.doSuccess("data")
                }
            }.toObservable()
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
        mBinding.successBtn.setOnClickListener {
            cleanLog()
            Maybe.create<String> { emitter ->
                val data = mBinding.failSwitch.isChecked.then { "" } ?: "data"
                try {
                    if (data.isNotEmpty()) {
                        emitter.doSuccess(data)
                    } else {
                        emitter.doError(NullPointerException(mDataEmptyStr))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    emitter.doError(e)
                }
            }.compose(RxUtils.ioToMainMaybe())
                .compose(bindDestroyEvent())
                .subscribe(
                    BaseMaybeObserver.action(
                        { any -> printLog("onBaseSuccess : $any") },
                        { printLog("onBaseComplete") },
                        { e -> printLog("onBaseError : ${e.message}") })
                )
        }

        // 完成订阅按钮
        mBinding.completeBtn.setOnClickListener {
            cleanLog()
            Maybe.create<String> { emitter ->
                val data = mBinding.failSwitch.isChecked.then { "" } ?: "data"
                try {
                    if (data.isNotEmpty()) {
                        emitter.doComplete()
                    } else {
                        emitter.doError(NullPointerException(mDataEmptyStr))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    emitter.doError(e)
                }
            }.compose(RxUtils.ioToMainMaybe())
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
        mBinding.rxBtn.setOnClickListener {
            cleanLog()
            createMaybe(false)
                    .compose(RxUtils.ioToMainMaybe())
                    .compose(bindDestroyEvent())
                    .subscribe(RxMaybeObserver.action({ any ->
                        printLog("onRxSuccess num : ${any.data}")
                    }, {
                        printLog("onRxComplete")
                    }, { e, isNetwork ->
                        printLog("onRxError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")
                    }))
        }

        // 返回键关闭开关
        mBinding.cancelableSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                mBinding.canceledOutsideSwitch.isChecked = false
            }
            mBinding.canceledOutsideSwitch.isEnabled = isChecked
        }

        // 进度条封装按钮
        mBinding.progressBtn.setOnClickListener {
            cleanLog()
            createMaybe(true)
                .compose(RxUtils.ioToMainMaybe())
                .compose(bindDestroyEvent())
                .subscribe(ProgressMaybeObserver.action(getContext(), "loading", mBinding.cancelableSwitch.isChecked, mBinding.canceledOutsideSwitch.isChecked,
                    { any -> printLog("onPgSuccess num : ${any.data}")},
                    { printLog("onPgComplete")},
                    { e, isNetwork -> printLog("onPgError message : ${RxUtils.getExceptionTips(e, isNetwork,"create fail")}")}))
        }
    }

    private fun createMaybe(isDelay: Boolean): Maybe<ResponseBean<String>> =
        Maybe.create { emitter ->
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
        runOnMainDelay(100) {
            mBinding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

}