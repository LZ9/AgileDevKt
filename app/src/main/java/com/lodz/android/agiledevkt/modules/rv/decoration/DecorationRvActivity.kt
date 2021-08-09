package com.lodz.android.agiledevkt.modules.rv.decoration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityDecorationMainBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * RV装饰器测试
 * Created by zhouL on 2018/12/5.
 */
class DecorationRvActivity : BaseActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, DecorationRvActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityDecorationMainBinding by bindingLayout(ActivityDecorationMainBinding::inflate)

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

        // 底部分割线
        mBinding.roundBottomBtn.setOnClickListener {
            DecorationTestActivity.start(getContext(), DecorationTestActivity.DECORATION_TYPE_ROUND_BOTTOM)
        }

        // 外围分割线
        mBinding.roundBtn.setOnClickListener {
            DecorationTestActivity.start(getContext(), DecorationTestActivity.DECORATION_TYPE_ROUND)
        }

        // 网格分割线
        mBinding.gridBtn.setOnClickListener {
            DecorationTestActivity.start(getContext(), DecorationTestActivity.DECORATION_TYPE_GRID)
        }

        // 分组标签
        mBinding.sectionBtn.setOnClickListener {
            DecorationTestActivity.start(getContext(), DecorationTestActivity.DECORATION_TYPE_SECTION)
        }

        // 固定数据的分组标签
        mBinding.sectionFixBtn.setOnClickListener {
            DecorationTestActivity.start(getContext(), DecorationTestActivity.DECORATION_TYPE_SECTION_FIX)
        }

        // 粘黏分组标签
        mBinding.stickySectionBtn.setOnClickListener {
            DecorationTestActivity.start(getContext(), DecorationTestActivity.DECORATION_TYPE_STICKY_SECTION)
        }

        // 固定数据的粘黏分组标签
        mBinding.stickySectionFixBtn.setOnClickListener {
            DecorationTestActivity.start(getContext(), DecorationTestActivity.DECORATION_TYPE_STICKY_SECTION_FIX)
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}