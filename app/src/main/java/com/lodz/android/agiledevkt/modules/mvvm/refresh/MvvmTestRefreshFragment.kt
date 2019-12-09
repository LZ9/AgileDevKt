package com.lodz.android.agiledevkt.modules.mvvm.refresh

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.mvvm.base.fragment.BaseRefreshVmFragment
import kotlin.random.Random

/**
 * MVVM带基础状态控件和下拉刷新控件Fragment
 * @author zhouL
 * @date 2019/12/9
 */
class MvvmTestRefreshFragment : BaseRefreshVmFragment<MvvmTestRefreshViewModel>() {

    companion object {
        fun newInstance(): MvvmTestRefreshFragment = MvvmTestRefreshFragment()
    }

    /** 结果 */
    private val mResult by bindView<TextView>(R.id.result)
    /** 获取成功数据按钮 */
    private val mGetSuccessResultBtn by bindView<Button>(R.id.get_success_reuslt_btn)
    /** 获取失败数据按钮 */
    private val mGetFailResultBtn by bindView<Button>(R.id.get_fail_reuslt_btn)

    override fun createViewModel(): Class<MvvmTestRefreshViewModel> = MvvmTestRefreshViewModel::class.java

    override fun getLayoutId(): Int = R.layout.activity_mvvm_test

    override fun onDataRefresh() {
        getViewModel().getRefreshData(Random.nextInt(9) % 2 == 0)
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        getViewModel().getResult(true)
    }

    override fun setListeners(view: View) {
        super.setListeners(view)
        mGetSuccessResultBtn.setOnClickListener {
            showStatusLoading()
            getViewModel().getResult(true)
        }

        mGetFailResultBtn.setOnClickListener {
            showStatusLoading()
            getViewModel().getResult(false)
        }

        getViewModel().mResultText.observe(this, Observer { value ->
            mResult.text = value
        })
    }

    override fun initData(view: View) {
        super.initData(view)
        showStatusLoading()
        getViewModel().getResult(true)
    }
}