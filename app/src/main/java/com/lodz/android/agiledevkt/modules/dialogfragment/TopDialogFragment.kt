package com.lodz.android.agiledevkt.modules.dialogfragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
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

    /** TabLayout */
    private val mTabLayout by bindView<TabLayout>(R.id.tab_layout)
    /** ViewPager */
    private val mViewPager by bindView<ViewPager2>(R.id.view_pager)

    override fun getLayoutId(): Int = R.layout.dialog_fragment_top

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        super.findViews(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        val list = ArrayList<Fragment>()
        for (i in 0 until TABS.size) {
            list.add(TestFragment.newInstance(context.getString(TABS[i])))
        }
        mViewPager.adapter = SimpleTabAdapter(this, list)
        mViewPager.offscreenPageLimit = TABS.size
        mViewPager.setCurrentItem(0, true)
        TabLayoutMediator(mTabLayout, mViewPager) { tab, position ->
            tab.text = context.getText(TABS[position])
        }.attach()
    }

}