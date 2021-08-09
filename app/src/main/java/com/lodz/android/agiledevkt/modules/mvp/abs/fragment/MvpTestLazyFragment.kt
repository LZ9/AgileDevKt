package com.lodz.android.agiledevkt.modules.mvp.abs.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lodz.android.agiledevkt.databinding.ActivityMvpTestBinding
import com.lodz.android.agiledevkt.modules.mvp.abs.MvpTestAbsPresenter
import com.lodz.android.agiledevkt.modules.mvp.abs.MvpTestAbsViewContract
import com.lodz.android.pandora.mvp.base.fragment.MvpLazyFragment
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * MVP测试Fragment
 * @author zhouL
 * @date 2020/12/3
 */
class MvpTestLazyFragment : MvpLazyFragment<MvpTestAbsPresenter, MvpTestAbsViewContract>(), MvpTestAbsViewContract {

    companion object {
        fun newInstance(): MvpTestLazyFragment = MvpTestLazyFragment()
    }

    override fun createMainPresenter(): MvpTestAbsPresenter = MvpTestAbsPresenter()

    private val mBinding: ActivityMvpTestBinding by bindingLayout(ActivityMvpTestBinding::inflate)

    override fun getAbsViewBindingLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = mBinding.root

    override fun setListeners(view: View) {
        super.setListeners(view)
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