package com.lodz.android.agiledevkt.modules.fglifecycle.vp2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityFgVp2TestBinding
import com.lodz.android.corekt.anko.setupViewPager
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
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

    private val mBinding: ActivityFgVp2TestBinding by bindingLayout(ActivityFgVp2TestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.fg_lifecycle_vp2)
        initViewPager()
    }

    private fun initViewPager() {
        val list = ArrayList<Fragment>()
        for (i in 0 until TOP_TAB_NAMES.size) {
            list.add(Vp2TopTestFragment.newInstance(TOP_TAB_NAMES[i]))
        }
        mBinding.acViewPager.adapter = SimpleTabAdapter(this, list)
        mBinding.acViewPager.offscreenPageLimit = TOP_TAB_NAMES.size
        mBinding.acViewPager.setCurrentItem(0, true)
        mBinding.acTabLayout.setupViewPager(mBinding.acViewPager, TOP_TAB_NAMES)
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