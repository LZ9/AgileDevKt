package com.lodz.android.agiledevkt.modules.fglifecycle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.fglifecycle.vp.FgVpTestActivity
import com.lodz.android.agiledevkt.modules.fglifecycle.vp2.FgVp2TestActivity
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity

/**
 * Fragment生命周期测试
 * @author zhouL
 * @date 2020/1/21
 */
class FragmentLifecycleActivity :BaseActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, FragmentLifecycleActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mVpBtn by bindView<MaterialButton>(R.id.vp_btn)
    private val mVp2Btn by bindView<MaterialButton>(R.id.vp2_btn)

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun getLayoutId(): Int = R.layout.activity_fg_lifecycle

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mVpBtn.setOnClickListener {
            FgVpTestActivity.start(getContext())
        }

        mVp2Btn.setOnClickListener {
            FgVp2TestActivity.start(getContext())
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}