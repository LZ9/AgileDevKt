package com.lodz.android.agiledevkt.modules.mvvm.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.mvvm.base.activity.BaseVmActivity

/**
 * MVVM带基础状态控件Activity
 * @author zhouL
 * @date 2019/12/4
 */
class MvvmTestBaseActivity : BaseVmActivity<MvvmTestBaseViewModel>() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, MvvmTestBaseActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 结果 */
    private val mResult by bindView<TextView>(R.id.result)
    /** 获取成功数据按钮 */
    private val mGetSuccessResultBtn by bindView<Button>(R.id.get_success_reuslt_btn)
    /** 获取失败数据按钮 */
    private val mGetFailResultBtn by bindView<Button>(R.id.get_fail_reuslt_btn)

    override fun createViewModel(): Class<MvvmTestBaseViewModel> = MvvmTestBaseViewModel::class.java

    override fun getLayoutId(): Int  = R.layout.activity_mvvm_test

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.mvvm_demo_base_title)
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        getViewModel().getResult(true)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

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

    override fun initData() {
        super.initData()
        showStatusLoading()
        getViewModel().getResult(true)
    }
}