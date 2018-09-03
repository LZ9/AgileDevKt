package com.lodz.android.agiledevkt.modules.statusbar

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.utils.StatusBarUtil

/**
 * 状态栏透明颜色测试类
 * Created by zhouL on 2018/8/30.
 */
class StatusBarTestActivity : BaseActivity() {

    companion object {

        /** 默认颜色 */
        const val DEFAULT_COLOR = Color.DKGRAY

        fun start(context: Context) {
            val intent = Intent(context, StatusBarTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 透明度滚动条 */
    @BindView(R.id.alpha_sb)
    lateinit var mAlphaSeekBar: SeekBar
    /** 透明度值 */
    @BindView(R.id.alpha_value_tv)
    lateinit var mAlphaValueTv: TextView

    /** 测试带图片的状态栏按钮 */
    @BindView(R.id.test_img_btn)
    lateinit var mTestImgBtn: Button
    /** 测试带DrawerLayout的状态栏 */
    @BindView(R.id.test_drawer_btn)
    lateinit var mTestDrawerBtn: Button


    override fun getLayoutId() = R.layout.activity_statusbar_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
        getTitleBarLayout().setBackgroundColor(DEFAULT_COLOR)
    }

    override fun clickBackBtn() {
        super.clickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        mAlphaSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    return
                }
                StatusBarUtil.setColor(window, DEFAULT_COLOR, progress / 100.0f)
                updateAlphaValue()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        mTestImgBtn.setOnClickListener {
            StatusBarImgTestActivity.start(getContext())
        }

        mTestDrawerBtn.setOnClickListener {
            StatusBarDrawerTestActivity.start(getContext())
        }
    }

    override fun initData() {
        super.initData()
        StatusBarUtil.setColor(window, DEFAULT_COLOR, mAlphaSeekBar.progress / 100.0f)
        updateAlphaValue()
        showStatusCompleted()
    }

    /** 更新透明度 */
    private fun updateAlphaValue() {
        mAlphaValueTv.text = mAlphaSeekBar.progress.toString()
    }

}