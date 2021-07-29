package com.lodz.android.agiledevkt.modules.mvc.sandwich

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityMvcTestBinding
import com.lodz.android.agiledevkt.databinding.ViewMvcTestBottomBinding
import com.lodz.android.agiledevkt.databinding.ViewMvcTestTopBinding
import com.lodz.android.agiledevkt.modules.mvc.ApiModuleRx
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseSandwichActivity
import com.lodz.android.pandora.rx.subscribe.observer.RxObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayoutLazy
import com.trello.rxlifecycle4.android.ActivityEvent
import kotlin.random.Random

/**
 * MVC带基础状态控件、中部刷新控件和顶部/底部扩展Activity
 * Created by zhouL on 2018/11/19.
 */
class MvcTestSandwichActivity : BaseSandwichActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, MvcTestSandwichActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 内容布局 */
    private val mContentBinding: ActivityMvcTestBinding by bindingLayoutLazy(ActivityMvcTestBinding::inflate)
    /** 顶部布局 */
    private val mTopBinding: ViewMvcTestTopBinding by bindingLayoutLazy(ViewMvcTestTopBinding::inflate)
    /** 底部布局 */
    private val mBottomBinding: ViewMvcTestBottomBinding by bindingLayoutLazy(ViewMvcTestBottomBinding::inflate)

    override fun getViewBindingLayout(): View = mContentBinding.root

    override fun getTopViewBindingLayout(): View = mTopBinding.root

    override fun getBottomViewBindingLayout(): View = mBottomBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        mTopBinding.titleBarLayout.setTitleName(R.string.mvc_demo_sandwich_title)
        setSwipeRefreshEnabled(true)
    }

    override fun onDataRefresh() {
        super.onDataRefresh()
        getRefreshData(Random.nextInt(9) % 2 == 0)
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        getResult(true)
    }

    override fun setListeners() {
        super.setListeners()

        // 顶部标题栏
        mTopBinding.titleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        mTopBinding.topNameTv.setOnClickListener {
            toastShort(mTopBinding.topNameTv.text.toString())
        }

        // 获取成功数据按钮
        mContentBinding.getSuccessReusltBtn.setOnClickListener {
            showStatusLoading()
            getResult(true)
        }

        // 获取失败数据按钮
        mContentBinding.getFailReusltBtn.setOnClickListener {
            showStatusLoading()
            getResult(false)
        }

        mBottomBinding.bottomNameTv.setOnClickListener {
            toastShort(mBottomBinding.bottomNameTv.text.toString())
        }
    }

    override fun initData() {
        super.initData()
        showStatusNoData()
    }

    private fun getResult(isSuccess: Boolean) {
        ApiModuleRx.requestResult(isSuccess)
                .compose(RxUtils.ioToMainObservable())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(object : RxObserver<String>() {
                    override fun onRxNext(any: String) {
                        mContentBinding.resultTv.text = any
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
                        mContentBinding.resultTv.text = any
                        showStatusCompleted()
                    }

                    override fun onRxError(e: Throwable, isNetwork: Boolean) {
                        setSwipeRefreshFinish()
                        toastShort(R.string.mvc_demo_refresh_data_fail)
                    }
                })
    }
}