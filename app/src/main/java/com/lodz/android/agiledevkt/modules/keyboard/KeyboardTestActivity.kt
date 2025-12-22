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
import com.lodz.android.pandora.widget.keyboard.collect.checkInputMethodOnFocusChange
import com.lodz.android.pandora.widget.keyboard.collect.setOnClickFinishListener

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
        mBinding.idcardCltedt.getEditText().checkInputMethodOnFocusChange()
        mBinding.carplateCltedt.getEditText().checkInputMethodOnFocusChange()
        mBinding.commonCertCltedt.getEditText().checkInputMethodOnFocusChange()
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        mBinding.idcardCltedt.getEditText().setOnClickFinishListener {
            toastShort(it)
        }

        mBinding.carplateCltedt.getEditText().setOnClickFinishListener {
            toastShort(it)
        }

        mBinding.commonCertCltedt.getEditText().setOnClickFinishListener {
            toastShort(it)
        }

        // 自定义键盘测试弹窗
        mBinding.showDialogBtn.setOnClickListener {
            showKeyboardTestDialog()
        }
    }

    /** 显示自定义键盘测试弹窗 */
    private fun showKeyboardTestDialog() {
        val dialog = KeyboardTestDialog(this)
        dialog.show()
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}