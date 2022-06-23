package com.lodz.android.agiledevkt.modules.rv.anim

import android.content.Context
import android.view.View
import android.widget.PopupWindow
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.PopupAnimBinding
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.popup.BasePopupWindow
import com.lodz.android.pandora.widget.rv.recycler.base.AbsRvAdapter

/**
 * 动画PopupWindow
 * Created by zhouL on 2018/11/23.
 */
class AnimPopupWindow(context: Context) : BasePopupWindow(context) {

    companion object {
        /** 自定义 */
        const val TYPE_CUSTOM = 0
    }

    /** 点击 */
    private var mOnClickListener: ((popup: PopupWindow, type: Int) -> Unit)? = null

    private val mBinding: PopupAnimBinding by getContext().bindingLayout(PopupAnimBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun setListeners() {
        super.setListeners()
        // 淡入
        mBinding.alphaInBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), AbsRvAdapter.ALPHA_IN)
        }

        // 缩放
        mBinding.scaleInBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), AbsRvAdapter.SCALE_IN)
        }

        // 底部进入
        mBinding.slideInBottomBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), AbsRvAdapter.SLIDE_IN_BOTTOM)
        }

        // 左侧进入
        mBinding.slideInLeftBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), AbsRvAdapter.SLIDE_IN_LEFT)
        }

        // 右侧进入
        mBinding.slideInRightBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), AbsRvAdapter.SLIDE_IN_RIGHT)
        }

        // 自定义
        mBinding.customBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), TYPE_CUSTOM)
        }
    }

    /** 设置动画类型[animType] */
    fun setAnimType(animType: Int) {
        mBinding.alphaInBtn.setBackgroundResource(if (animType == AbsRvAdapter.ALPHA_IN) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc)
        mBinding.alphaInBtn.setTextColor(getContext().getColorCompat(if (animType == AbsRvAdapter.ALPHA_IN) R.color.color_00a0e9 else R.color.color_9a9a9a))
        mBinding.scaleInBtn.setBackgroundResource(if (animType == AbsRvAdapter.SCALE_IN) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc)
        mBinding.scaleInBtn.setTextColor(getContext().getColorCompat(if (animType == AbsRvAdapter.SCALE_IN) R.color.color_00a0e9 else R.color.color_9a9a9a))
        mBinding.slideInBottomBtn.setBackgroundResource(if (animType == AbsRvAdapter.SLIDE_IN_BOTTOM) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc)
        mBinding.slideInBottomBtn.setTextColor(getContext().getColorCompat(if (animType == AbsRvAdapter.SLIDE_IN_BOTTOM) R.color.color_00a0e9 else R.color.color_9a9a9a))
        mBinding.slideInLeftBtn.setBackgroundResource(if (animType == AbsRvAdapter.SLIDE_IN_LEFT) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc)
        mBinding.slideInLeftBtn.setTextColor(getContext().getColorCompat(if (animType == AbsRvAdapter.SLIDE_IN_LEFT) R.color.color_00a0e9 else R.color.color_9a9a9a))
        mBinding.slideInRightBtn.setBackgroundResource(if (animType == AbsRvAdapter.SLIDE_IN_RIGHT) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc)
        mBinding.slideInRightBtn.setTextColor(getContext().getColorCompat(if (animType == AbsRvAdapter.SLIDE_IN_RIGHT) R.color.color_00a0e9 else R.color.color_9a9a9a))
        mBinding.customBtn.setBackgroundResource(if (animType == TYPE_CUSTOM) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc)
        mBinding.customBtn.setTextColor(getContext().getColorCompat(if (animType == TYPE_CUSTOM) R.color.color_00a0e9 else R.color.color_9a9a9a))
    }

    /** 设置监听器[listener] */
    fun setOnClickListener(listener: (popup: PopupWindow, type: Int) -> Unit) {
        mOnClickListener = listener
    }
}