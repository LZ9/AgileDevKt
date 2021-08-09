package com.lodz.android.agiledevkt.modules.rv.popup

import android.content.Context
import android.view.View
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.PopupOrientationBinding
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.popup.BasePopupWindow

/**
 * RV横纵方向PopupWindow
 * Created by zhouL on 2018/11/27.
 */
class OrientationPopupWindow(context: Context) : BasePopupWindow(context) {

    private val mBinding: PopupOrientationBinding by getContext().bindingLayout(PopupOrientationBinding::inflate)

    /** 点击 */
    private var mOnClickListener: ((popup: PopupWindow, orientation: Int) -> Unit)? = null

    override fun getViewBindingLayout(): View = mBinding.root

    override fun setListeners() {
        super.setListeners()
        // 纵向按钮
        mBinding.verticalBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), RecyclerView.VERTICAL)
        }

        // 横向按钮
        mBinding.horizontalBtn.setOnClickListener {
            mOnClickListener?.invoke(getPopup(), RecyclerView.HORIZONTAL)
        }
    }

    /** 设置RV横纵类型[type] */
    fun setOrientationType(type: Int) {
        mBinding.verticalBtn.setBackgroundResource(if (type == RecyclerView.VERTICAL) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc)
        mBinding.verticalBtn.setTextColor(getContext().getColorCompat(if (type == RecyclerView.VERTICAL) R.color.color_00a0e9 else R.color.color_9a9a9a))
        mBinding.horizontalBtn.setBackgroundResource(if (type == RecyclerView.HORIZONTAL) R.drawable.bg_f0f0f0_stroke_00a0e9 else R.drawable.bg_f0f0f0_stroke_cccccc)
        mBinding.horizontalBtn.setTextColor(getContext().getColorCompat(if (type == RecyclerView.HORIZONTAL) R.color.color_00a0e9 else R.color.color_9a9a9a))
    }

    /** 设置监听器[listener] */
    fun setOnClickListener(listener: (popup: PopupWindow, orientation: Int) -> Unit) {
        mOnClickListener = listener
    }
}