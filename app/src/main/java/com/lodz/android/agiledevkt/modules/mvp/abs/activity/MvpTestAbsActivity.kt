package com.lodz.android.agiledevkt.modules.mvp.abs.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityMvpTestBinding
import com.lodz.android.agiledevkt.modules.mvp.abs.MvpTestAbsPresenter
import com.lodz.android.agiledevkt.modules.mvp.abs.MvpTestAbsViewContract
import com.lodz.android.pandora.mvp.base.activity.MvpAbsActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * MVP基础Activity
 * Created by zhouL on 2018/11/19.
 */
class MvpTestAbsActivity : MvpAbsActivity<MvpTestAbsPresenter, MvpTestAbsViewContract>(), MvpTestAbsViewContract {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MvpTestAbsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createMainPresenter(): MvpTestAbsPresenter = MvpTestAbsPresenter()

    private val mBinding: ActivityMvpTestBinding by bindingLayout(ActivityMvpTestBinding::inflate)

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun setListeners() {
        super.setListeners()
        // 获取成功数据按钮
        mBinding.getSuccessReusltBtn.setOnClickListener {
            getPresenterContract()?.getResult(true)
        }

        // 获取失败数据按钮
        mBinding.getFailReusltBtn.setOnClickListener {
            getPresenterContract()?.getResult(false)
        }

    }

    override fun showResult() {
        mBinding.resultTv.visibility = View.VISIBLE
    }

    override fun setResult(result: String) {
        mBinding.resultTv.text = result
    }
}