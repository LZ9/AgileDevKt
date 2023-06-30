package com.lodz.android.pandora.widget.vp2

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.lang.IllegalArgumentException

/**
 * 简单的ViewPager适配器
 * @author zhouL
 * @date 2020/1/2
 */
class SimpleVpAdapter : FragmentPagerAdapter {

    private val mFragments: List<Fragment>
    private val mTitles: List<String>

    constructor(fm: FragmentManager, fragments: List<Fragment>, titles: List<String>) : super(fm) {
        if (fragments.size != titles.size) {
            throw IllegalArgumentException("fragment size does not match title size")
        }
        this.mFragments = fragments
        this.mTitles = titles
    }

    constructor(fm: FragmentManager, behavior: Int, fragments: List<Fragment>, titles: List<String>) : super(fm, behavior) {
        if (fragments.size != titles.size) {
            throw IllegalArgumentException("fragment size does not match title size")
        }
        this.mFragments = fragments
        this.mTitles = titles
    }

    constructor(
        fm: FragmentManager, fragments: List<Fragment>,
        context: Context,
        titleResIds: IntArray
    ) : super(fm) {
        if (fragments.size != titleResIds.size) {
            throw IllegalArgumentException("fragment size does not match title size")
        }
        this.mFragments = fragments
        val list = ArrayList<String>()
        for (resId in titleResIds) {
            list.add(context.getString(resId))
        }
        this.mTitles = list
    }

    constructor(
        fm: FragmentManager,
        behavior: Int,
        fragments: List<Fragment>,
        context: Context,
        titleResIds: IntArray
    ) : super(fm, behavior) {
        if (fragments.size != titleResIds.size) {
            throw IllegalArgumentException("fragment size does not match title size")
        }
        this.mFragments = fragments
        val list = ArrayList<String>()
        for (resId in titleResIds) {
            list.add(context.getString(resId))
        }
        this.mTitles = list
    }

    override fun getCount(): Int = mFragments.size

    override fun getItem(position: Int): Fragment = mFragments[position]

    override fun getPageTitle(position: Int): CharSequence = mTitles[position]
}