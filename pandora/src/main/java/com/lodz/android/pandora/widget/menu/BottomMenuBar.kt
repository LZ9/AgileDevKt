package com.lodz.android.pandora.widget.menu

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.util.Pair
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.pandora.R

/**
 * 底部菜单栏
 * @author zhouL
 * @date 2019/2/25
 */
class BottomMenuBar : LinearLayout {

    /** 数据 */
    private var mList: ArrayList<Pair<MenuConfig, ViewGroup>> = ArrayList()
    /** 菜单选择监听器 */
    private var mOnSelectedListener: ((type: Int) -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        orientation = LinearLayout.HORIZONTAL
    }

    private fun findViews() {
        orientation = LinearLayout.HORIZONTAL
    }

    /** 设置菜单配置[configs] */
    @SuppressLint("InflateParams")
    fun setMenuConfigs(configs: List<MenuConfig>) {
        if (configs.isEmpty()) {
            return
        }
        if (mList.size > 0) {//已经赋值过了
            return
        }
        for (config in configs) {
            val viewGroup = LayoutInflater.from(context).inflate(R.layout.pandora_view_menu_bar_item, null) as ViewGroup
            val layoutParams = LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
            addView(viewGroup, layoutParams)
            val iconImg = viewGroup.findViewById<ImageView>(R.id.ic_img)
            val textTv = viewGroup.findViewById<TextView>(R.id.text_tv)
            val numTv = viewGroup.findViewById<TextView>(R.id.num_tv)

            if (config.getIconDrawableState() == null || config.getTextColorState() == null) {// 未设置图标和文字颜色
                break
            }

            iconImg.setImageDrawable(config.getIconDrawableState())
            if (config.getIconSize() > 0){
                iconImg.layoutParams.height = dp2px(config.getIconSize())
                iconImg.layoutParams.width = dp2px(config.getIconSize())
            }

            textTv.text = config.getText()
            if (config.getTextSize() > 0f) {
                textTv.textSize = config.getTextSize()
            }
            textTv.setTextColor(config.getTextColorState())
            if (config.getDrawablePadding() > 0){
                val lp = textTv.layoutParams as LayoutParams
                lp.topMargin = config.getDrawablePadding()
            }

            numTv.text = config.getNum().toString()
            if (config.getNumBackgroundDrawableResId() != 0) {
                numTv.setBackgroundResource(config.getNumBackgroundDrawableResId())
            }
            if (config.getNumTextColor() != 0) {
                numTv.setTextColor(config.getNumTextColor())
            }
            if (config.getNumTextSize() > 0f) {
                numTv.textSize = config.getNumTextSize()
            }
            if (config.getNumTextMarginTop() > 0){
                val lp = numTv.layoutParams as FrameLayout.LayoutParams
                lp.topMargin = config.getNumTextMarginTop()
            }
            if (config.getNumTextMarginEnd() > 0){
                val lp = numTv.layoutParams as FrameLayout.LayoutParams
                lp.marginEnd = config.getNumTextMarginEnd()
            }
            if (config.getNumTextBgSizeDp() > 0){
                val lp = numTv.layoutParams
                lp.width = dp2px(config.getNumTextBgSizeDp())
                lp.width = dp2px(config.getNumTextBgSizeDp())
            }
            numTv.visibility = if (config.getNum() == 0) View.GONE else View.VISIBLE

            viewGroup.setOnClickListener {
                setSelectedMenu(config.getType())
            }
            mList.add(Pair(config, viewGroup))
        }
    }

    /** 更新菜单类型[type]对应的数字[num]提示 */
    fun updateNum(type: Int, num: Int) {
        if (mList.size == 0) {
            return
        }
        for (pair in mList) {
            val config = pair.first
            val viewGroup = pair.second
            if (config == null || viewGroup == null) {
                continue
            }
            if (config.getType() == type) {
                val numTv = viewGroup.findViewById<TextView>(R.id.num_tv)
                numTv.text = num.toString()
                numTv.visibility = if (num == 0) View.GONE else View.VISIBLE
            }
        }
    }

    /** 设置选中的菜单 */
    fun setSelectedMenu(type: Int){
        for (pair in mList) {
            val cfg = pair.first
            val vg = pair.second
            if (cfg == null || vg == null) {
                continue
            }
            if (cfg.getType() == type){
                if (!vg.isSelected){
                    vg.isSelected =  true
                    mOnSelectedListener?.invoke(type)
                }
            }else{
                vg.isSelected =  false
            }
        }
    }

    /** 设置点击事件监听器 */
    fun setOnSelectedListener(listener: (type: Int) -> Unit) {
        mOnSelectedListener = listener
    }
}