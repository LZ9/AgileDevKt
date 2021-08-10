package com.lodz.android.agiledevkt.modules.rxjava.completable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import com.lodz.android.agiledevkt.databinding.ActivityRxCompletableBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.runOnMainDelay
import com.lodz.android.corekt.anko.then
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.completable.BaseCompletableObserver
import com.lodz.android.pandora.rx.subscribe.completable.ProgressCompletableObserver
import com.lodz.android.pandora.rx.subscribe.completable.RxCompletableObserver
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.rx.utils.doComplete
import com.lodz.android.pandora.rx.utils.doError
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.GlobalScope

/**
 * RxCompletable订阅测试
 * 不发送事件，以完成或失败结束
 * Created by zhouL on 2019/1/22.
 */
class RxCompletableActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RxCompletableActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityRxCompletableBinding by bindingLayout(ActivityRxCompletableBinding::inflate)

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

        // Completable转Observable订阅按钮
        mBinding.completableToObservableBtn.setOnClickListener {
            cleanLog()
            createCompletable()
                    .toObservable<Any>()
                    .compose(RxUtils.ioToMainObservable())
                    .compose(bindDestroyEvent())
                    .subscribe(object : BaseObserver<Any>() {
                        override fun onBaseSubscribe(d: Disposable) {
                            super.onBaseSubscribe(d)
                            printLog("onBaseSubscribe")
                        }

                        override fun onBaseNext(any: Any) {
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

        // 完成订阅按钮
        mBinding.completeBtn.setOnClickListener {
            cleanLog()
            createCompletable()
                    .compose(RxUtils.ioToMainCompletable())
                    .compose(bindAnyDestroyEvent())
                    .subscribe(object : CompletableObserver {
                        override fun onSubscribe(d: Disposable) {
                            printLog("onSubscribe")
                        }

                        override fun onComplete() {
                            printLog("onComplete")
                        }

                        override fun onError(e: Throwable) {
                            printLog("onError : ${e.message}")
                        }
                    })
        }

        // 基类封装订阅按钮
        mBinding.baseBtn.setOnClickListener {
            cleanLog()
            createCompletable()
                    .compose(RxUtils.ioToMainCompletable())
                    .compose(bindAnyDestroyEvent())
                    .subscribe(BaseCompletableObserver.action({
                        printLog("onBaseComplete")
                    }, { e ->
                        printLog("onBaseError : ${e.message}")
                    }))
        }


        // 响应数据封装按钮
        mBinding.rxBtn.setOnClickListener {
            cleanLog()
            createCompletable()
                    .compose(RxUtils.ioToMainCompletable())
                    .compose(bindAnyDestroyEvent())
                    .subscribe(RxCompletableObserver.action({
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
            createCompletable(true)
                .compose(RxUtils.ioToMainCompletable())
                .compose(bindAnyDestroyEvent())
                .subscribe(ProgressCompletableObserver.action(getContext(),"loading", mBinding.cancelableSwitch.isChecked, mBinding.canceledOutsideSwitch.isChecked,
                    { printLog("onPgComplete") },
                    { e, isNetwork -> printLog("onPgError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")}))
        }
    }

    private fun createCompletable(isDelay: Boolean = false): Completable =
        Completable.create { emitter ->
            val delayTime = isDelay.then { 3 } ?: 0
            try {
                Thread.sleep(delayTime * 1000L)
                if (mBinding.failSwitch.isChecked) {
                    emitter.doError(NullPointerException("data empty"))
                } else {
                    emitter.doComplete()
                }
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
        GlobalScope.runOnMainDelay(100) {
            mBinding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

}