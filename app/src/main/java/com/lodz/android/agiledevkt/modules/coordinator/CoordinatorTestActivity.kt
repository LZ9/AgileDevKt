package com.lodz.android.agiledevkt.modules.coordinator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.base.activity.AbsActivity

/**
 * CoordinatorLayout测试类
 * Created by zhouL on 2018/9/11.
 */
class CoordinatorTestActivity :AbsActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, CoordinatorTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getAbsLayoutId() = R.layout.activity_coordinator_test

    override fun findViews(savedInstanceState: Bundle?) {


    }
}