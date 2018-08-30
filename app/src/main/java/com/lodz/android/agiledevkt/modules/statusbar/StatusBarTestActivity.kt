package com.lodz.android.agiledevkt.modules.statusbar

import android.os.Bundle
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.base.activity.AbsActivity

/**
 * 状态栏透明颜色测试类
 * Created by zhouL on 2018/8/30.
 */
class StatusBarTestActivity :AbsActivity(){


    override fun getAbsLayoutId() = R.layout.activity_statusbar_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
    }

}