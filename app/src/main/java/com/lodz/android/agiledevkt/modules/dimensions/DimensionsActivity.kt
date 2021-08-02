package com.lodz.android.agiledevkt.modules.dimensions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityDimensionsBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.*
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 单位转换测试类
 * @author zhouL
 * @date 2019/5/21
 */
class DimensionsActivity :BaseActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, DimensionsActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityDimensionsBinding by bindingLayout(ActivityDimensionsBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // px输入
        mBinding.pxCedit.setOnJumpClickListener {
            if (mBinding.pxCedit.getContentText().isEmpty()){
                return@setOnJumpClickListener
            }
            val px = mBinding.pxCedit.getContentText().toFloat()
            mBinding.pxDpCtv.setContentText(px2dp(px).toString())
            mBinding.pxSpCtv.setContentText(px2sp(px).toString())
        }

        // dp输入
        mBinding.dpCedit.setOnJumpClickListener {
            if (mBinding.dpCedit.getContentText().isEmpty()){
                return@setOnJumpClickListener
            }
            val dp = mBinding.dpCedit.getContentText().toFloat()
            mBinding.dpPxCtv.setContentText(dp2px(dp).toString())
        }

        // sp输入
        mBinding.spCedit.setOnJumpClickListener {
            if (mBinding.spCedit.getContentText().isEmpty()){
                return@setOnJumpClickListener
            }
            val sp = mBinding.spCedit.getContentText().toFloat()
            mBinding.spPxCtv.setContentText(sp2px(sp).toString())
        }
    }

    override fun initData() {
        super.initData()
        mBinding.screenInfoTv.text = StringBuilder().append("width : ").append(getScreenWidth()).append(" ; ").append("height : ").append(getRealScreenHeight())
        showStatusCompleted()
    }

}