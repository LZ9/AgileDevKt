package com.lodz.android.agiledevkt.modules.mvc.base

import android.view.LayoutInflater
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityMvcTestBinding
import com.lodz.android.agiledevkt.modules.mvc.ApiModuleRx
import com.lodz.android.pandora.base.fragment.BaseFragment
import com.lodz.android.pandora.rx.subscribe.observer.RxObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.trello.rxlifecycle4.android.FragmentEvent

/**
 * 带基础控件的MVC测试类
 * Created by zhouL on 2018/11/19.
 */
class MvcTestBaseFragment : BaseFragment() {

    companion object {
        fun newInstance(): MvcTestBaseFragment = MvcTestBaseFragment()
    }

    private var mBinding : ActivityMvcTestBinding? = null

    override fun getViewBindingLayout(): View? {
        mBinding = ActivityMvcTestBinding.inflate(LayoutInflater.from(context))
        return mBinding?.root
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        getResult(true)
    }

    override fun setListeners(view: View) {
        super.setListeners(view)
        // 获取成功数据按钮
        mBinding?.getSuccessReusltBtn?.setOnClickListener {
            showStatusLoading()
            getResult(true)
        }
        // 获取失败数据按钮
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

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}