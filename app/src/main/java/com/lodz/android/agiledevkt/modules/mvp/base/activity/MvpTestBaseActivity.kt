package com.lodz.android.agiledevkt.modules.mvp.base.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityMvpTestBinding
import com.lodz.android.agiledevkt.modules.mvp.base.MvpTestBasePresenter
import com.lodz.android.agiledevkt.modules.mvp.base.MvpTestBaseViewContract
import com.lodz.android.pandora.mvp.base.activity.MvpBaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 带基础控件的MVP测试类
 * @author zhouL
 * @date 2020/12/3
 */
class MvpTestBaseActivity : MvpBaseActivity<MvpTestBasePresenter, MvpTestBaseViewContract>(), MvpTestBaseViewContract {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MvpTestBaseActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createMainPresenter(): MvpTestBasePresenter = MvpTestBasePresenter()

    private val mBinding: ActivityMvpTestBinding by bindingLayout(ActivityMvpTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.mvp_demo_base_title)
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        getPresenterContract()?.getResult(true)
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
            getPresenterContract()?.getResult(true)
        }

        // 获取失败数据按钮
        mBinding.getFailReusltBtn.setOnClickListener {
            showStatusLoading()
            getPresenterContract()?.getResult(false)
        }
    }

    override fun initData() {
        super.initData()
        showStatusLoading()
        getPresenterContract()?.getResult(true)
    }

    override fun showResult() {
        mBinding.resultTv.visibility = View.VISIBLE
    }

    override fun setResult(result: String) {
        mBinding.resultTv.text = result
    }
}