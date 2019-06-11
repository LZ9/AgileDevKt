package com.lodz.android.pandora.widget.rv.snap

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

/**
 * 支持TabLayout联动的PagerSnapHelper
 * Created by zhouL on 2018/11/22.
 */
class TabPagerSnapHelper(mStartPosition: Int) : ViewPagerSnapHelper(mStartPosition) {

    private var mRecyclerView: RecyclerView? = null
    private var mTabLayout: TabLayout? = null

    /** 是否滚动了RecyclerView  */
    private var isScrollRv = false
    /** 是否选择了TabLayout  */
    private var isSelectedTab = false


    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        super.attachToRecyclerView(recyclerView)
        if (recyclerView == null) {
            return
        }
        if (mRecyclerView != null && mRecyclerView == recyclerView) {//不再重复赋值
            return
        }
        mRecyclerView = recyclerView
        setOnPageChangeListener { position ->
            if (isSelectedTab) {// 如果是tablayout选择则不做处理
                isSelectedTab = false
                return@setOnPageChangeListener
            }
            if (mTabLayout != null) {
                linkage(mTabLayout!!, position)
            }
        }
    }

    /** 处理联动逻辑 */
    private fun linkage(tabLayout: TabLayout, position: Int) {

        if (position < tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(position)
            if (tab != null) {
                isScrollRv = true
                tab.select()
            }
        }
    }

    /** 与TabLayout联动 */
    fun setupWithTabLayout(tabLayout: TabLayout) {
        mTabLayout = tabLayout
        mTabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (isScrollRv) {//RV滚动的tab设置不做处理
                    isScrollRv = false
                    return
                }
                isSelectedTab = true
                if (mRecyclerView != null && tab != null) {
                    mRecyclerView?.smoothScrollToPosition(tab.position)
                }
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
        })
    }
}