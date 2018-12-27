package com.lodz.android.agiledevkt.modules.rv.decoration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity

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

    /** 底部分割线 */
    private val mRoundBottomBtn by bindView<MaterialButton>(R.id.round_bottom_btn)
    /** 外围分割线 */
    private val mRoundBtn by bindView<MaterialButton>(R.id.round_btn)
    /** 网格分割线 */
    private val mGridBtn by bindView<MaterialButton>(R.id.grid_btn)
    /** 分组标签 */
    private val mSectionBtn by bindView<MaterialButton>(R.id.section_btn)
    /** 固定数据的分组标签 */
    private val mSectionFixBtn by bindView<MaterialButton>(R.id.section_fix_btn)
    /** 粘黏分组标签 */
    private val mStickySectionBtn by bindView<MaterialButton>(R.id.sticky_section_btn)
    /** 固定数据的粘黏分组标签 */
    private val mStickySectionFixBtn by bindView<MaterialButton>(R.id.sticky_section_fix_btn)

    override fun getLayoutId(): Int = R.layout.activity_decoration_main

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        mRoundBottomBtn.setOnClickListener {
            DecorationTestActivity.start(getContext(), DecorationTestActivity.DECORATION_TYPE_ROUND_BOTTOM)
        }

        mRoundBtn.setOnClickListener {
            DecorationTestActivity.start(getContext(), DecorationTestActivity.DECORATION_TYPE_ROUND)
        }

        mGridBtn.setOnClickListener {
            DecorationTestActivity.start(getContext(), DecorationTestActivity.DECORATION_TYPE_GRID)
        }

        mSectionBtn.setOnClickListener {
            DecorationTestActivity.start(getContext(), DecorationTestActivity.DECORATION_TYPE_SECTION)
        }

        mSectionFixBtn.setOnClickListener {
            DecorationTestActivity.start(getContext(), DecorationTestActivity.DECORATION_TYPE_SECTION_FIX)
        }

        mStickySectionBtn.setOnClickListener {
            DecorationTestActivity.start(getContext(), DecorationTestActivity.DECORATION_TYPE_STICKY_SECTION)
        }

        mStickySectionFixBtn.setOnClickListener {
            DecorationTestActivity.start(getContext(), DecorationTestActivity.DECORATION_TYPE_STICKY_SECTION_FIX)
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}