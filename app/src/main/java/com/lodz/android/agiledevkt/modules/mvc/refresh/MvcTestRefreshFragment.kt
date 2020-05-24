package com.lodz.android.agiledevkt.modules.mvc.refresh

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvc.ApiModuleRx
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.fragment.BaseRefreshFragment
import com.lodz.android.pandora.rx.subscribe.observer.RxObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.trello.rxlifecycle4.android.FragmentEvent
import kotlin.random.Random

/**
 * 刷新测试类
 * Created by zhouL on 2018/11/19.
 */
class MvcTestRefreshFragment : BaseRefreshFragment() {

    companion object {
        fun newInstance(): MvcTestRefreshFragment = MvcTestRefreshFragment()
    }

    /** 结果 */
    private val mResult by bindView<TextView>(R.id.result)
    /** 获取成功数据按钮 */
    private val mGetSuccessResultBtn by bindView<Button>(R.id.get_success_reuslt_btn)
    /** 获取失败数据按钮 */
    private val mGetFailResultBtn by bindView<Button>(R.id.get_fail_reuslt_btn)

    override fun getLayoutId(): Int = R.layout.activity_mvc_test

    override fun onDataRefresh() {
        getRefreshData(Random.nextInt(9) % 2 == 0)
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        getResult(true)
    }

    override fun setListeners(view: View) {
        super.setListeners(view)
        mGetSuccessResultBtn.setOnClickListener {
            showStatusLoading()
            getResult(true)
        }

        mGetFailResultBtn.setOnClickListener {
            showStatusLoading()
            getResult(false)
        }
    }

    override fun initData(view: View) {
        super.initData(view)
        showStatusLoading()
        getResult(true)
    }

    private fun getResult(isSuccess: Boolean) {
        ApiModuleRx.requestResult(isSuccess)
                .compose(RxUtils.ioToMainObservable())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(object : RxObserver<String>() {
                    override fun onRxNext(any: String) {
                        mResult.text = any
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
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(object : RxObserver<String>() {
                    override fun onRxNext(any: String) {
                        setSwipeRefreshFinish()
                        mResult.text = any
                    }

                    override fun onRxError(e: Throwable, isNetwork: Boolean) {
                        setSwipeRefreshFinish()
                        toastShort(R.string.mvc_demo_refresh_data_fail)
                    }
                })
    }
}