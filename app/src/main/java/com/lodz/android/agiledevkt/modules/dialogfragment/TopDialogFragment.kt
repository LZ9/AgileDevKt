package com.lodz.android.agiledevkt.modules.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lodz.android.agiledevkt.config.Constant
import com.lodz.android.agiledevkt.databinding.DialogFragmentTopBinding
import com.lodz.android.corekt.anko.setupViewPager
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.dialogfragment.BaseTopDialogFragment
import com.lodz.android.pandora.widget.vp2.SimpleVp2Adapter

/**
 * 底部DialogFragment测试类
 * Created by zhouL on 2018/12/13.
 */
class TopDialogFragment : BaseTopDialogFragment() {

    private val mBinding: DialogFragmentTopBinding by bindingLayout(DialogFragmentTopBinding::inflate)

    override fun getViewBindingLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = mBinding.root

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        super.findViews(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        val list = ArrayList<Fragment>()
        for (i in 0 until Constant.TAB_NAME.size) {
            list.add(TestFragment.newInstance(Constant.TAB_NAME[i]))
        }
        mBinding.viewPager.adapter = SimpleVp2Adapter(this, list)
        mBinding.viewPager.offscreenPageLimit = Constant.TAB_NAME.size
        mBinding.viewPager.setCurrentItem(0, true)
        mBinding.tabLayout.setupViewPager(mBinding.viewPager, Constant.TAB_NAME)
    }

}