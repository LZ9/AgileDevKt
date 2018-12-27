package com.lodz.android.agiledevkt.modules.color

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.utils.ColorUtils
import com.lodz.android.pandora.base.activity.BaseActivity

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
    private val mSeekBar by bindView<SeekBar>(R.id.seek_bar)
    /** 颜色百分比文字 */
    private val mPercentageTv by bindView<TextView>(R.id.percentage_tv)
    /** 封面颜色控件 */
    private val mCoverView by bindView<View>(R.id.cover_view)

    override fun getLayoutId() = R.layout.activity_color_test

    override fun findViews(savedInstanceState: Bundle?) {
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