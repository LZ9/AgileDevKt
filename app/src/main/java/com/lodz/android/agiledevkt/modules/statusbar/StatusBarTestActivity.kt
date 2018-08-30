package com.lodz.android.agiledevkt.modules.statusbar

import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorInt
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.base.activity.AbsActivity
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.corekt.utils.toastShort

/**
 * 状态栏透明颜色测试类
 * Created by zhouL on 2018/8/30.
 */
class StatusBarTestActivity : AbsActivity() {

    /** 状态栏背景色 */
    @ColorInt
    private val BG_COLOR = Color.BLUE

    /** 返回按钮 */
    @BindView(R.id.back_btn)
    lateinit var mBackBtn: ImageView
    /** 描述按钮 */
    @BindView(R.id.desc_btn)
    lateinit var mDescBtn: ImageView
    /** 状态栏设置类型单选组 */
    @BindView(R.id.type_rg)
    lateinit var mTypeRadioGroup: RadioGroup
    /** 透明度滚动条 */
    @BindView(R.id.alpha_sb)
    lateinit var mAlphaSeekBar: SeekBar
    /** 透明度值 */
    @BindView(R.id.alpha_value_tv)
    lateinit var mAlphaValueTv: TextView

    /** 是否设置颜色并调整透明度 */
    private var isColorAlpha = false

    override fun getAbsLayoutId() = R.layout.activity_statusbar_test

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

        mTypeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.alpha_rb -> configAlpha()
                R.id.color_alpha_rb -> configColorAlpha()
            }
        }

        mAlphaSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser){
                    return
                }
                if (isColorAlpha){
                    StatusBarUtil.setColor(getContext(), BG_COLOR, progress)
                }else{
                    StatusBarUtil.setTranslucent(getContext(), progress)
                }
                mAlphaValueTv.text = getAlpha().toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun configAlpha() {
        isColorAlpha = false
        StatusBarUtil.setTranslucent(this, getAlpha())
        mAlphaValueTv.text = getAlpha().toString()
    }

    private fun configColorAlpha() {
        isColorAlpha = true
        StatusBarUtil.setColor(this, BG_COLOR, getAlpha())
        mAlphaValueTv.text = getAlpha().toString()
    }

    /** 获取透明度 */
    private fun getAlpha(): Int {
        val alpha = mAlphaSeekBar.progress
        if (alpha < 0) {
            return 0
        }
        if (alpha > 255) {
            return 255
        }
        return alpha
    }
}