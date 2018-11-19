package com.lodz.android.agiledevkt.modules.mvc.refresh

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvc.ApiModule
import com.lodz.android.componentkt.base.activity.BaseRefreshActivity
import com.lodz.android.componentkt.rx.subscribe.observer.RxObserver
import com.lodz.android.componentkt.rx.utils.RxUtils
import com.lodz.android.componentkt.widget.base.TitleBarLayout
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.utils.toastShort
import com.trello.rxlifecycle3.android.ActivityEvent
import kotlin.random.Random

/**
 * 带基础状态控件和下来刷新控件Activity
 * Created by zhouL on 2018/11/19.
 */
class MvcTestRefreshActivity : BaseRefreshActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, MvcTestRefreshActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 结果 */
    private val mResult by bindView<TextView>(R.id.result)
    /** 获取成功数据按钮 */
    private val mGetSuccessResultBtn by bindView<Button>(R.id.get_success_reuslt_btn)
    /** 获取失败数据按钮 */
    private val mGetFailResultBtn by bindView<Button>(R.id.get_fail_reuslt_btn)

    override fun getLayoutId(): Int = R.layout.activity_mvc_test

    override fun findViews(savedInstanceState: Bundle?) {
        initTitleBarLayout(getTitleBarLayout())
    }

    private fun initTitleBarLayout(titleBarLayout: TitleBarLayout) {
        titleBarLayout.setTitleName(R.string.mvc_demo_refresh_title)
    }

    override fun onDataRefresh() {
        getRefreshData(Random.nextInt(9) % 2 == 0)
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        getResult(true)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mGetSuccessResultBtn.setOnClickListener {
            showStatusLoading()
            getResult(true)
        }

        mGetFailResultBtn.setOnClickListener {
            showStatusLoading()
            getResult(false)
        }
    }

    override fun initData() {
        super.initData()
        showStatusLoading()
        getResult(true)
    }

    private fun getResult(isSuccess: Boolean) {
        ApiModule.requestResult(isSuccess)
                .compose(RxUtils.ioToMainObservable())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(object : RxObserver<String>() {
                    override fun onRxNext(any: String) {
                        mResult.text = any
                        showStatusCompleted()
                    }

                    override fun onRxError(e: Throwable, isNetwork: Boolean) {
                        showStatusError()
                    }
                })
    }

    private fun getRefreshData(isSuccess: Boolean) {
        ApiModule.requestResult(isSuccess)
                .compose(RxUtils.ioToMainObservable())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(object : RxObserver<String>() {
                    override fun onRxNext(any: String) {
                        setSwipeRefreshFinish()
                        mResult.text = any
                        showStatusCompleted()
                    }

                    override fun onRxError(e: Throwable, isNetwork: Boolean) {
                        setSwipeRefreshFinish()
                        toastShort(R.string.mvc_demo_refresh_data_fail)
                    }
                })
    }
}