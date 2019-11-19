package com.lodz.android.agiledevkt.modules.rv.anim

import android.content.Context
import android.widget.PopupWindow
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.pandora.widget.popup.BasePopupWindow
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColor

/**
 * 动画PopupWindow
 * Created by zhouL on 2018/11/23.
 */
class AnimPopupWindow(context: Context) : BasePopupWindow(context) {

    companion object {
        /** 自定义 */
        const val TYPE_CUSTOM = 0
    }

    /** 淡入 */
    private val mAlphaInBtn by getPopup().bindView<TextView>(R.id.alpha_in_btn)
    /** 缩放 */
    private val mScaleInBtn by getPopup().bindView<TextView>(R.id.scale_in_btn)
    /** 底部进入 */
    private val mSlideInBottomBtn by getPopup().bindView<TextView>(R.id.slide_in_bottom_btn)
    /** 左侧进入 */
    private val mSlideInLeftBtn by getPopup().bindView<TextView>(R.id.slide_in_left_btn)
    /** 右侧进入 */
    private val mSlideInRightBtn by getPopup().bindView<TextView>(R.id.slide_in_right_btn)
    /** 自定义 */
    private val mCustomBtn by getPopup().bindView<TextView>(R.id.custom_btn)

    /** 点击 */
    private var mOnClickListener: ((popup: PopupWindow, type: Int) -> Unit)? = null

    override fun getLayoutId(): Int = R.layout.popup_anim

    override fun setListeners() {
        super.setListeners()
        mAlphaInBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), BaseRecyclerViewAdapter.ALPHA_IN)
        }

        mScaleInBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), BaseRecyclerViewAdapter.SCALE_IN)
        }

        mSlideInBottomBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), BaseRecyclerViewAdapter.SLIDE_IN_BOTTOM)
        }

        mSlideInLeftBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), BaseRecyclerViewAdapter.SLIDE_IN_LEFT)
        }

        mSlideInRightBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), BaseRecyclerViewAdapter.SLIDE_IN_RIGHT)
        }

        mCustomBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), TYPE_CUSTOM)
        }
    }

    /** 设置动画类型[animType] */
    fun setAnimType(animType: Int) {
        mAlphaInBtn.backgroundResource = if (animType == BaseRecyclerViewAdapter.ALPHA_IN) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc
        mAlphaInBtn.textColor = getContext().getColorCompat(if (animType == BaseRecyclerViewAdapter.ALPHA_IN) R.color.color_00a0e9 else R.color.color_9a9a9a)
        mScaleInBtn.backgroundResource = if (animType == BaseRecyclerViewAdapter.SCALE_IN) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc
        mScaleInBtn.textColor = getContext().getColorCompat(if (animType == BaseRecyclerViewAdapter.SCALE_IN) R.color.color_00a0e9 else R.color.color_9a9a9a)
        mSlideInBottomBtn.backgroundResource = if (animType == BaseRecyclerViewAdapter.SLIDE_IN_BOTTOM) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc
        mSlideInBottomBtn.textColor = getContext().getColorCompat(if (animType == BaseRecyclerViewAdapter.SLIDE_IN_BOTTOM) R.color.color_00a0e9 else R.color.color_9a9a9a)
        mSlideInLeftBtn.backgroundResource = if (animType == BaseRecyclerViewAdapter.SLIDE_IN_LEFT) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc
        mSlideInLeftBtn.textColor = getContext().getColorCompat(if (animType == BaseRecyclerViewAdapter.SLIDE_IN_LEFT) R.color.color_00a0e9 else R.color.color_9a9a9a)
        mSlideInRightBtn.backgroundResource = if (animType == BaseRecyclerViewAdapter.SLIDE_IN_RIGHT) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc
        mSlideInRightBtn.textColor = getContext().getColorCompat(if (animType == BaseRecyclerViewAdapter.SLIDE_IN_RIGHT) R.color.color_00a0e9 else R.color.color_9a9a9a)
        mCustomBtn.backgroundResource = if (animType == TYPE_CUSTOM) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc
        mCustomBtn.textColor = getContext().getColorCompat(if (animType == TYPE_CUSTOM) R.color.color_00a0e9 else R.color.color_9a9a9a)
    }

    /** 设置监听器[listener] */
    fun setOnClickListener(listener: (popup: PopupWindow, type: Int) -> Unit) {
        mOnClickListener = listener
    }
}