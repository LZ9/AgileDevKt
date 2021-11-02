package com.lodz.android.agiledevkt.modules.extras

import android.view.View
import com.lodz.android.agiledevkt.databinding.FragmentExtrasResultBinding
import com.lodz.android.pandora.base.fragment.BaseFragment
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 参数传递给Fragment
 * @author zhouL
 * @date 2021/11/1
 */
class ExtrasToFragment : BaseFragment() {

    private val mBinding: FragmentExtrasResultBinding by bindingLayout(FragmentExtrasResultBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun initData(view: View) {
        super.initData(view)
        showStatusCompleted()
    }
}