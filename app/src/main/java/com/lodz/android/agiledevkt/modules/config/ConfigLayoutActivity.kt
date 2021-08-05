package com.lodz.android.agiledevkt.modules.config

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityConfigLayoutBinding
import com.lodz.android.agiledevkt.databinding.ActivityMainBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.base.ErrorLayout
import com.lodz.android.pandora.widget.base.LoadingLayout
import com.lodz.android.pandora.widget.base.NoDataLayout
import com.lodz.android.pandora.widget.base.TitleBarLayout
import com.lodz.android.pandora.widget.custom.MmsTabLayout

/**
 * 基础控件配置
 * @author zhouL
 * @date 2019/5/27
 */
class ConfigLayoutActivity : AbsActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ConfigLayoutActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityConfigLayoutBinding by bindingLayout(ActivityConfigLayoutBinding::inflate)

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        StatusBarUtil.setColor(window, getColorCompat(R.color.color_ea413c))
        mBinding.titleBarLayout.setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initTitleBar()
    }

    private fun initTitleBar() {
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText(R.string.config_loading), true)
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText(R.string.config_no_data))
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText(R.string.config_fail))
    }

    override fun setListeners() {
        super.setListeners()

        mBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab == null) {
                    return
                }
                when (tab.position) {
                    0 -> showLoading()
                    1 -> showNoData()
                    2 -> showError()
                    else -> showLoading()
                }
            }
        })

        // 标题栏返回按钮
        mBinding.titleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        // 标题栏右侧自定义按钮
        mBinding.titleBarLayout.getExpandView().setOnClickListener {
            mBinding.tabLayout.getTabAt(1)?.select()
        }

        mBinding.errorLayout.setReloadListener {
            mBinding.tabLayout.getTabAt(0)?.select()
        }
    }

    private fun showLoading() {
        mBinding.loadingLayout.visibility = View.VISIBLE
        mBinding.noDataLayout.visibility = View.GONE
        mBinding.errorLayout.visibility = View.GONE
    }

    private fun showNoData() {
        mBinding.loadingLayout.visibility = View.GONE
        mBinding.noDataLayout.visibility = View.VISIBLE
        mBinding.errorLayout.visibility = View.GONE
    }

    private fun showError() {
        mBinding.loadingLayout.visibility = View.GONE
        mBinding.noDataLayout.visibility = View.GONE
        mBinding.errorLayout.visibility = View.VISIBLE
    }

    override fun initData() {
        super.initData()
        showLoading()
    }
}