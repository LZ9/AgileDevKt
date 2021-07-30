package com.lodz.android.agiledevkt.modules.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.DialogFragmentTopBinding
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.dialogfragment.BaseTopDialogFragment
import com.lodz.android.pandora.widget.vp2.SimpleTabAdapter

/**
 * 底部DialogFragment测试类
 * Created by zhouL on 2018/12/13.
 */
class TopDialogFragment : BaseTopDialogFragment() {

    private val TABS = arrayListOf(
            R.string.bottom_sheets_pikachu_pinarello,
            R.string.bottom_sheets_pikachu_colnago,
            R.string.bottom_sheets_pikachu_specialized
    )

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
        for (i in 0 until TABS.size) {
            list.add(TestFragment.newInstance(context.getString(TABS[i])))
        }
        mBinding.viewPager.adapter = SimpleTabAdapter(this, list)
        mBinding.viewPager.offscreenPageLimit = TABS.size
        mBinding.viewPager.setCurrentItem(0, true)
        TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager) { tab, position ->
            tab.text = context.getText(TABS[position])
        }.attach()
    }

}