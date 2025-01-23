package com.lodz.android.agiledevkt.modules.viewbinding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lodz.android.agiledevkt.databinding.ActivityViewBindingTestBinding
import com.lodz.android.corekt.anko.setupViewPager
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.vp2.SimpleVp2Adapter

/**
 * ViewBinding测试类
 * @author zhouL
 * @date 2021/7/19
 */
class ViewBindingTestActivity : AbsActivity() {

    /** tab数据 */
    private val TOP_TAB_NAMES = arrayListOf("A", "B", "C")

    private val mBinding: ActivityViewBindingTestBinding by bindingLayout(ActivityViewBindingTestBinding::inflate)

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        initViewPager()
    }

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun setListeners() {
        super.setListeners()
        mBinding.dialogBtn.setOnClickListener {
            val dialog = ViewBindingDialog(getContext())
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
        mBinding.viewPager.adapter = SimpleVp2Adapter(this, list)
        mBinding.viewPager.offscreenPageLimit = TOP_TAB_NAMES.size
        mBinding.viewPager.setCurrentItem(0, true)
        mBinding.tabLayout.setupViewPager(mBinding.viewPager, TOP_TAB_NAMES)
    }

    /** 显示动画的PopupWindow */
    private fun showViewBindingPopupWindow(view: View) {
        val popupWindow = ViewBindingPopupWindow(getContext())
        popupWindow.create()
        popupWindow.getPopup().showAsDropDown(view, 100, 20)
    }
}