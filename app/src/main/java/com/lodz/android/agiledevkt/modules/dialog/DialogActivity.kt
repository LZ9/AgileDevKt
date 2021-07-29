package com.lodz.android.agiledevkt.modules.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityDialogBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayoutLazy

/**
 * Dialog测试
 * Created by zhouL on 2018/11/1.
 */
class DialogActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, DialogActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityDialogBinding by bindingLayoutLazy(ActivityDialogBinding::inflate)

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

        // 普通弹框
        mBinding.normalBtn.setOnClickListener {
            NormalDialog(getContext()).show()
        }

        // 中间弹框
        mBinding.centerBtn.setOnClickListener {
            CenterDialog(getContext()).show()
        }

        // 左侧弹框
        mBinding.leftBtn.setOnClickListener {
            LeftDialog(getContext()).show()
        }

        // 右侧弹框
        mBinding.rightBtn.setOnClickListener {
            RightDialog(getContext()).show()
        }

        // 底部弹框
        mBinding.bottomBtn.setOnClickListener {
            BottomDialog(getContext()).show()
        }

        // 顶部弹框
        mBinding.topBtn.setOnClickListener {
            TopDialog(getContext()).show()
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}