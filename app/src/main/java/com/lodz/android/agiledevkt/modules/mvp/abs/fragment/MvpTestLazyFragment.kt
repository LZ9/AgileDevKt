package com.lodz.android.agiledevkt.modules.mvp.abs.fragment

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvp.abs.MvpTestAbsPresenter
import com.lodz.android.agiledevkt.modules.mvp.abs.MvpTestAbsViewContract
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.mvp.base.fragment.MvpLazyFragment

/**
 * MVP测试Fragment
 * @author zhouL
 * @date 2020/12/3
 */
class MvpTestLazyFragment : MvpLazyFragment<MvpTestAbsPresenter, MvpTestAbsViewContract>(), MvpTestAbsViewContract {

    companion object {
        fun newInstance(): MvpTestLazyFragment = MvpTestLazyFragment()
    }

    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)
    /** 获取成功数据按钮 */
    private val mGetSuccessResultBtn by bindView<Button>(R.id.get_success_reuslt_btn)
    /** 获取失败数据按钮 */
    private val mGetFailResultBtn by bindView<Button>(R.id.get_fail_reuslt_btn)

    override fun createMainPresenter(): MvpTestAbsPresenter = MvpTestAbsPresenter()

    override fun getAbsLayoutId(): Int = R.layout.activity_mvp_test

    override fun setListeners(view: View) {
        super.setListeners(view)
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