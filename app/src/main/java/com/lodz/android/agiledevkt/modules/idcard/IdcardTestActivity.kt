package com.lodz.android.agiledevkt.modules.idcard

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.base.activity.AbsActivity
import com.lodz.android.componentkt.widget.base.SearchTitleBarLayout
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.corekt.utils.IdCardUtils

/**
 * 身份证号码测试类
 * Created by zhouL on 2018/7/17.
 */
class IdcardTestActivity : AbsActivity() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, IdcardTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 搜索标题框 */
    @BindView(R.id.search_title_layout)
    lateinit var mSearchTitleBarLayout: SearchTitleBarLayout
    /** 结果 */
    @BindView(R.id.result)
    lateinit var mResultTv: TextView

    override fun getAbsLayoutId() = R.layout.activity_id_card_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
    }

    override fun setListeners() {
        super.setListeners()

        // 返回按钮
        mSearchTitleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        // 搜索按钮
        mSearchTitleBarLayout.setOnSearchClickListener {
            val idcard = mSearchTitleBarLayout.getInputText()
            if (idcard.isEmpty()){
                mResultTv.setTextColor(Color.RED)
                mResultTv.setText(R.string.idcard_empty_tips)
                return@setOnSearchClickListener
            }
            if (idcard.length != 18){
                mResultTv.setTextColor(Color.RED)
                mResultTv.setText(R.string.idcard_length_error)
                return@setOnSearchClickListener
            }
            checkIdcard(idcard)
        }
    }

    /** 校验身份证号[idcard] */
    private fun checkIdcard(idcard: String) {
        if (!IdCardUtils.validateIdCard(idcard)){
            mResultTv.setText(R.string.idcard_illegal)
            mResultTv.setTextColor(Color.RED)
            return
        }

        mResultTv.setTextColor(Color.BLACK)
        val result = StringBuilder()
        result.append(getString(R.string.idcard_check_success)).append("\n")
                .append(getString(R.string.idcard_province)).append(IdCardUtils.getProvince(idcard)).append("\n")
                .append(getString(R.string.idcard_sex)).append(IdCardUtils.getSexStr(idcard)).append("\n")
                .append(getString(R.string.idcard_birth)).append(IdCardUtils.getBirth(idcard, DateUtils.TYPE_6)).append("\n")
                .append(getString(R.string.idcard_year)).append(IdCardUtils.getYear(idcard)).append("\n")
                .append(getString(R.string.idcard_month)).append(IdCardUtils.getMonth(idcard)).append("\n")
                .append(getString(R.string.idcard_day)).append(IdCardUtils.getDay(idcard)).append("\n")
                .append(getString(R.string.idcard_age)).append(IdCardUtils.getAge(idcard)).append("\n")

        mResultTv.text = result
    }

}