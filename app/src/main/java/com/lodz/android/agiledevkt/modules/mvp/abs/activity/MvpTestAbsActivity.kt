package com.lodz.android.agiledevkt.modules.mvp.abs.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvp.abs.MvpTestAbsPresenter
import com.lodz.android.agiledevkt.modules.mvp.abs.MvpTestAbsViewContract
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.mvp.base.activity.MvpAbsActivity

/**
 * MVP基础Activity
 * Created by zhouL on 2018/11/19.
 */
class MvpTestAbsActivity : MvpAbsActivity<MvpTestAbsPresenter, MvpTestAbsViewContract>(), MvpTestAbsViewContract {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MvpTestAbsActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)

    /** 获取成功数据按钮 */
    private val mGetSuccessResultBtn by bindView<Button>(R.id.get_success_reuslt_btn)

    /** 获取失败数据按钮 */
    private val mGetFailResultBtn by bindView<Button>(R.id.get_fail_reuslt_btn)

    override fun createMainPresenter(): MvpTestAbsPresenter = MvpTestAbsPresenter()

    override fun getAbsLayoutId(): Int = R.layout.activity_mvp_test

    override fun setListeners() {
        super.setListeners()
        mGetSuccessResultBtn.setOnClickListener {
            getPresenterContract()?.getResult(true)
        }

        mGetFailResultBtn.setOnClickListener {
            getPresenterContract()?.getResult(false)
        }

    }

    override fun showResult() {
        mResultTv.visibility = View.VISIBLE
    }

    override fun setResult(result: String) {
        mResultTv.text = result
    }
}