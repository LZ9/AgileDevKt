package com.lodz.android.agiledevkt.modules.hole

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.pandora.base.activity.BaseActivity

/**
 * 子控件透明布局测试类
 * @author zhouL
 * @date 2020/11/5
 */
class HoleTestActivity :BaseActivity(){

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, HoleTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_hole_test

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
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