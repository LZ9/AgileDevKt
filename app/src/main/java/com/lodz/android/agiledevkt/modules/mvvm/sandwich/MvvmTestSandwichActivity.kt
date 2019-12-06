package com.lodz.android.agiledevkt.modules.mvvm.sandwich

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.mvvm.base.activity.BaseSandwichVmActivity
import com.lodz.android.pandora.widget.base.TitleBarLayout
import kotlin.random.Random

/**
 * MVVM带基础状态控件、中部刷新控件和顶部/底部扩展的Activity
 * @author zhouL
 * @date 2019/12/6
 */
class MvvmTestSandwichActivity : BaseSandwichVmActivity<MvvmTestSandwichViewModel>() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, MvvmTestSandwichActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 结果 */
    private val mResult by bindView<TextView>(R.id.result)
    /** 获取成功数据按钮 */
    private val mGetSuccessResultBtn by bindView<Button>(R.id.get_success_reuslt_btn)
    /** 获取失败数据按钮 */
    private val mGetFailResultBtn by bindView<Button>(R.id.get_fail_reuslt_btn)
    /** 顶部标题栏 */
    private val mTitleBarLayout by bindView<TitleBarLayout>(R.id.title_bar_layout)

    override fun createViewModel(): Class<MvvmTestSandwichViewModel> = MvvmTestSandwichViewModel::class.java

    override fun getLayoutId(): Int = R.layout.activity_mvvm_test

    override fun getTopLayoutId(): Int = R.layout.view_mvvm_test_top

    override fun getBottomLayoutId(): Int = R.layout.view_mvvm_test_bottom

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        mTitleBarLayout.setTitleName(R.string.mvvm_demo_sandwich_title)
        setSwipeRefreshEnabled(true)
    }

    override fun onDataRefresh() {
        super.onDataRefresh()
        getViewModel().getRefreshData(Random.nextInt(9) % 2 == 0)
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        getViewModel().getResult(true)
    }

    override fun setListeners() {
        super.setListeners()

        mTitleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        mGetSuccessResultBtn.setOnClickListener {
            showStatusLoading()
            getViewModel().getResult(true)
        }

        mGetFailResultBtn.setOnClickListener {
            showStatusLoading()
            getViewModel().getResult(false)
        }

        getViewModel().mResultText.observe(this, Observer { value ->
            mResult.text = value
        })
    }

    override fun initData() {
        super.initData()
        showStatusNoData()
    }

}