package com.lodz.android.agiledevkt.modules.mvc.refresh

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityMvcTestBinding
import com.lodz.android.agiledevkt.modules.mvc.ApiModuleRx
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseRefreshActivity
import com.lodz.android.pandora.rx.subscribe.observer.RxObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.base.TitleBarLayout
import com.trello.rxlifecycle4.android.ActivityEvent
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

    private val mBinding: ActivityMvcTestBinding by bindingLayout(ActivityMvcTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

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
        // 获取成功数据按钮
        mBinding.getSuccessReusltBtn.setOnClickListener {
            showStatusLoading()
            getResult(true)
        }

        // 获取失败数据按钮
        mBinding.getFailReusltBtn.setOnClickListener {
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
        ApiModuleRx.requestResult(isSuccess)
                .compose(RxUtils.ioToMainObservable())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(object : RxObserver<String>() {
                    override fun onRxNext(any: String) {
                        mBinding.resultTv.text = any
                        showStatusCompleted()
                    }

                    override fun onRxError(e: Throwable, isNetwork: Boolean) {
                        showStatusError(e)
                    }
                })
    }

    private fun getRefreshData(isSuccess: Boolean) {
        ApiModuleRx.requestResult(isSuccess)
                .compose(RxUtils.ioToMainObservable())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(object : RxObserver<String>() {
                    override fun onRxNext(any: String) {
                        setSwipeRefreshFinish()
                        mBinding.resultTv.text = any
                        showStatusCompleted()
                    }

                    override fun onRxError(e: Throwable, isNetwork: Boolean) {
                        setSwipeRefreshFinish()
                        toastShort(R.string.mvc_demo_refresh_data_fail)
                    }
                })
    }
}