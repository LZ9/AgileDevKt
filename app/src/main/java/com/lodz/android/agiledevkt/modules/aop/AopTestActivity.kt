package com.lodz.android.agiledevkt.modules.aop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityAopTestBinding
import com.lodz.android.agiledevkt.modules.aop.checklogin.AopAttentionActivity
import com.lodz.android.agiledevkt.modules.aop.checklogin.LoginHelper
import com.lodz.android.agiledevkt.modules.aop.checklogin.aspect.CheckLogin
import com.lodz.android.agiledevkt.modules.aop.fastclick.FastClickLimit
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.utils.DateUtils
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
            goAttentionActivity()
        }

        mBinding.cleanBtn.setOnClickListener {
            mBinding.logTv.text = ""
        }

        mBinding.fastClickAnnotationBtn.setOnClickListener @FastClickLimit(2000) {
            addLog(DateUtils.getCurrentFormatString(DateUtils.TYPE_10).append("：FastClick Annotation"))
        }

        mBinding.fastClickObjectBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                addLog(DateUtils.getCurrentFormatString(DateUtils.TYPE_10).append("：FastClick Object"))
            }
        })

        mBinding.fastClickLambdaBtn.setOnClickListener {
            addLog(DateUtils.getCurrentFormatString(DateUtils.TYPE_10).append("：FastClick Lambda"))
        }
    }

    @CheckLogin
    private fun goAttentionActivity() {
        AopAttentionActivity.start(getContext())
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    override fun onResume() {
        super.onResume()
        mBinding.loginStatusTv.text = getString(R.string.aop_login_status).append(LoginHelper.isLogin)
    }

    private fun addLog(log: String) {
        if (log.isEmpty()) {
            return
        }
        mBinding.logTv.text = log.append("\n").append(mBinding.logTv.text)
    }
}