package com.lodz.android.agiledevkt.modules.viewbinding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.lodz.android.agiledevkt.databinding.ActivityViewBindingTestBinding
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayoutLazy
import com.lodz.android.pandora.widget.vp2.SimpleTabAdapter

/**
 * ViewBinding测试类
 * @author zhouL
 * @date 2021/7/19
 */
class ViewBindingTestActivity : AbsActivity() {

    /** tab数据 */
    private val TOP_TAB_NAMES = arrayListOf("A", "B", "C")

    private val mBinding: ActivityViewBindingTestBinding by bindingLayoutLazy(ActivityViewBindingTestBinding::inflate)

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        initViewPager()
    }

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun setListeners() {
        super.setListeners()
        mBinding.dialogBtn.setOnClickListener {
            val dialog = ViewBindingDialog(this@ViewBindingTestActivity)
            dialog.show()
        }
        mBinding.popupBtn.setOnClickListener {
            showViewBindingPopupWindow(it)
        }
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

    /** 显示动画的PopupWindow */
    private fun showViewBindingPopupWindow(view: View) {
        val popupWindow = ViewBindingPopupWindow(this@ViewBindingTestActivity)
        popupWindow.create()
        popupWindow.getPopup().showAsDropDown(view, 100, 20)
    }
}