package com.lodz.android.corekt.anko

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * TabLayout扩展类
 * @author zhouL
 * @date 2022/10/12
 */

/** 设置ViewPager2[vp]，标题列表[titleList] */
fun TabLayout.setupViewPager(vp: ViewPager2, titleList: ArrayList<String>) {
    TabLayoutMediator(this, vp) { tab, position ->
        tab.text = titleList[position]
    }.attach()
}

/** 设置ViewPager2[vp]，标题数组[titleArray] */
fun TabLayout.setupViewPager(vp: ViewPager2, titleArray: Array<String>) {
    setupViewPager(vp, titleArray.toArrayList())
}

/** 设置ViewPager2[vp]，标题资源ID数组[titleResIdArray] */
fun TabLayout.setupViewPager(vp: ViewPager2, titleResIdArray: IntArray) {
    val titleList = ArrayList<String>()
    for (strResId in titleResIdArray) {
        titleList.add(vp.context.getString(strResId))
    }
    setupViewPager(vp, titleList)
}