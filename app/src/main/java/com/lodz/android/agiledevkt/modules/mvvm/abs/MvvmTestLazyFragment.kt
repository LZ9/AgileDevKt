package com.lodz.android.agiledevkt.modules.mvvm.abs

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.mvvm.base.fragment.LazyVmFragment

/**
 * MVVM基础Fragment
 * @author zhouL
 * @date 2019/12/6
 */
class MvvmTestLazyFragment :LazyVmFragment<MvvmTestAbsViewModel>(){

    companion object {
        fun newInstance(): MvvmTestLazyFragment = MvvmTestLazyFragment()
    }

    /** 结果 */
    private val mResult by bindView<TextView>(R.id.result)
    /** 获取成功数据按钮 */
    private val mGetSuccessResultBtn by bindView<Button>(R.id.get_success_reuslt_btn)
    /** 获取失败数据按钮 */
    private val mGetFailResultBtn by bindView<Button>(R.id.get_fail_reuslt_btn)

    override fun getAbsLayoutId(): Int = R.layout.activity_mvvm_test

    override fun createViewModel(): Class<MvvmTestAbsViewModel> = MvvmTestAbsViewModel::class.java

    override fun setListeners(view: View) {
        super.setListeners(view)
        mGetSuccessResultBtn.setOnClickListener {
            getViewModel().getResult(getContext(), true)
        }

        mGetFailResultBtn.setOnClickListener {
            getViewModel().getResult(getContext(), false)
        }

        getViewModel().mResultText.observe(this, Observer { value ->
            mResult.text = value
        })
    }
}