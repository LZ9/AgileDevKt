package com.lodz.android.agiledevkt.modules.dimensions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.*
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.collect.CltEditView
import com.lodz.android.pandora.widget.collect.CltTextView

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

    /** 屏幕宽高 */
    private val mScreenInfoTv by bindView<TextView>(R.id.screen_info_tv)
    /** px输入 */
    private val mPxCedit by bindView<CltEditView>(R.id.px_cedit)
    /** px转dp */
    private val mPxDpCtv by bindView<CltTextView>(R.id.px_dp_ctv)
    /** px转sp */
    private val mPxSpCtv by bindView<CltTextView>(R.id.px_sp_ctv)
    /** dp输入 */
    private val mDpCedit by bindView<CltEditView>(R.id.dp_cedit)
    /** dp转px */
    private val mDpPxCtv by bindView<CltTextView>(R.id.dp_px_ctv)
    /** sp输入 */
    private val mSpCedit by bindView<CltEditView>(R.id.sp_cedit)
    /** sp转px */
    private val mSpPxCtv by bindView<CltTextView>(R.id.sp_px_ctv)

    override fun getLayoutId(): Int = R.layout.activity_dimensions

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
        mPxCedit.setOnJumpClickListener {
            if (mPxCedit.getContentText().isEmpty()){
                return@setOnJumpClickListener
            }
            val px = mPxCedit.getContentText().toFloat()
            mPxDpCtv.setContentText(px2dp(px).toString())
            mPxSpCtv.setContentText(px2sp(px).toString())
        }

        mDpCedit.setOnJumpClickListener {
            if (mDpCedit.getContentText().isEmpty()){
                return@setOnJumpClickListener
            }
            val dp = mDpCedit.getContentText().toFloat()
            mDpPxCtv.setContentText(dp2px(dp).toString())
        }

        mSpCedit.setOnJumpClickListener {
            if (mSpCedit.getContentText().isEmpty()){
                return@setOnJumpClickListener
            }
            val sp = mSpCedit.getContentText().toFloat()
            mSpPxCtv.setContentText(sp2px(sp).toString())
        }
    }

    override fun initData() {
        super.initData()
        mScreenInfoTv.text = StringBuilder().append("width : ").append(getScreenWidth()).append(" ; ").append("height : ").append(getRealScreenWidth())
        showStatusCompleted()
    }

}