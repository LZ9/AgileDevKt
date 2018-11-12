package com.lodz.android.componentkt.base.activity

import android.os.Bundle
import com.lodz.android.componentkt.R

/**
 * 基类Activity（带基础状态控件和下来刷新控件）
 * Created by zhouL on 2018/11/12.
 */
class BaseRefreshActivity :AbsActivity(){



    override fun getAbsLayoutId(): Int = R.layout.component_activity_base_refresh

    override fun findViews(savedInstanceState: Bundle?) {


    }
}