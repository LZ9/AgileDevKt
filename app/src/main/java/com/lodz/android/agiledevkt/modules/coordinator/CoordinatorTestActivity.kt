package com.lodz.android.agiledevkt.modules.coordinator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity

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
    private val mTranslationBtn by bindView<Button>(R.id.translation_btn)
    /** 状态栏按钮 */
    private val mStatusBarBtn by bindView<Button>(R.id.status_bar_btn)

    override fun getLayoutId() = R.layout.activity_coordinator_test

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
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