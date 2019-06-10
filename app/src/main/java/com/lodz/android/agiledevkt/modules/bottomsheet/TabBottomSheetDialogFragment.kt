package com.lodz.android.agiledevkt.modules.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.dialogfragment.TestFragment
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.pandora.widget.bottomsheets.dialogfragment.BaseBottomSheetDialogFragment

/**
 * Tab页的BottomSheetDialogFragment
 * Created by zhouL on 2018/12/13.
 */
class TabBottomSheetDialogFragment : BaseBottomSheetDialogFragment() {

    private val TABS = arrayListOf(
            R.string.bottom_sheets_pikachu_pinarello,
            R.string.bottom_sheets_pikachu_trek,
            R.string.bottom_sheets_pikachu_colnago,
            R.string.bottom_sheets_pikachu_look,
            R.string.bottom_sheets_pikachu_specialized
    )

    /** TabLayout */
    private val mTabLayout by bindView<TabLayout>(R.id.tab_layout)
    /** ViewPager */
    private val mViewPager by bindView<ViewPager>(R.id.view_pager)

    override fun getLayoutId(): Int = R.layout.dialog_fragment_tab_sheet

    override fun findViews(view: View, savedInstanceState: Bundle?) {
        super.findViews(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        mViewPager.adapter = TabAdapter(requireContext(), childFragmentManager, TABS)
        mViewPager.offscreenPageLimit = TABS.size
        mViewPager.setCurrentItem(0, true)
        mTabLayout.setupWithViewPager(mViewPager)
    }

    override fun onBehaviorInit(behavior: BottomSheetBehavior<*>) {
        behavior.peekHeight = dp2px(250)
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dialog.cancel()
                }
                setDim(if (newState == BottomSheetBehavior.STATE_EXPANDED) 0f else 0.6f)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private class TabAdapter(val context: Context, fm: FragmentManager, val tabs: ArrayList<Int>) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment = TestFragment.newInstance(context.getString(tabs[position]))

        override fun getCount(): Int = tabs.size

        override fun getPageTitle(position: Int): CharSequence? = context.getString(tabs[position])

    }
}