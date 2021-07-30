package com.lodz.android.agiledevkt.modules.dialogfragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityDialogFragmentBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * DialogFragment测试类
 * Created by zhouL on 2018/12/13.
 */
class DialogFragmentActivity : BaseActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, DialogFragmentActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityDialogFragmentBinding by bindingLayout(ActivityDialogFragmentBinding::inflate)

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
        mBinding.normalDfBtn.setOnClickListener {
            NormalDialogFragment().show(supportFragmentManager, "NormalDialogFragment")
        }

        mBinding.centerDfBtn.setOnClickListener {
            CenterDialogFragment().show(supportFragmentManager, "CenterDialogFragment")
        }

        mBinding.leftDfBtn.setOnClickListener {
            LeftDialogFragment().show(supportFragmentManager, "LeftDialogFragment")
        }

        mBinding.rightDfBtn.setOnClickListener {
            RightDialogFragment().show(supportFragmentManager, "RightDialogFragment")
        }

        mBinding.bottomDfBtn.setOnClickListener {
            BottomDialogFragment().show(supportFragmentManager, "BottomDialogFragment")
        }

        mBinding.topDfBtn.setOnClickListener {
            TopDialogFragment().show(supportFragmentManager, "TopDialogFragment")
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}