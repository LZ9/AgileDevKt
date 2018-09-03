package com.lodz.android.agiledevkt.modules.statusbar

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.base.activity.AbsActivity
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.corekt.utils.toastShort

/**
 * 带ImageView的状态栏颜色测试
 * Created by zhouL on 2018/8/31.
 */
class StatusBarImgTestActivity :AbsActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, StatusBarImgTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 标题布局 */
    @BindView(R.id.title_layout)
    lateinit var mTitleLayout: ViewGroup
    /** 返回按钮 */
    @BindView(R.id.back_btn)
    lateinit var mBackBtn: ImageView
    /** 描述按钮 */
    @BindView(R.id.desc_btn)
    lateinit var mDescBtn: ImageView

    /** 透明度滚动条 */
    @BindView(R.id.alpha_sb)
    lateinit var mAlphaSeekBar: SeekBar
    /** 透明度值 */
    @BindView(R.id.alpha_value_tv)
    lateinit var mAlphaValueTv: TextView

    override fun getAbsLayoutId() = R.layout.activity_statusbar_img_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
    }

    override fun setListeners() {
        super.setListeners()
        mBackBtn.setOnClickListener {
            finish()
        }

        mDescBtn.setOnClickListener {
            toastShort(R.string.status_bar_old_trafford)
        }

        mAlphaSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    return
                }
                StatusBarUtil.setTransparentForImageView(getContext(), mTitleLayout, progress / 100.0f, Color.DKGRAY)
                updateAlphaValue()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun initData() {
        super.initData()
        StatusBarUtil.setTransparentForImageView(getContext(), mTitleLayout, mAlphaSeekBar.progress / 100.0f, Color.DKGRAY)
        updateAlphaValue()
    }

    /** 更新透明度 */
    private fun updateAlphaValue() {
        mAlphaValueTv.text = mAlphaSeekBar.progress.toString()
    }

}