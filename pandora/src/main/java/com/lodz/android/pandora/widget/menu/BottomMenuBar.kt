package com.lodz.android.pandora.widget.menu

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.util.Pair
import com.lodz.android.corekt.anko.createColorIntDrawable
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.utils.SelectorUtils
import com.lodz.android.pandora.R

/**
 * 底部菜单栏
 * @author zhouL
 * @date 2019/2/25
 */
class BottomMenuBar : LinearLayout {

    /** 数据 */
    private var mPdrList: ArrayList<Pair<MenuConfig, ViewGroup>> = ArrayList()
    /** 菜单选择监听器 */
    private var mPdrOnMenuSelectedListener: ((view: View, menuConfig: MenuConfig) -> Unit)? = null
    /** 菜单点击监听器 */
    private var mPdrOnMenuClickListener: ((view: View, menuConfig: MenuConfig) -> Boolean)? = null
    /** 菜单长按监听器 */
    private var mPdrOnMenuLongClickListener: ((view: View, menuConfig: MenuConfig) -> Boolean)? = null
    /** 菜单角标图片点击监听器 */
    private var mPdrOnMenuBadgeImgClickListener: ((view: View, menuConfig: MenuConfig) -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        orientation = LinearLayout.HORIZONTAL
    }

    /** 设置菜单配置[configs] */
    @SuppressLint("InflateParams")
    fun setMenuConfigs(configs: List<MenuConfig>) {
        if (configs.isEmpty()) {
            return
        }
        if (mPdrList.size > 0) {//已经赋值过了
            return
        }
        for (config in configs) {
            val viewGroup = LayoutInflater.from(context).inflate(R.layout.pandora_view_menu_bar_item, null) as ViewGroup
            val layoutParams = LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
            addView(viewGroup, layoutParams)
            val iconImg = viewGroup.findViewById<ImageView>(R.id.pdr_ic_img)
            val textTv = viewGroup.findViewById<TextView>(R.id.pdr_text_tv)
            val numTv = viewGroup.findViewById<TextView>(R.id.pdr_num_tv)
            val pointView = viewGroup.findViewById<View>(R.id.pdr_point_view)
            val badgeImg = viewGroup.findViewById<ImageView>(R.id.pdr_badge_img)
            val indicatorView = viewGroup.findViewById<View>(R.id.pdr_indicator_view)

            iconImg.visibility = if (config.iconDrawableState != null) View.VISIBLE else View.GONE
            iconImg.setImageDrawable(config.iconDrawableState)
            if (config.iconSizeDp > 0){
                iconImg.layoutParams.height = dp2px(config.iconSizeDp)
                iconImg.layoutParams.width = dp2px(config.iconSizeDp)
            }

            textTv.visibility = if (config.text.isEmpty()) View.GONE else View.VISIBLE
            if (config.textSizeSp > 0f) {
                textTv.textSize = config.textSizeSp
            }
            textTv.layoutParams.width = config.textWidthPx
            textTv.layoutParams.height = config.textHeightPx
            if (config.textEllipsize != null) {
                textTv.ellipsize = config.textEllipsize
            }
            if (config.textMaxLength > 0) {
                textTv.setFilters(arrayOf<InputFilter>(LengthFilter(config.textMaxLength)))
            }
            textTv.setLines(config.textLines)
            if (config.textColorState != null){
                textTv.setTextColor(config.textColorState)
            }
            if (config.drawablePaddingPx > 0){
                val lp = textTv.layoutParams as LayoutParams
                lp.topMargin = config.drawablePaddingPx
            }
            textTv.text = config.text

            numTv.text = config.num.toString()
            if (config.numTextBackgroundDrawableResId != 0) {
                numTv.setBackgroundResource(config.numTextBackgroundDrawableResId)
            }
            if (config.numTextColor != 0) {
                numTv.setTextColor(config.numTextColor)
            }
            if (config.numTextSizeSp > 0f) {
                numTv.textSize = config.numTextSizeSp
            }
            if (config.numTextAngle > 0) {
                val lp = numTv.layoutParams as ConstraintLayout.LayoutParams
                lp.circleAngle = config.numTextAngle.toFloat()
            }
            if (config.numTextRadiusPx > 0) {
                val lp = numTv.layoutParams as ConstraintLayout.LayoutParams
                lp.circleRadius = config.numTextRadiusPx
            }
            if (config.numTextBgSizeDp > 0){
                val lp = numTv.layoutParams
                lp.width = dp2px(config.numTextBgSizeDp)
                lp.width = dp2px(config.numTextBgSizeDp)
            }
            numTv.visibility = if (config.num == 0) View.GONE else View.VISIBLE

            pointView.visibility = config.pointVisibility
            pointView.setBackgroundResource(config.pointBackgroundDrawableResId)
            if (config.pointAngle > 0) {
                val lp = pointView.layoutParams as ConstraintLayout.LayoutParams
                lp.circleAngle = config.pointAngle.toFloat()
            }
            if (config.pointRadiusPx > 0) {
                val lp = pointView.layoutParams as ConstraintLayout.LayoutParams
                lp.circleRadius = config.pointRadiusPx
            }

            badgeImg.visibility = config.badgeImgVisibility
            if (config.badgeImgResId != 0) {
                badgeImg.setImageResource(config.badgeImgResId)
            }
            badgeImg.layoutParams.width = config.badgeImgWidthPx
            badgeImg.layoutParams.height = config.badgeImgHeightPx
            if (config.badgeImgAngle > 0) {
                val lp = badgeImg.layoutParams as ConstraintLayout.LayoutParams
                lp.circleAngle = config.badgeImgAngle.toFloat()
            }
            if (config.badgeImgRadiusPx > 0) {
                val lp = badgeImg.layoutParams as ConstraintLayout.LayoutParams
                lp.circleRadius = config.badgeImgRadiusPx
            }
            badgeImg.setOnClickListener {
                mPdrOnMenuBadgeImgClickListener?.invoke(it, config)
            }

            if (config.isUseIndicator) {
                indicatorView.background = SelectorUtils.createBgSelectedDrawable(
                    context.createColorIntDrawable(Color.TRANSPARENT),
                    context.createColorIntDrawable(config.indicatorColor)
                )
                val lp = indicatorView.layoutParams as ConstraintLayout.LayoutParams
                lp.width = config.indicatorWidthPx
                lp.height = config.indicatorHeightPx
                lp.marginStart = config.indicatorMarginStartPx
                lp.marginEnd = config.indicatorMarginEndPx
                lp.topMargin = config.indicatorMarginTopPx
                lp.bottomMargin = config.indicatorMarginBottomPx
                indicatorView.layoutParams = lp
                indicatorView.visibility = View.VISIBLE
            } else {
                indicatorView.visibility = View.GONE
            }

            viewGroup.setOnClickListener {
                if (!onClickMenu(config.type)){
                    setSelectedMenu(config.type)
                }
            }
            viewGroup.setOnLongClickListener {
                onLongClickMenu(config.type)
            }
            mPdrList.add(Pair(config, viewGroup))
        }
    }

    /** 更新菜单类型[type]对应的数字[num]提示 */
    fun updateNum(type: Int, num: Int) {
        if (mPdrList.size == 0) {
            return
        }
        for (pair in mPdrList) {
            val config = pair.first
            val viewGroup = pair.second
            if (config == null || viewGroup == null) {
                continue
            }
            if (config.type == type) {
                val numTv = viewGroup.findViewById<TextView>(R.id.pdr_num_tv)
                numTv.text = num.toString()
                numTv.visibility = if (num == 0) View.GONE else View.VISIBLE
                config.num = num
            }
        }
    }

    /** 更新菜单类型[type]对应的提示点显隐[visibility]和颜色[resId] */
    fun updatePointVisibility(type: Int, visibility: Int, @DrawableRes resId: Int = 0) {
        if (mPdrList.size == 0) {
            return
        }
        for (pair in mPdrList) {
            val config = pair.first
            val viewGroup = pair.second
            if (config == null || viewGroup == null) {
                continue
            }
            if (config.type == type) {
                val pointView = viewGroup.findViewById<View>(R.id.pdr_point_view)
                pointView.visibility = visibility
                config.pointVisibility = visibility
                if (resId != 0) {
                    pointView.setBackgroundResource(resId)
                    config.pointBackgroundDrawableResId = resId
                }
            }
        }
    }

    /** 更新菜单类型[type]对应的角标图片显隐[visibility]和资源[resId] */
    fun updateBadgeImgVisibility(type: Int, visibility: Int, @DrawableRes resId: Int = 0) {
        if (mPdrList.size == 0) {
            return
        }
        for (pair in mPdrList) {
            val config = pair.first
            val viewGroup = pair.second
            if (config == null || viewGroup == null) {
                continue
            }
            if (config.type == type) {
                val badgeImg = viewGroup.findViewById<ImageView>(R.id.pdr_badge_img)
                badgeImg.visibility = visibility
                config.badgeImgVisibility = visibility
                if (resId != 0) {
                    badgeImg.setImageResource(resId)
                    config.badgeImgResId = resId
                }
            }
        }
    }

    /** 设置选中的菜单，如果点击已选中的菜单，则不会再回调 */
    fun setSelectedMenu(type: Int){
        for (pair in mPdrList) {
            val cfg = pair.first
            val vg = pair.second
            if (cfg == null || vg == null) {
                continue
            }
            if (cfg.type == type) {
                if (!vg.isSelected) {
                    vg.isSelected = true
                    mPdrOnMenuSelectedListener?.invoke(vg, cfg)
                }
            } else {
                vg.isSelected = false
            }
        }
    }

    /** 获取当前选中得menu类型，没有menu被选中时返回-1 */
    fun getCurrentSelectedType(): Int {
        if (mPdrList.size == 0) {
            return -1
        }
        for (pair in mPdrList) {
            val config = pair.first
            val viewGroup = pair.second
            if (config == null || viewGroup == null) {
                continue
            }
            if (viewGroup.isSelected) {
                return config.type
            }
        }
        return -1
    }

    /** 点击菜单回调 */
    private fun onClickMenu(type: Int): Boolean {
        for (pair in mPdrList) {
            val cfg = pair.first
            val vg = pair.second
            if (cfg == null || vg == null) {
                continue
            }
            if (cfg.type == type) {
                return mPdrOnMenuClickListener?.invoke(vg, cfg) ?: false
            }
        }
        return false
    }

    /** 长按菜单回调 */
    private fun onLongClickMenu(type: Int): Boolean {
        for (pair in mPdrList) {
            val cfg = pair.first
            val vg = pair.second
            if (cfg == null || vg == null) {
                continue
            }
            if (cfg.type == type) {
                return mPdrOnMenuLongClickListener?.invoke(vg, cfg) ?: false
            }
        }
        return false
    }

    fun cleanMenu(){
        mPdrList.clear()
        mPdrList = ArrayList()
        removeAllViews()
    }

    /** 设置菜单选中监听器 */
    fun setOnMenuSelectedListener(listener: (view: View, menuConfig: MenuConfig) -> Unit) {
        mPdrOnMenuSelectedListener = listener
    }

    /** 设置菜单点击监听器 */
    fun setOnMenuClickListener(listener: (view: View, menuConfig: MenuConfig) -> Boolean) {
        mPdrOnMenuClickListener = listener
    }

    /** 设置菜单长按监听器 */
    fun setOnMenuLongClickListener(listener: (view: View, menuConfig: MenuConfig) -> Boolean) {
        mPdrOnMenuLongClickListener = listener
    }

    /** 设置菜单角标图片点击监听器 */
    fun setOnMenuBadgeImgClickListener(listener: (view: View, menuConfig: MenuConfig) -> Unit) {
        mPdrOnMenuBadgeImgClickListener = listener
    }
}