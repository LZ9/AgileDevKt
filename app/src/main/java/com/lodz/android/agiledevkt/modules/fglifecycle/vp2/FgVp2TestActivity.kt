package com.lodz.android.agiledevkt.modules.fglifecycle.vp2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.vp2.SimpleTabAdapter

/**
 * ViewPager2嵌套Fragment测试
 * @author zhouL
 * @date 2020/1/21
 */
class FgVp2TestActivity :BaseActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, FgVp2TestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 顶部tab */
    private val TOP_TAB_NAMES = arrayListOf("A", "B", "C")

    private val mAcTabLayout by bindView<TabLayout>(R.id.ac_tab_layout)
    private val mAcViewPager by bindView<ViewPager2>(R.id.ac_view_pager)

    override fun getLayoutId(): Int = R.layout.activity_fg_vp2_test

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.fg_lifecycle_vp)
        initViewPager()
    }

    private fun initViewPager() {
        val list = ArrayList<Fragment>()
        for (i in 0 until TOP_TAB_NAMES.size) {
            list.add(Vp2TopTestFragment.newInstance(TOP_TAB_NAMES[i]))
        }
        mAcViewPager.adapter = SimpleTabAdapter(this, list)
        mAcViewPager.offscreenPageLimit = TOP_TAB_NAMES.size
        mAcViewPager.setCurrentItem(0, true)
        TabLayoutMediator(mAcTabLayout, mAcViewPager) { tab, position ->
            tab.text = TOP_TAB_NAMES[position]
        }.attach()
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}