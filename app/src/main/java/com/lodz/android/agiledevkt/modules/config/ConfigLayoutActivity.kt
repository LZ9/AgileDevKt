package com.lodz.android.agiledevkt.modules.config

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.pandora.base.activity.AbsActivity
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

    /** 标题栏 */
    private val mTitleBarLayout by bindView<TitleBarLayout>(R.id.title_bar_layout)
    /** TabLayout */
    private val mTabLayout by bindView<MmsTabLayout>(R.id.tab_layout)
    /** 加载页 */
    private val mLoadingLayout by bindView<LoadingLayout>(R.id.loading_layout)
    /** 无数据页 */
    private val mNoDataLayout by bindView<NoDataLayout>(R.id.no_data_layout)
    /** 异常页 */
    private val mErrorLayout by bindView<ErrorLayout>(R.id.error_layout)

    override fun getAbsLayoutId(): Int = R.layout.activity_config_layout

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        StatusBarUtil.setColor(window, getColorCompat(R.color.color_ea413c))
        mTitleBarLayout.setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
        initTitleBar()
    }

    private fun initTitleBar() {
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.config_loading), true)
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.config_no_data))
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.config_fail))
    }

    override fun setListeners() {
        super.setListeners()

        mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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

        mTitleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        mTitleBarLayout.getExpandView().setOnClickListener {
            mTabLayout.getTabAt(1)?.select()
        }

        mErrorLayout.setReloadListener {
            mTabLayout.getTabAt(0)?.select()
        }
    }

    private fun showLoading() {
        mLoadingLayout.visibility = View.VISIBLE
        mNoDataLayout.visibility = View.GONE
        mErrorLayout.visibility = View.GONE
    }

    private fun showNoData() {
        mLoadingLayout.visibility = View.GONE
        mNoDataLayout.visibility = View.VISIBLE
        mErrorLayout.visibility = View.GONE
    }

    private fun showError() {
        mLoadingLayout.visibility = View.GONE
        mNoDataLayout.visibility = View.GONE
        mErrorLayout.visibility = View.VISIBLE
    }

    override fun initData() {
        super.initData()
        showLoading()
    }
}