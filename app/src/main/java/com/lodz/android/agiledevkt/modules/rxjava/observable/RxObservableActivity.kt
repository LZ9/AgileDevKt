package com.lodz.android.agiledevkt.modules.rxjava.observable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import com.lodz.android.agiledevkt.databinding.ActivityRxObservableBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
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
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.MainScope

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

    private val mBinding: ActivityRxObservableBinding by bindingLayout(ActivityRxObservableBinding::inflate)

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

        // 返回键关闭开关
        mBinding.cancelableSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                mBinding.canceledOutsideSwitch.isChecked = false
            }
            mBinding.canceledOutsideSwitch.isEnabled = isChecked
        }

        // 原始订阅接口
        mBinding.originalBtn.setOnClickListener {
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
        mBinding.baseBtn.setOnClickListener {
            cleanLog()
            createObservable()
                    .compose(RxUtils.ioToMainObservable())
                    .compose(bindDestroyEvent())
                    .subscribe(BaseObserver.action({
                        printLog("onBaseNext num : $it")
                    }, { e ->
                        printLog("onBaseError message : ${e.message}")
                    }))
        }

        // 响应数据封装
        mBinding.rxBtn.setOnClickListener {
            cleanLog()
            createObservable(false)
                    .compose(RxUtils.ioToMainObservable())
                    .compose(bindDestroyEvent())
                    .subscribe(RxObserver.action({
                        printLog("onRxNext : ${it.data}")
                    }, { e, isNetwork ->
                        printLog("onRxError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")
                    }))
        }

        // 进度条封装
        mBinding.progressBtn.setOnClickListener {
            cleanLog()
            createObservable(true)
                .compose(RxUtils.ioToMainObservable())
                .compose(bindDestroyEvent())
                .subscribe(
                    ProgressObserver.action(getContext(),
                        msg ="loading",
                        cancelable = mBinding.cancelableSwitch.isChecked,
                        canceledOnTouchOutside = mBinding.canceledOutsideSwitch.isChecked,
                        next = { printLog("onPgNext num : ${it.data}") },
                        error = { e, isNetwork ->
                            printLog("onPgError message : ${RxUtils.getExceptionTips(e, isNetwork,"create fail")}")
                        })
                )
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
            if (mBinding.failSwitch.isChecked) {
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
        MainScope().runOnMainDelay(100) {
            mBinding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }
}