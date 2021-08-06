package com.lodz.android.agiledevkt.modules.mvc.sandwich

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityMvcTestBinding
import com.lodz.android.agiledevkt.databinding.ViewMvcTestBottomBinding
import com.lodz.android.agiledevkt.databinding.ViewMvcTestTopBinding
import com.lodz.android.agiledevkt.modules.mvc.ApiModuleRx
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.fragment.BaseSandwichFragment
import com.lodz.android.pandora.rx.subscribe.observer.RxObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.trello.rxlifecycle4.android.FragmentEvent
import kotlin.random.Random

/**
 * MVC带基础状态控件、中部刷新控件和顶部/底部扩展Fragment
 * Created by zhouL on 2018/11/19.
 */
class MvcTestSandwichFragment : BaseSandwichFragment(){

    companion object {
        fun newInstance(): MvcTestSandwichFragment = MvcTestSandwichFragment()
    }

    /** 内容布局 */
    private var mContentBinding: ActivityMvcTestBinding? = null
    /** 顶部布局 */
    private var mTopBinding: ViewMvcTestTopBinding? = null
    /** 底部布局 */
    private var mBottomBinding: ViewMvcTestBottomBinding? = null

    override fun getViewBindingLayout(): View? {
        mContentBinding = ActivityMvcTestBinding.inflate(LayoutInflater.from(context))
        return mContentBinding?.root
    }

    override fun getTopViewBindingLayout(): View? {
        mTopBinding = ViewMvcTestTopBinding.inflate(LayoutInflater.from(context))
        return mTopBinding?.root
    }

    override fun getBottomViewBindingLayout(): View? {
        mBottomBinding = ViewMvcTestBottomBinding.inflate(LayoutInflater.from(context))
        return mBottomBinding?.root
    }

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        mTopBinding?.titleBarLayout?.visibility = View.GONE
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

    override fun setListeners(view: View) {
        super.setListeners(view)
        // 获取成功数据按钮
        mContentBinding?.getSuccessReusltBtn?.setOnClickListener {
            showStatusLoading()
            getResult(true)
        }
        // 获取失败数据按钮
        mContentBinding?.getFailReusltBtn?.setOnClickListener {
            showStatusLoading()
            getResult(false)
        }
    }

    override fun initData(view: View) {
        super.initData(view)
        showStatusNoData()
    }

    private fun getResult(isSuccess: Boolean) {
        ApiModuleRx.requestResult(isSuccess)
            .compose(RxUtils.ioToMainObservable())
            .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
            .subscribe(object : RxObserver<String>() {
                override fun onRxNext(any: String) {
                    mContentBinding?.resultTv?.text = any
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
                    mContentBinding?.resultTv?.text = any
                    showStatusCompleted()
                }

                override fun onRxError(e: Throwable, isNetwork: Boolean) {
                    setSwipeRefreshFinish()
                    toastShort(R.string.mvc_demo_refresh_data_fail)
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mContentBinding = null
        mTopBinding = null
        mBottomBinding = null
    }
}