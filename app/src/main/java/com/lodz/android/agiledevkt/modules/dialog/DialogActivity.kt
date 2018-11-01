package com.lodz.android.agiledevkt.modules.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity

/**
 * 弹框测试类
 * Created by zhouL on 2018/11/1.
 */
class DialogActivity :BaseActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, DialogActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_dialog

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
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