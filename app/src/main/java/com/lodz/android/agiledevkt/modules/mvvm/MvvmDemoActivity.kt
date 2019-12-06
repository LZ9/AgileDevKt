package com.lodz.android.agiledevkt.modules.mvvm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.mvvm.abs.MvvmTestAbsActivity
import com.lodz.android.agiledevkt.modules.mvvm.base.MvvmTestBaseActivity
import com.lodz.android.agiledevkt.modules.mvvm.refresh.MvvmTestRefreshActivity
import com.lodz.android.agiledevkt.modules.mvvm.sandwich.MvvmTestSandwichActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity

/**
 * MVVM模式测试类
 * @author zhouL
 * @date 2019/11/29
 */
class MvvmDemoActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MvvmDemoActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 基础Activity */
    private val mAbsBtn by bindView<Button>(R.id.abs_btn)
    /** 带基础状态控件Activity */
    private val mBaseBtn by bindView<Button>(R.id.base_btn)
    /** 带基础状态控件和下来刷新控件Activity */
    private val mRefreshBtn by bindView<Button>(R.id.refresh_btn)
    /** 带基础状态控件、中部刷新控件和顶部/底部扩展Activity */
    private val mSandwichBtn by bindView<Button>(R.id.sandwich_btn)
    /** Fragment用例 */
    private val mFragmentBtn by bindView<Button>(R.id.fragment_btn)

    override fun getLayoutId(): Int = R.layout.activity_mvvm_demo

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 基础Activity
        mAbsBtn.setOnClickListener {
            MvvmTestAbsActivity.start(getContext())
        }

        // 带基础状态控件Activity
        mBaseBtn.setOnClickListener {
            MvvmTestBaseActivity.start(getContext())
        }

        // 带基础状态控件和下来刷新控件Activity
        mRefreshBtn.setOnClickListener {
            MvvmTestRefreshActivity.start(getContext())
        }

        // 带基础状态控件、中部刷新控件和顶部/底部扩展Activity
        mSandwichBtn.setOnClickListener {
            MvvmTestSandwichActivity.start(getContext())
        }

        // Fragment用例
        mFragmentBtn.setOnClickListener {

        }
    }


    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}