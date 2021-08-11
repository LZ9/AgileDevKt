package com.lodz.android.agiledevkt.modules.format

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityNumFormatTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.*
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 数字格式化测试类
 * Created by zhouL on 2018/7/18.
 */
class NumFormatTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, NumFormatTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityNumFormatTestBinding by bindingLayout(ActivityNumFormatTestBinding::inflate)

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

        // 按钮1
        mBinding.oneBtn.setOnClickListener {
            printFormat(mBinding.oneBtn.text.toString())
        }

        // 按钮2
        mBinding.twoBtn.setOnClickListener {
            printFormat(mBinding.twoBtn.text.toString())
        }

        // 按钮3
        mBinding.threeBtn.setOnClickListener {
            printFormat(mBinding.threeBtn.text.toString())
        }

        // 转换按钮
        mBinding.transformationBtn.setOnClickListener {
            val str = mBinding.numEdit.text.toString()
            if (str.isEmpty()){
                toastShort(R.string.format_num_hint)
                return@setOnClickListener
            }
            mBinding.resultTv.text = str.toInt().toChinese()
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    private fun printFormat(numStr: String) {
        val num = numStr.toDouble()
        val result = StringBuilder()
        result.append(num.format(AnkoNumFormat.TYPE_ONE_DECIMAL)).append("\n")
                .append(num.format()).append("\n")
                .append(num.format(AnkoNumFormat.TYPE_THREE_DECIMAL)).append("\n")
                .append(num.format(AnkoNumFormat.TYPE_FOUR_DECIMAL)).append("\n")
                .append(num.format(AnkoNumFormat.TYPE_FIVE_DECIMAL)).append("\n")

        mBinding.resultTv.text = result
    }
}