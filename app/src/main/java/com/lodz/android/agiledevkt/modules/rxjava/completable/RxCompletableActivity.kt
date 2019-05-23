package com.lodz.android.agiledevkt.modules.rxjava.completable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ScrollView
import android.widget.Switch
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.then
import com.lodz.android.corekt.utils.UiHandler
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.completable.BaseCompletableObserver
import com.lodz.android.pandora.rx.subscribe.completable.ProgressCompletableObserver
import com.lodz.android.pandora.rx.subscribe.completable.RxCompletableObserver
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.utils.RxCompletableOnSubscribe
import com.lodz.android.pandora.rx.utils.RxUtils
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable

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

    /** 滚动控件 */
    private val mScrollView by bindView<NestedScrollView>(R.id.scroll_view)
    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)
    /** 订阅失败开关 */
    private val mFailSwitch by bindView<Switch>(R.id.fail_switch)
    /** Completable转Observable订阅按钮 */
    private val mCompletableToObservableBtn by bindView<MaterialButton>(R.id.completable_to_observable_btn)
    /** 完成订阅按钮 */
    private val mCompleteBtn by bindView<MaterialButton>(R.id.complete_btn)
    /** 基类封装订阅按钮 */
    private val mBaseBtn by bindView<MaterialButton>(R.id.base_btn)
    /** 响应数据封装按钮 */
    private val mRxBtn by bindView<MaterialButton>(R.id.rx_btn)
    /** 返回键关闭开关 */
    private val mCancelableSwitch by bindView<Switch>(R.id.cancelable_switch)
    /** 空白处关闭开关 */
    private val mCanceledOutsideSwitch by bindView<Switch>(R.id.canceled_outside_switch)
    /** 进度条封装按钮 */
    private val mProgressBtn by bindView<MaterialButton>(R.id.progress_btn)

    override fun getLayoutId(): Int = R.layout.activity_rx_completable

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

        // Completable转Observable订阅按钮
        mCompletableToObservableBtn.setOnClickListener {
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
        mCompleteBtn.setOnClickListener {
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
        mBaseBtn.setOnClickListener {
            cleanLog()
            createCompletable()
                    .compose(RxUtils.ioToMainCompletable())
                    .compose(bindAnyDestroyEvent())
                    .subscribe(object : BaseCompletableObserver() {
                        override fun onBaseComplete() {
                            printLog("onBaseComplete")
                        }

                        override fun onBaseError(e: Throwable) {
                            printLog("onBaseError : ${e.message}")
                        }
                    })
        }


        // 响应数据封装按钮
        mRxBtn.setOnClickListener {
            cleanLog()
            createCompletable()
                    .compose(RxUtils.ioToMainCompletable())
                    .compose(bindAnyDestroyEvent())
                    .subscribe(object : RxCompletableObserver() {
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
            createCompletable(true)
                    .compose(RxUtils.ioToMainCompletable())
                    .compose(bindAnyDestroyEvent())
                    .subscribe(object : ProgressCompletableObserver() {
                        override fun onPgComplete() {
                            printLog("onPgComplete")
                        }

                        override fun onPgError(e: Throwable, isNetwork: Boolean) {
                            printLog("onPgError message : ${RxUtils.getExceptionTips(e, isNetwork, "create fail")}")
                        }
                    }.create(getContext(), "loading", mCancelableSwitch.isChecked, mCanceledOutsideSwitch.isChecked))
        }
    }

    private fun createCompletable(isDelay: Boolean = false): Completable {
        return Completable.create(object : RxCompletableOnSubscribe(isDelay.then { 3 } ?: 0) {
            override fun subscribe(emitter: CompletableEmitter) {
                val delayTime = getArgs()[0] as Int
                try {
                    Thread.sleep(delayTime * 1000L)
                    if (mFailSwitch.isChecked) {
                        doError(emitter, NullPointerException("data empty"))
                    } else {
                        doComplete(emitter)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    doError(emitter, e)
                }
            }
        })
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
        UiHandler.postDelayed(100){
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

}