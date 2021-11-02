package com.lodz.android.agiledevkt.modules.extras

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityExtrasTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.intentExtrasNoNull
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 参数传递测试类
 * @author zhouL
 * @date 2021/11/1
 */
class ExtrasTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, ExtrasTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityExtrasTestBinding by bindingLayout(ActivityExtrasTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    private val mTitle by intentExtrasNoNull(MainActivity.EXTRA_TITLE_NAME, "")

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(mTitle)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.toJumpBtn.setOnClickListener {
            ExtrasToActivity.start(getContext())
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}