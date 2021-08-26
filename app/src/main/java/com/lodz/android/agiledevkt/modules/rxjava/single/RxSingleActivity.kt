package com.lodz.android.agiledevkt.modules.rxjava.single

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import com.lodz.android.agiledevkt.databinding.ActivityRxSingleBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
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
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.MainScope

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

    private val mBinding: ActivityRxSingleBinding by bindingLayout(ActivityRxSingleBinding::inflate)

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

        // Single转Observable订阅按钮
        mBinding.singleToObservableBtn.setOnClickListener {
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
        mBinding.originalBtn.setOnClickListener {
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
        mBinding.baseBtn.setOnClickListener {
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
        mBinding.rxBtn.setOnClickListener {
            cleanLog()
            createSingle(false)
                .compose(RxUtils.ioToMainSingle())
                .compose(bindDestroyEvent())
                .subscribe(RxSingleObserver.action(
                    { any ->
                        printLog("onRxNext num : ${any.data}")
                    }, { e, isNetwork ->
                        printLog("onRxError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")
                    })
                )
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
            createSingle(true)
                .compose(RxUtils.ioToMainSingle())
                .compose(bindDestroyEvent())
                .subscribe(
                    ProgressSingleObserver.action(getContext(),
                        "loading",
                        mBinding.cancelableSwitch.isChecked,
                        mBinding.canceledOutsideSwitch.isChecked,
                        { any ->
                            printLog("onPgSuccess num : ${any.data}")
                        },
                        { e, isNetwork ->
                            printLog("onPgError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")
                        })
                )
        }
    }

    private fun createSingle(): Single<String> = Single.create { emitter ->
        val data = mBinding.failSwitch.isChecked.then { "" } ?: "data"
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
        MainScope().runOnMainDelay(100) {
            mBinding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }
}