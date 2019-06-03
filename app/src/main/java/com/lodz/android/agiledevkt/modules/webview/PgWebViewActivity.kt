package com.lodz.android.agiledevkt.modules.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.webview.PgWebView

/**
 * 带加载控件的WebView测试类
 * @author zhouL
 * @date 2019/5/30
 */
class PgWebViewActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PgWebViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val BAIDU_URL = "https://www.baidu.com/"

    /** 加载进度控件 */
    private val mPgWebView by bindView<PgWebView>(R.id.pg_webview)

    override fun getLayoutId(): Int = R.layout.activity_pg_webview

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.wv_pg_title)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
    }

    override fun initData() {
        super.initData()
        mPgWebView.loadUrl(BAIDU_URL)
        showStatusCompleted()
    }

    override fun finish() {
        super.finish()
        mPgWebView.release()
    }
}