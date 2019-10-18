package com.lodz.android.agiledevkt.modules.statusbar

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.coordinator.CoorStatusBarTestActivity
import com.lodz.android.agiledevkt.modules.drawer.DrawerTestActivity
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.pandora.base.activity.BaseActivity

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
    private val mAlphaSeekBar by bindView<SeekBar>(R.id.alpha_sb)
    /** 透明度值 */
    private val mAlphaValueTv by bindView<TextView>(R.id.alpha_value_tv)

    /** 测试带图片的状态栏按钮 */
    private val mTestImgBtn by bindView<Button>(R.id.test_img_btn)
    /** 测试带DrawerLayout的状态栏 */
    private val mTestDrawerBtn by bindView<Button>(R.id.test_drawer_btn)
    /** 带CoordinatorLayout的状态栏测试类 */
    private val mTestCoordinatorBtn by bindView<Button>(R.id.test_coordinator_btn)

    override fun getLayoutId() = R.layout.activity_statusbar_test

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        getTitleBarLayout().setBackgroundColor(DEFAULT_COLOR)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
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
            DrawerTestActivity.start(getContext(), getString(R.string.status_bar_test_drawer))
        }

        mTestCoordinatorBtn.setOnClickListener {
            CoorStatusBarTestActivity.start(getContext(), getString(R.string.status_bar_test_coordinator))
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