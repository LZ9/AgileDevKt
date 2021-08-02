package com.lodz.android.agiledevkt.modules.fglifecycle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityFgLifecycleBinding
import com.lodz.android.agiledevkt.modules.fglifecycle.vp.FgVpTestActivity
import com.lodz.android.agiledevkt.modules.fglifecycle.vp2.FgVp2TestActivity
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * Fragment生命周期测试
 * @author zhouL
 * @date 2020/1/21
 */
class FragmentLifecycleActivity :BaseActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, FragmentLifecycleActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityFgLifecycleBinding by bindingLayout(ActivityFgLifecycleBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.vpBtn.setOnClickListener {
            FgVpTestActivity.start(getContext())
        }

        mBinding.vp2Btn.setOnClickListener {
            FgVp2TestActivity.start(getContext())
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}