package com.lodz.android.agiledevkt.modules.coordinator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityCoordinatorTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * CoordinatorLayout测试类
 * Created by zhouL on 2018/9/27.
 */
class CoordinatorTestActivity : BaseActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, CoordinatorTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityCoordinatorTestBinding by bindingLayout(ActivityCoordinatorTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 位移按钮
        mBinding.translationBtn.setOnClickListener {
            CoorTranslationActivity.start(getContext())
        }
        // 状态栏按钮
        mBinding.statusBarBtn.setOnClickListener {
            CoorStatusBarTestActivity.start(getContext(), getString(R.string.status_bar_test_coordinator))
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}