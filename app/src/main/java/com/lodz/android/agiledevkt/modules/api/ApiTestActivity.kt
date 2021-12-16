package com.lodz.android.agiledevkt.modules.api

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityApiTestBinding
import com.lodz.android.agiledevkt.modules.api.coroutines.ApiCoroutinesTestActivity
import com.lodz.android.agiledevkt.modules.api.rx.ApiRxTestActivity
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 接口测试类
 * @author zhouL
 * @date 2019/3/22
 */
class ApiTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ApiTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityApiTestBinding by bindingLayout(ActivityApiTestBinding::inflate)

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
        mBinding.rxBtn.setOnClickListener {
            ApiRxTestActivity.start(getContext())
        }

        mBinding.coroutinesBtn.setOnClickListener {
            ApiCoroutinesTestActivity.start(getContext())
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}