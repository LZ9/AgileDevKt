package com.lodz.android.agiledevkt.modules.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.mvp.abs.activity.MvpTestAbsActivity
import com.lodz.android.agiledevkt.modules.mvp.base.activity.MvpTestBaseActivity
import com.lodz.android.agiledevkt.modules.mvp.refresh.activity.MvpTestRefreshActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity

/**
 * MVP模式测试类
 * Created by zhouL on 2018/11/19.
 */
class MvpDemoActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MvpDemoActivity::class.java)
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

    override fun getLayoutId(): Int = R.layout.activity_mvp_demo

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
            MvpTestAbsActivity.start(getContext())
        }

        // 带基础状态控件Activity
        mBaseBtn.setOnClickListener {
            MvpTestBaseActivity.start(getContext())
        }

        // 带基础状态控件和下来刷新控件Activity
        mRefreshBtn.setOnClickListener {
            MvpTestRefreshActivity.start(getContext())
        }

        // 带基础状态控件、中部刷新控件和顶部/底部扩展Activity
        mSandwichBtn.setOnClickListener {
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