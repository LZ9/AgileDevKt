package com.lodz.android.agiledevkt.modules.statusbar

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityStatusbarTestBinding
import com.lodz.android.agiledevkt.modules.coordinator.CoorStatusBarTestActivity
import com.lodz.android.agiledevkt.modules.drawer.DrawerTestActivity
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

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

    private val mBinding: ActivityStatusbarTestBinding by bindingLayout(ActivityStatusbarTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

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

        // 透明度滚动条
        mBinding.alphaSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

        // 测试带图片的状态栏按钮
        mBinding.testImgBtn.setOnClickListener {
            StatusBarImgTestActivity.start(getContext())
        }

        // 测试带DrawerLayout的状态栏
        mBinding.testDrawerBtn.setOnClickListener {
            DrawerTestActivity.start(getContext(), getString(R.string.status_bar_test_drawer))
        }

        // 带CoordinatorLayout的状态栏测试类
        mBinding.testCoordinatorBtn.setOnClickListener {
            CoorStatusBarTestActivity.start(getContext(), getString(R.string.status_bar_test_coordinator))
        }
    }

    override fun initData() {
        super.initData()
        StatusBarUtil.setColor(window, DEFAULT_COLOR, mBinding.alphaSb.progress / 100.0f)
        updateAlphaValue()
        showStatusCompleted()
    }

    /** 更新透明度 */
    private fun updateAlphaValue() {
        mBinding.alphaValueTv.text = mBinding.alphaSb.progress.toString()
    }

}