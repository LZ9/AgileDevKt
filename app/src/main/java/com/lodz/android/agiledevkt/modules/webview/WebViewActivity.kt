package com.lodz.android.agiledevkt.modules.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.webview.simple.SimpleWebViewActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity

/**
 * WebView测试类
 * @author zhouL
 * @date 2019/5/30
 */
class WebViewActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, WebViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 加载控件的WebView */
    private val mPgBtn by bindView<MaterialButton>(R.id.pg_btn)
    /** JS交互的WebView */
    private val mJsBtn by bindView<MaterialButton>(R.id.js_btn)
    /** 简单配置的WebView */
    private val mFbBtn by bindView<MaterialButton>(R.id.simple_btn)

    override fun getLayoutId(): Int = R.layout.activity_webview

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mPgBtn.setOnClickListener {
            PgWebViewActivity.start(getContext())
        }

        mJsBtn.setOnClickListener {
            JsWebViewActivity.start(getContext())
        }

        mFbBtn.setOnClickListener {
            SimpleWebViewActivity.start(getContext())
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}