package com.lodz.android.agiledevkt.modules.aop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityAopTestBinding
import com.lodz.android.agiledevkt.modules.aop.checklogin.AopAttentionActivity
import com.lodz.android.agiledevkt.modules.aop.checklogin.AopLoginActivity
import com.lodz.android.agiledevkt.modules.aop.checklogin.LoginHelper
import com.lodz.android.agiledevkt.modules.aop.checklogin.aspect.CheckLogin
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 切面编程测试类
 * @author zhouL
 * @date 2021/11/11
 */
class AopTestActivity :BaseActivity(){

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AopTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityAopTestBinding by bindingLayout(ActivityAopTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        mBinding.loginProxyBtn.setOnClickListener {
//            if (LoginHelper.isUserLogin()) {
//                AopAttentionActivity.start(getContext())
//            } else {
//                AopLoginActivity.start(getContext())
//            }
            goAttentionActivity()
        }

        mBinding.fastClickBtn.setOnClickListener {

        }
    }

    @CheckLogin
    fun goAttentionActivity() {
        AopAttentionActivity.start(getContext())
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}