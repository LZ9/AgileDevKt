package com.lodz.android.agiledevkt.modules.coordinator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity

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

    /** 位移按钮 */
    @BindView(R.id.translation_btn)
    lateinit var mTranslationBtn: Button
    /** 状态栏按钮 */
    @BindView(R.id.status_bar_btn)
    lateinit var mStatusBarBtn: Button


    override fun getLayoutId() = R.layout.activity_coordinator_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun clickBackBtn() {
        super.clickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        mTranslationBtn.setOnClickListener {
            CoorTranslationActivity.start(getContext())
        }

        mStatusBarBtn.setOnClickListener {
            CoorStatusBarTestActivity.start(getContext(), getString(R.string.status_bar_test_coordinator))
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}