package com.lodz.android.agiledevkt.modules.result

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivitySecondBinding
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 跳转第二个Activity
 * @author zhouL
 * @date 2022/4/11
 */
class SecondActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SecondActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivitySecondBinding by bindingLayout(ActivitySecondBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(R.string.arc_test_second_title)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.resultBtn.setOnClickListener {
            val str = mBinding.inputEdit.text.toString()
            if (str.isEmpty()) {
                toastShort(R.string.arc_input_hint)
                return@setOnClickListener
            }
            val intent = Intent()
            intent.putExtra(Activity.RESULT_OK.toString(), str)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}