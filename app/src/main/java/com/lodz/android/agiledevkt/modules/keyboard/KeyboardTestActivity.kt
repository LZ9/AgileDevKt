package com.lodz.android.agiledevkt.modules.keyboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityKeyboardTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 自定义键盘测试类
 * @author zhouL
 * @date 2021/2/22
 */
class KeyboardTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, KeyboardTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityKeyboardTestBinding by bindingLayout(ActivityKeyboardTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        mBinding.idcardKeyboardView.initView(window, mBinding.idcardCltedt.getEditText())
        mBinding.carplateKeyboardView.initView(window, mBinding.carplateCltedt.getEditText())
        mBinding.commonCertKeyboardView.initView(window, mBinding.commonCertCltedt.getEditText())
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        // 身份证键盘
        mBinding.idcardKeyboardView.setOnClickFinishListener{
            mBinding.idcardKeyboardView.goneKeyboard()
            if (it.isNotEmpty()){
                toastShort(it)
            }
        }

        // 车牌键盘
        mBinding.carplateKeyboardView.setOnClickFinishListener{
            mBinding.carplateKeyboardView.goneKeyboard()
            if (it.isNotEmpty()){
                toastShort(it)
            }
        }

        // 全键盘
        mBinding.commonCertKeyboardView.setOnClickFinishListener{
            mBinding.commonCertKeyboardView.goneKeyboard()
            if (it.isNotEmpty()){
                toastShort(it)
            }
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}