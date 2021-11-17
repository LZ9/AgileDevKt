package com.lodz.android.agiledevkt.modules.aop.checklogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityAopLoginBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.append
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 登录页
 * @author zhouL
 * @date 2021/11/11
 */
class AopLoginActivity :BaseActivity(){
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AopLoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityAopLoginBinding by bindingLayout(ActivityAopLoginBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(R.string.aop_title_login)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        mBinding.loginBtn.setOnClickListener {
            LoginHelper.isLogin = true
            finish()
        }

    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}