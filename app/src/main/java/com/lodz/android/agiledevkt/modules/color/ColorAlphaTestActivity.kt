package com.lodz.android.agiledevkt.modules.color

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.utils.ColorUtils

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

    /** 透明度拖动条 */
    @BindView(R.id.seek_bar)
    lateinit var mSeekBar: SeekBar
    /** 颜色百分比文字 */
    @BindView(R.id.percentage_tv)
    lateinit var mPercentageTv: TextView
    /** 封面颜色控件 */
    @BindView(R.id.cover_view)
    lateinit var mCoverView: View

    override fun getLayoutId() = R.layout.activity_color_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (!fromUser){
                    return
                }
                mPercentageTv.text = (progress.toString() + "%")
                val colorInt = ColorUtils.getColorAlphaRes(getContext(), R.color.yellow, (progress / 100.0).toFloat())
                mCoverView.setBackgroundColor(colorInt)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun initData() {
        super.initData()
        mPercentageTv.text = ("100%")
        showStatusCompleted()
    }
}