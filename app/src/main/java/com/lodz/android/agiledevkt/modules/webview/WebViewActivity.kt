package com.lodz.android.agiledevkt.modules.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityWebviewBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.webview.simple.SimpleWebViewActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

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

    private val mBinding: ActivityWebviewBinding by bindingLayout(ActivityWebviewBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        // 加载控件的WebView
        mBinding.pgBtn.setOnClickListener {
            PgWebViewActivity.start(getContext())
        }

        // JS交互的WebView
        mBinding.jsBtn.setOnClickListener {
            JsWebViewActivity.start(getContext())
        }

        // 简单配置的WebView
        mBinding.simpleBtn.setOnClickListener {
            SimpleWebViewActivity.start(getContext())
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}