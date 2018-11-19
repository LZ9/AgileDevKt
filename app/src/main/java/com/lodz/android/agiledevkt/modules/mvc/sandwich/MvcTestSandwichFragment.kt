package com.lodz.android.agiledevkt.modules.mvc.sandwich

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvc.ApiModule
import com.lodz.android.componentkt.base.fragment.BaseSandwichFragment
import com.lodz.android.componentkt.rx.subscribe.observer.RxObserver
import com.lodz.android.componentkt.rx.utils.RxUtils
import com.lodz.android.componentkt.widget.base.TitleBarLayout
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.utils.toastShort
import com.trello.rxlifecycle3.android.FragmentEvent
import kotlin.random.Random

/**
 * MVC带基础状态控件、中部刷新控件和顶部/底部扩展Fragment
 * Created by zhouL on 2018/11/19.
 */
class MvcTestSandwichFragment : BaseSandwichFragment(){

    companion object {
        fun newInstance(): MvcTestSandwichFragment = MvcTestSandwichFragment()
    }

    /** 结果 */
    private val mResult by bindView<TextView>(R.id.result)
    /** 获取成功数据按钮 */
    private val mGetSuccessResultBtn by bindView<Button>(R.id.get_success_reuslt_btn)
    /** 获取失败数据按钮 */
    private val mGetFailResultBtn by bindView<Button>(R.id.get_fail_reuslt_btn)
    /** 顶部标题栏 */
    private val mTitleBarLayout by bindView<TitleBarLayout>(R.id.title_bar_layout)

    override fun getLayoutId(): Int = R.layout.activity_mvc_test

    override fun getTopLayoutId(): Int = R.layout.view_mvc_test_top

    override fun getBottomLayoutId(): Int = R.layout.view_mvc_test_bottom

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        mTitleBarLayout.visibility = View.GONE
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
        showStatusNoData()
    }

    private fun getResult(isSuccess: Boolean) {
        ApiModule.requestResult(isSuccess)
                .compose(RxUtils.ioToMainObservable())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
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
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
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