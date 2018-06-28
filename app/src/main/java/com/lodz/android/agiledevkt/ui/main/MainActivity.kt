package com.lodz.android.agiledevkt.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.base.activity.BaseActivity

class MainActivity : BaseActivity() {

    companion object {
        /** 通过上下文[context]启动Activity */
        fun start(context: Context){
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_main

    override fun findViews(savedInstanceState: Bundle?) {



    }



}
