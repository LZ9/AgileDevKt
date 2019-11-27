package com.lodz.android.agiledevkt.modules.crash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.splash.SplashActivity
import com.lodz.android.agiledevkt.utils.crash.CrashManager
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.runOnMainDelay
import com.lodz.android.pandora.base.activity.BaseActivity
import kotlinx.coroutines.GlobalScope

/**
 * 崩溃测试类
 * Created by zhouL on 2018/9/18.
 */
class CrashTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CrashTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 崩溃按钮 */
    private val mCrashBtn by bindView<TextView>(R.id.crash_btn)
    /** 崩溃提示语 */
    private val mCrashTips by bindView<TextView>(R.id.crash_tips)

    override fun getLayoutId() = R.layout.activity_crash_test

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mCrashBtn.setOnClickListener {
            mCrashTips.visibility = View.VISIBLE
            mCrashBtn.visibility = View.GONE
            GlobalScope.runOnMainDelay(100){
                val case: String? = null
                case!!.toInt()
            }
        }
    }

    override fun initData() {
        super.initData()
        configCrash()
        showStatusCompleted()
    }

    /** 配置崩溃管理 */
    private fun configCrash() {
        CrashManager.get()
                .setInterceptor(true)
                .setToastTips("还不快给卢姥爷上柱香")
//                .setSaveFolderPath(FileManager.getCrashFolderPath() + "test" + File.separator)
//                .setFileName(System.currentTimeMillis().toString() + ".log")
                .setLauncherClass(SplashActivity::class.java)
//                .setLogTag("Crash")
                .build()

    }
}