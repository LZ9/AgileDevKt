package com.lodz.android.agiledevkt.modules.color

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityColorTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.utils.ColorUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 颜色透明度测试
 * Created by zhouL on 2018/7/16.
 */
class ColorAlphaTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, ColorAlphaTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityColorTestBinding by bindingLayout(ActivityColorTestBinding::inflate)

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
        // 透明度拖动条
        mBinding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (!fromUser){
                    return
                }
                mBinding.percentageTv.text = ("$progress%")
                val colorInt = ColorUtils.getColorAlphaRes(getContext(), R.color.yellow, (progress / 100.0).toFloat())
                mBinding.coverView.setBackgroundColor(colorInt)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun initData() {
        super.initData()
        mBinding.percentageTv.text = ("100%")
        showStatusCompleted()
    }
}