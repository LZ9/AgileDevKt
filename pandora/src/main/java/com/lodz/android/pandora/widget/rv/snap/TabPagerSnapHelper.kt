package com.lodz.android.pandora.widget.rv.snap

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

/**
 * 支持TabLayout联动的PagerSnapHelper
 * Created by zhouL on 2018/11/22.
 */
class TabPagerSnapHelper(mStartPosition: Int) : ViewPagerSnapHelper(mStartPosition) {

    private var mPdrRecyclerView: RecyclerView? = null
    private var mPdrTabLayout: TabLayout? = null

    /** 是否滚动了RecyclerView  */
    private var isPdrScrollRv = false
    /** 是否选择了TabLayout  */
    private var isPdrSelectedTab = false


    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        super.attachToRecyclerView(recyclerView)
        if (recyclerView == null) {
            return
        }
        if (mPdrRecyclerView != null && mPdrRecyclerView == recyclerView) {//不再重复赋值
            return
        }
        mPdrRecyclerView = recyclerView
        setOnPageChangeListener { position ->
            if (isPdrSelectedTab) {// 如果是tablayout选择则不做处理
                isPdrSelectedTab = false
                return@setOnPageChangeListener
            }
            if (mPdrTabLayout != null) {
                linkage(mPdrTabLayout!!, position)
            }
        }
    }

    /** 处理联动逻辑 */
    private fun linkage(tabLayout: TabLayout, position: Int) {

        if (position < tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(position)
            if (tab != null) {
                isPdrScrollRv = true
                tab.select()
            }
        }
    }

    /** 与TabLayout联动 */
    fun setupWithTabLayout(tabLayout: TabLayout) {
        mPdrTabLayout = tabLayout
        mPdrTabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (isPdrScrollRv) {//RV滚动的tab设置不做处理
                    isPdrScrollRv = false
                    return
                }
                isPdrSelectedTab = true
                if (mPdrRecyclerView != null && tab != null) {
                    mPdrRecyclerView?.smoothScrollToPosition(tab.position)
                }
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
        })
    }
}