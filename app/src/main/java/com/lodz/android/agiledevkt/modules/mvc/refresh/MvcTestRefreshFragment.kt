package com.lodz.android.agiledevkt.modules.mvc.refresh

import android.view.LayoutInflater
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityMvcTestBinding
import com.lodz.android.agiledevkt.modules.mvc.ApiModuleRx
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

    private var mBinding : ActivityMvcTestBinding? = null

    override fun getViewBindingLayout(): View? {
        mBinding = ActivityMvcTestBinding.inflate(LayoutInflater.from(context))
        return mBinding?.root
    }

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
        //获取成功数据按钮
        mBinding?.getSuccessReusltBtn?.setOnClickListener {
            showStatusLoading()
            getResult(true)
        }

        //获取失败数据按钮
        mBinding?.getFailReusltBtn?.setOnClickListener {
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
                    mBinding?.resultTv?.text = any
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
                    mBinding?.resultTv?.text = any
                }

                override fun onRxError(e: Throwable, isNetwork: Boolean) {
                    setSwipeRefreshFinish()
                    toastShort(R.string.mvc_demo_refresh_data_fail)
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}