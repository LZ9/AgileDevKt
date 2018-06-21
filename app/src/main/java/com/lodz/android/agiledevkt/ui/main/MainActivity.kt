package com.lodz.android.agiledevkt.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.log.PrintLog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        PrintLog.setPrint(false)
    }
}
