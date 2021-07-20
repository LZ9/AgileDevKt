package com.lodz.android.agiledevkt.modules.viewbinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.lodz.android.agiledevkt.databinding.ActivityViewBindingTestBinding
import com.lodz.android.pandora.widget.vp2.SimpleTabAdapter

/**
 * ViewBinding测试类
 * @author zhouL
 * @date 2021/7/19
 */
class ViewBindingTestActivity : AppCompatActivity() {

    /** tab数据 */
    private val TOP_TAB_NAMES = arrayListOf("A", "B", "C")

    private lateinit var mBinding: ActivityViewBindingTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityViewBindingTestBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initViewPager()
    }

    private fun initViewPager() {
        val list = ArrayList<Fragment>()
        for (i in 0 until TOP_TAB_NAMES.size) {
            list.add(ViewBindingTestFragment.newInstance(TOP_TAB_NAMES[i]))
        }
        mBinding.viewPager.adapter = SimpleTabAdapter(this, list)
        mBinding.viewPager.offscreenPageLimit = TOP_TAB_NAMES.size
        mBinding.viewPager.setCurrentItem(0, true)
        TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager) { tab, position ->
            tab.text = TOP_TAB_NAMES[position]
        }.attach()
    }
}