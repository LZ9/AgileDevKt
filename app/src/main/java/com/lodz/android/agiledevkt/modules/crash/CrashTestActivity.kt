package com.lodz.android.agiledevkt.modules.crash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.splash.SplashActivity
import com.lodz.android.agiledevkt.utils.crash.CrashManager
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.utils.UiHandler

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
    @BindView(R.id.crash_btn)
    lateinit var mCrashBtn: TextView
    /** 崩溃提示语 */
    @BindView(R.id.crash_tips)
    lateinit var mCrashTips: TextView

    override fun getLayoutId() = R.layout.activity_crash_test

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
        mCrashBtn.setOnClickListener {
            mCrashTips.visibility = View.VISIBLE
            mCrashBtn.visibility = View.GONE
            UiHandler.postDelayed(Runnable {
                val case: String? = null
                case!!.toInt()
            }, 100)
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