package com.lodz.android.agiledevkt.modules.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityJsWebviewBinding
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * JS交互的WebView测试类
 * @author zhouL
 * @date 2019/5/31
 */
class JsWebViewActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, JsWebViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    // TODO: 2021/8/11  待开发

    private val mBinding: ActivityJsWebviewBinding by bindingLayout(ActivityJsWebviewBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.wv_js_title)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}