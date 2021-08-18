package com.lodz.android.agiledevkt.modules.statusbar

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityStatusbarImgTestBinding
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 带ImageView的状态栏颜色测试
 * Created by zhouL on 2018/8/31.
 */
class StatusBarImgTestActivity : AbsActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, StatusBarImgTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityStatusbarImgTestBinding by bindingLayout(ActivityStatusbarImgTestBinding::inflate)

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
    }

    override fun setListeners() {
        super.setListeners()
        // 返回按钮
        mBinding.backBtn.setOnClickListener {
            finish()
        }

        // 描述按钮
        mBinding.descBtn.setOnClickListener {
            toastShort(R.string.status_bar_old_trafford)
        }

        // 透明度滚动条
        mBinding.alphaSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    return
                }
                StatusBarUtil.setTransparentForOffsetView(this@StatusBarImgTestActivity, mBinding.titleLayout, progress / 100.0f, Color.DKGRAY)
                updateAlphaValue()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun initData() {
        super.initData()
        StatusBarUtil.setTransparentForOffsetView(this@StatusBarImgTestActivity, mBinding.titleLayout, mBinding.alphaSb.progress / 100.0f, Color.DKGRAY)
        updateAlphaValue()
    }

    /** 更新透明度 */
    private fun updateAlphaValue() {
        mBinding.alphaValueTv.text = mBinding.alphaSb.progress.toString()
    }

}