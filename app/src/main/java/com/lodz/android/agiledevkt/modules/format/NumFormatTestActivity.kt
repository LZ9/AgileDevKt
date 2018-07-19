package com.lodz.android.agiledevkt.modules.format

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.anko.AnkoNumFormat
import com.lodz.android.corekt.anko.format

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

    /** 按钮1 */
    @BindView(R.id.one_btn)
    lateinit var mOneBtn: TextView
    /** 按钮2 */
    @BindView(R.id.two_btn)
    lateinit var mTwoBtn: TextView
    /** 按钮3 */
    @BindView(R.id.three_btn)
    lateinit var mThreeBtn: TextView

    /** 结果 */
    @BindView(R.id.result_tv)
    lateinit var mResultTv: TextView

    override fun getLayoutId() = R.layout.activity_num_format_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun clickBackBtn() {
        super.clickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        mOneBtn.setOnClickListener {
            printFormat(mOneBtn.text.toString())
        }

        mTwoBtn.setOnClickListener {
            printFormat(mTwoBtn.text.toString())
        }

        mThreeBtn.setOnClickListener {
            printFormat(mThreeBtn.text.toString())
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

        mResultTv.text = result
    }
}